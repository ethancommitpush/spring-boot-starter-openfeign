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
import com.github.ethancommitpush.feign.decoder.CustomErrorDecoder;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import lombok.Getter;

/**
 * {@link org.springframework.boot.autoconfigure.EnableAutoConfiguration
 * Auto-configuration} that sets up feign clients.
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ FeignClient.class })
@Import(FeignClientsRegistrar.class)
@EnableConfigurationProperties(FeignClientsProperties.class)
@Getter
public class FeignClientsAutoConfiguration implements BeanFactoryAware {

    private BeanFactory beanFactory;

    private final FeignClientsProperties properties;

    public FeignClientsAutoConfiguration(FeignClientsProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean(ErrorDecoder.class)
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }

    @Bean
    @ConditionalOnMissingBean(name = "defaultFeignDecoder")
    public Decoder defaultFeignDecoder() {
        System.out.println("------------------------starter decoder--------------------------->");
        
        return FeignConfigurationUtils.resolveDecoder(
            getBeanFactory(), 
            getProperties().getDefaultDecoderBean(), 
            getProperties().getDefaultDecoderClass());
    }

    @Bean
    @ConditionalOnMissingBean(name = "defaultFeignEncoder")
    public Encoder defaultFeignEncoder() {
        System.out.println("------------------------starter encoder--------------------------->");

        return FeignConfigurationUtils.resolveEncoder(
            getBeanFactory(), 
            getProperties().getDefaultEncoderBean(), 
            getProperties().getDefaultEncoderClass());
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

}
