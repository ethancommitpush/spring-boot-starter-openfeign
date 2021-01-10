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

import feign.Feign;
import feign.Logger;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.httpclient.ApacheHttpClient;
import lombok.Getter;
import lombok.Setter;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.util.StringUtils;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import javax.net.ssl.SSLContext;

import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * 
 */
@Getter
@Setter
@EnableConfigurationProperties(FeignClientsProperties.class)
public class FeignClientsFactory<T> implements FactoryBean<Object>, BeanFactoryAware, EnvironmentAware {

    private BeanFactory beanFactory;

    private Environment environment;

    private Class<T> apiType;

    @Autowired
    private Encoder feignEncoder;

    @Autowired
    private Decoder feignDecoder;

    @Autowired
    private ErrorDecoder feignErrorDecoder;

    private Map<String, Object> attributes;

    @Autowired
    private FeignClientsProperties properties;

    @Override
    public Object getObject() throws Exception {
        return feignBuild();
    }

    /**
     * Get a default httpClient which trust self-signed certificates.
     * 
     * @return default httpClient.
     */
    private CloseableHttpClient getHttpClient() {
        CloseableHttpClient httpClient = null;
        try {
            // To trust self-signed certificates
            TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
            SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
            httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
        } catch (Exception e) {
        }
        return httpClient;
    }

    /**
     * Generate feign client.
     * 
     * @param apiType  class type of the interface which declared with
     *                 &#64;FeignClient.
     * @param url      url from the attributes of the &#64;FeignClient annotation.
     * @param encoder  encoder from the attributes of the &#64;FeignClient
     *                 annotation.
     * @param logLevel log level.
     * @return generated feign client.
     */
    private T feignBuild() {
        Feign.Builder builder = Feign.builder();
        
        builder.client(new ApacheHttpClient(getHttpClient()));

        Encoder encoder = resolveEncoder();
        if (encoder != null) {
            builder.encoder(encoder);
        }

        Decoder decoder = resolveDecoder();
        if (decoder != null) {
            builder.decoder(decoder);
        }

        Logger logger = FeignLoggerKind.resolve(getProperties().getLoggerKind(), getApiType());
        if (logger != null) {
            builder.logger(logger);
        }

        builder.logLevel(getProperties().getLogLevel());

        ErrorDecoder errorDecoder = resolveErrorDecoder();
        if (errorDecoder != null) {
            builder.errorDecoder(errorDecoder);
        }

        return builder.target(getApiType(), getUrl());
    }

    public String getUrl() {
        return resolveAttribute((String)getAttributes().get("url"));
    }
    

    @Override
    public Class<?> getObjectType() {
        return this.apiType;
    }

    /**
     * Get the encoder value from the attributes of the &#64;FeignClient annotation.
     * 
     * @return encoder.
     */
    @SuppressWarnings("unchecked")
    public Encoder resolveEncoder() {
        Class<?> encoderClass = (Class<?>) getAttributes().get("encoder");
        String encoderBeanName = (String) getAttributes().get("encoderBean");

        Encoder encoder = FeignConfigurationUtils.resolveEncoder(getBeanFactory(), encoderBeanName,
                (Class<? extends Encoder>) encoderClass);
        if (encoder != null) {
            return encoder;
        }

        return getFeignEncoder();
    }

    /**
     * Get the decoder value from the attributes of the &#64;FeignClient annotation.
     * 
     * @return decoder.
     */
    @SuppressWarnings("unchecked")
    public Decoder resolveDecoder() {
        Class<?> decoderClass = (Class<?>) getAttributes().get("decoder");
        String decoderBeanName = (String) getAttributes().get("decoderBean");

        Decoder decoder = FeignConfigurationUtils.resolveDecoder(getBeanFactory(), decoderBeanName,
                (Class<? extends Decoder>) decoderClass);
        if (decoder != null) {
            return decoder;
        }

        return getFeignDecoder();
    }

    /**
     * Get the default error decoder value from the attributes of the &#64;FeignClient annotation.
     * 
     * @return decoder.
     */
    @SuppressWarnings("unchecked")
    public ErrorDecoder resolveErrorDecoder() {
        Class<?> errorDecoderClass = (Class<?>) getAttributes().get("errorDecoder");
        String errorDecoderBeanName = (String) getAttributes().get("errorDecoderBean");

        ErrorDecoder errorDecoder = FeignConfigurationUtils.resolveErrorDecoder(getBeanFactory(), errorDecoderBeanName,
                (Class<? extends ErrorDecoder>) errorDecoderClass);
        if (errorDecoder != null) {
            return errorDecoder;
        }

        return getFeignErrorDecoder();
    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /**
     * Set the {@code Environment} that this component runs in.
     * @see org.springframework.context.EnvironmentAware
     */
    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    /**
     * Get the value or resolve placeholders to find the value configured at the property file.
     * @return value.
     */
    public String resolveAttribute(String value) {
        if (StringUtils.hasText(value)) {
            return getEnvironment().resolvePlaceholders(value);
        }
        return value;
    }

}
