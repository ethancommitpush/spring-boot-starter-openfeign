/**
 * Copyright 2012-2020 Yisin Lin
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.github.ethancommitpush.feign;

import com.github.ethancommitpush.feign.annotation.FeignClient;
import com.github.ethancommitpush.feign.decoder.CustomErrorDecoder;
import feign.Feign;
import feign.Logger;
import feign.codec.Encoder;
import feign.httpclient.ApacheHttpClient;
import feign.jackson.JacksonDecoder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import javax.net.ssl.SSLContext;
import java.beans.Introspector;
import java.security.cert.X509Certificate;
import java.util.*;

public class FeignClientsRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware, BeanFactoryAware {

    private static final String BASE_PACKAGES_KEY = "feign.base-packages";
    private static final String LOG_LEVEL_KEY = "feign.log-level";

    private Environment environment;
    private ResourceLoader resourceLoader;
    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        registerFeignClients(metadata, registry);
    }

    public void registerFeignClients(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        ClassPathScanningCandidateComponentProvider scanner = getScanner();
        scanner.setResourceLoader(this.resourceLoader);

        AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(FeignClient.class);
        scanner.addIncludeFilter(annotationTypeFilter);
        List<String> basePackages = Optional.ofNullable(environment.getProperty(BASE_PACKAGES_KEY))
                .map(s -> Arrays.asList(s.split("\\,"))).orElse(Collections.emptyList());
        String logLevel = Optional.ofNullable(environment.getProperty(LOG_LEVEL_KEY))
                .orElse(Logger.Level.BASIC.name());com.fasterxml.jackson.databind.PropertyNamingStrategy.UpperCamelCaseStrategy a;

        basePackages.stream()
                .map(p -> scanner.findCandidateComponents(p))
                .flatMap(Collection::stream)
                .filter(bd -> bd instanceof AnnotatedBeanDefinition)
                .map(bd -> (AnnotatedBeanDefinition) bd)
                .map(abd -> abd.getMetadata())
                .filter(meta -> meta.isInterface())
                .forEach(meta -> {
                    Map<String, Object> attributes = meta.getAnnotationAttributes(FeignClient.class.getCanonicalName());
                    registerFeignClient(meta, attributes, logLevel);
                });
    }

    private void registerFeignClient(AnnotationMetadata annotationMetadata, Map<String, Object> attributes, String logLevel) {
        String className = annotationMetadata.getClassName();
        String shortClassName = ClassUtils.getShortName(className);
        String beanName =  Introspector.decapitalize(shortClassName);
        Encoder encoder = getEncoder(attributes);
        Class<?> apiType = null;
        try {
            apiType = Class.forName(className);
        } catch (Exception e) {

        }
        Object bean = feignBuild(apiType, resolve((String) attributes.get("url")), encoder, logLevel);
        ((ConfigurableListableBeanFactory) beanFactory).registerSingleton(beanName, bean);
    }

    private <T> T feignBuild(Class<T> apiType, String url, Encoder encoder, String logLevel) {
        Feign.Builder builder = Feign.builder()
                .client(new ApacheHttpClient(getHttpClient()))
                .errorDecoder(new CustomErrorDecoder())
                .decoder(new JacksonDecoder())
                .logger(new Logger.ErrorLogger())
                .logLevel(Logger.Level.valueOf(logLevel));
        if (encoder != null) {
            builder.encoder(encoder);
        }

        return builder.target(apiType, url);
    }

    private CloseableHttpClient getHttpClient() {
        CloseableHttpClient httpClient = null;
        try {
            //To trust self-signed certificates
            TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
            SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
            httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
        } catch (Exception e) {
        }
        return httpClient;
    }

    private Encoder getEncoder(Map<String, Object> attributes) {
        if (attributes.get("encoder") == null) {
            return null;
        }

        Encoder encoder = null;
        try {
            encoder = (Encoder) ((Class<?>) attributes.get("encoder")).newInstance();
        } catch (Exception e) {
        }
        return encoder;
    }

    private String resolve(String value) {
        if (StringUtils.hasText(value)) {
            return this.environment.resolvePlaceholders(value);
        }
        return value;
    }

    private ClassPathScanningCandidateComponentProvider getScanner() {
        return new ClassPathScanningCandidateComponentProvider(false, this.environment) {
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                if (!beanDefinition.getMetadata().isIndependent()) {
                    return false;
                }
                return !beanDefinition.getMetadata().isAnnotation();
            }
        };
    }

}