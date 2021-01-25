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

import feign.Client;
import feign.Feign;
import feign.Logger.Level;
import feign.slf4j.Slf4jLogger;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.util.Map;

public class FeignClientsFactory<T> implements FactoryBean<Object>, BeanFactoryAware, EnvironmentAware {
    private static final Logger log = LoggerFactory.getLogger(FeignClientsFactory.class);

    private BeanFactory beanFactory;

    private Environment environment;

    private Class<T> apiType;

    private Map<String, Object> attributes;

    @Autowired
    private Client feignClient;

    @Autowired
    private Encoder feignEncoder;

    @Autowired
    private Decoder feignDecoder;

    @Autowired
    private ErrorDecoder feignErrorDecoder;

    @Autowired
    private FeignClientsProperties properties;

    @Override
    public Object getObject() throws Exception {
        Object r = feignBuild();
        log.debug("{} feign client: instance is {}, url is {}", apiType, r, getUrl());
        return r;
    }

    /**
     * Generate feign client.
     *
     * @return generated feign client.
     */
    private T feignBuild() {
        Feign.Builder builder = Feign.builder();

        Client client = resolveClient();
        log.debug("{} feign client {}: http client is {}", apiType, client);
        if (client != null) {
            builder.client(client);
        }

        Encoder encoder = resolveEncoder();
        log.debug("{} feign client {}: encoder is {}", apiType, encoder);
        if (encoder != null) {
            builder.encoder(encoder);
        }

        Decoder decoder = resolveDecoder();
        log.debug("{} feign client {}: decoder is {}", apiType, decoder);
        if (decoder != null) {
            builder.decoder(decoder);
        }

        feign.Logger logger = resolveLogger();
        log.debug("{} feign client {}: logger is {}", apiType, logger);
        if (logger != null) {
            builder.logger(logger);
        }

        Level logLevel = properties.getLogLevel();
        log.debug("{} feign client {}: logger level is {}", apiType, logLevel);
        builder.logLevel(logLevel);

        ErrorDecoder errorDecoder = resolveErrorDecoder();
        log.debug("{} feign client {}: error decoder is {}", apiType, errorDecoder);
        if (errorDecoder != null) {
            builder.errorDecoder(errorDecoder);
        }

        return builder.target(apiType, getUrl());
    }

    public feign.Logger resolveLogger() {
        switch(properties.getLoggerType()) {
            case SYSTEM_ERR: return new feign.Logger.ErrorLogger();
            case JUL: return new feign.Logger.JavaLogger(apiType);
            case NO_OP: return new feign.Logger.NoOpLogger();
            case SLF4J: return new Slf4jLogger(apiType);
        }
        return null;
    }

    public String getUrl() {
        return resolveAttribute((String) attributes.get("url"));
    }


    @Override
    public Class<?> getObjectType() {
        return this.apiType;
    }

    /**
     * Resolves the http client from either &#64;FeignClient annotation or default properties
     *
     * @return client
     */
    @SuppressWarnings("unchecked")
    public Client resolveClient() {
        Class<?> clientClass = (Class<?>) attributes.get("clientClass");
        String clientBeanName = (String) attributes.get("client");

        Client client = FeignConfigurationUtils.resolveClient(beanFactory, clientBeanName,
                (Class<? extends Client>) clientClass);
        if (client != null) {
            return client;
        }

        return feignClient;
    }

    /**
     * Resolves the encoder from either &#64;FeignClient annotation or default properties
     *
     * @return encoder
     */
    @SuppressWarnings("unchecked")
    public Encoder resolveEncoder() {
        Class<?> encoderClass = (Class<?>) attributes.get("encoderClass");
        String encoderBeanName = (String) attributes.get("encoder");

        Encoder encoder = FeignConfigurationUtils.resolveEncoder(beanFactory, encoderBeanName,
                (Class<? extends Encoder>) encoderClass);
        if (encoder != null) {
            return encoder;
        }

        return feignEncoder;
    }

    /**
     * Resolves the decoder from either &#64;FeignClient annotation or default properties
     *
     * @return decoder
     */
    @SuppressWarnings("unchecked")
    public Decoder resolveDecoder() {
        Class<?> decoderClass = (Class<?>) attributes.get("decoderClass");
        String decoderBeanName = (String) attributes.get("decoder");

        Decoder decoder = FeignConfigurationUtils.resolveDecoder(beanFactory, decoderBeanName,
                (Class<? extends Decoder>) decoderClass);
        if (decoder != null) {
            return decoder;
        }

        return feignDecoder;
    }

    /**
     * Resolves the error decoder from either &#64;FeignClient annotation or default properties
     *
     * @return error decoder
     */
    @SuppressWarnings("unchecked")
    public ErrorDecoder resolveErrorDecoder() {
        Class<?> errorDecoderClass = (Class<?>) attributes.get("errorDecoderClass");
        String errorDecoderBeanName = (String) attributes.get("errorDecoder");

        ErrorDecoder errorDecoder = FeignConfigurationUtils.resolveErrorDecoder(beanFactory, errorDecoderBeanName,
                (Class<? extends ErrorDecoder>) errorDecoderClass);
        if (errorDecoder != null) {
            return errorDecoder;
        }

        return feignErrorDecoder;
    }

    /**
     * Get the value or resolve placeholders to find the value configured at the property file.
     * @return value.
     */
    public String resolveAttribute(String value) {
        if (StringUtils.hasText(value)) {
            return environment.resolvePlaceholders(value);
        }
        return value;
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

    public Class<T> getApiType() {
        return apiType;
    }

    public void setApiType(Class<T> apiType) {
        this.apiType = apiType;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public void setProperties(FeignClientsProperties properties) {
        this.properties = properties;
    }

}
