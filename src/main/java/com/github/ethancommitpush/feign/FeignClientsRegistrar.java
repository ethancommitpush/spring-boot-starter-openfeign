/**
 * Copyright 2020 Yisin Lin
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
import feign.codec.Decoder;
import feign.codec.Encoder;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
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

import java.beans.Introspector;
import java.util.*;

/**
 * Registrar to register {@link com.github.ethancommitpush.feign.annotation.FeignClient}s.
 */
public class FeignClientsRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {

    private static final String BASE_PACKAGES_KEY = "feign.base-packages";

    private Environment environment;
    private ResourceLoader resourceLoader;

    /**
     * Trigger registering feign clients, but actually metadata and registry are not used at all.
     * @param metadata annotation metadata of the importing class.
     * @param registry current bean definition registry.
     * @see org.springframework.context.annotation.ImportBeanDefinitionRegistrar
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        registerFeignClients(registry);
    }

    /**
     * Scan all interfaces declared with &#64;FeignClient and collect className, attributes, and logLevel.
     */
    public void registerFeignClients(BeanDefinitionRegistry registry) {
        ClassPathScanningCandidateComponentProvider scanner = getScanner();
        scanner.setResourceLoader(this.resourceLoader);

        AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(FeignClient.class);
        scanner.addIncludeFilter(annotationTypeFilter);
        List<String> basePackages = Optional.ofNullable(environment.getProperty(BASE_PACKAGES_KEY))
                .map(s -> Arrays.asList(s.split("\\,"))).orElse(Collections.emptyList());

        basePackages.stream()
                .map(p -> scanner.findCandidateComponents(p))
                .flatMap(Collection::stream)
                .filter(bd -> bd instanceof AnnotatedBeanDefinition)
                .map(bd -> (AnnotatedBeanDefinition) bd)
                .map(abd -> abd.getMetadata())
                .filter(meta -> meta.isInterface())
                .forEach(meta -> {
                    Map<String, Object> attributes = meta.getAnnotationAttributes(FeignClient.class.getCanonicalName());
                    registerFeignClient(registry, meta.getClassName(), attributes);
                });
    }

    /**
     * Register generated feign clients as singletons.
     * @param className class name of the interface which declared with &#64;FeignClient.
     * @param attributes attributes of the &#64;FeignClient annotation.
     * @param logLevel log level configured at property file or as default value: BASIC.
     */
    private void registerFeignClient(BeanDefinitionRegistry registry, String className, Map<String, Object> attributes) {
        String shortClassName = ClassUtils.getShortName(className);
        String beanName =  Introspector.decapitalize(shortClassName);
        
        Class<?> apiType = null;
        try {
            apiType = Class.forName(className);
        } catch (Exception e) {

        }

        String url = resolve((String) attributes.get("url"));

        BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(FeignClientsFactory.class);

		definition.addPropertyValue("apiType", apiType);
        definition.addPropertyValue("url", url);
        definition.addPropertyValue("attributes", attributes);
		definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);

		AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();
		beanDefinition.setAttribute(FactoryBean.OBJECT_TYPE_ATTRIBUTE, className);

		BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, beanName);
		BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
    }

    /**
     * Get the value or resolve placeholders to find the value configured at the property file.
     * @return value.
     */
    private String resolve(String value) {
        if (StringUtils.hasText(value)) {
            return this.environment.resolvePlaceholders(value);
        }
        return value;
    }

    /**
     * Get the class path scanner.
     * @return scanner.
     */
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

    /**
     * Set the ResourceLoader that this object runs in.
     * @param resourceLoader the ResourceLoader object to be used by this object.
     * @see org.springframework.context.ResourceLoaderAware
     */
    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * Set the {@code Environment} that this component runs in.
     * @see org.springframework.context.EnvironmentAware
     */
    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

}
