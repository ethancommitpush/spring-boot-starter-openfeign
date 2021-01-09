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

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

/**
 * {@link org.springframework.boot.autoconfigure.EnableAutoConfiguration
 * Auto-configuration} that sets up feign clients.
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ FeignClient.class })
@Import(FeignClientsRegistrar.class)
public class FeignClientsAutoConfiguration {

    public FeignClientsAutoConfiguration() {
        System.out.println("------------------------------- starter FeignClientsAutoConfiguration.ctor");
    }

    @Bean
    @ConditionalOnMissingBean(Decoder.class)
    public Decoder decoder() {
        System.out.println("------------------------starter decoder--------------------------->");
        return new JacksonDecoder();
    }

    @Bean
    @ConditionalOnMissingBean(Encoder.class)
    public Encoder encoder() {
        System.out.println("------------------------starter encoder--------------------------->");
        return new JacksonEncoder();
    }

}
