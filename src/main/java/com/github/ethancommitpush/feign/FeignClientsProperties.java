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

import com.github.ethancommitpush.feign.decoder.CustomErrorDecoder;

import org.springframework.boot.context.properties.ConfigurationProperties;

import feign.Client;
import feign.Logger;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ConfigurationProperties("feign")
@Setter
@Getter
@ToString
public class FeignClientsProperties {

    private Logger.Level logLevel = Logger.Level.BASIC;

    private FeignLoggerKind loggerKind = FeignLoggerKind.SYSTEM_ERR;

    /**
     * Default decoder class. 
     * The decoder class must implement the class feign.codec.Decoder.
     * 
     * Note:
     * 1) exclusive with property 'defaultDecoderBean'
     * 2) override-able by FeignClient.decoder/decoderBean
     * 
     * @return decoder class
     */
    private Class<? extends Decoder> defaultDecoderClass = JacksonDecoder.class;

    /**
     * Default encoder class.
     * The encoder class must implement the class feign.codec.Encoder.
     * 
     * Note: 
     * 1) exclusive with property 'defaultEncoderBean'
     * 2) override-able by FeignClient.encoder/encoderBean
     * 
     * @return encoder class
     */
    private Class<? extends Encoder> defaultEncoderClass = JacksonEncoder.class;
    
    /**
     * Default encoder bean name.
     * The encoder bean class must implement the class feign.codec.Encoder.
     * 
     * Note: 
     * 1) exclusive with property 'defaultEncoderBean'
     * 2) override-able by FeignClient.encoder/encoderBean
     * 
     * @return encoder bean name
     */
    private String defaultEncoderBean;

    /**
     * Default decoder bean name. 
     * The decoder bean class must implement the class feign.codec.Decoder.
     * 
     * Note:
     * 1) exclusive with property 'defaultDecoderBean'
     * 2) override-able by FeignClient.decoder/decoderBean
     * 
     * @return decoder bean name
     */
    private String defaultDecoderBean;

    /**
     * Default error decoder bean name. 
     * The error decoder bean class must implement the class feign.codec.ErrorDecoder.
     * 
     * Note:
     * 1) exclusive with property 'defaultErorrDecoderBean'
     * 2) override-able by FeignClient.errorDecoder/errorDecoderBean
     * 
     * @return error decoder bean name
     */
    private String defaultErrorDecoderBean;
    
    /**
     * Default error decoder class. 
     * The error decoder class must implement the class feign.codec.ErrorDecoder.
     * 
     * Note:
     * 1) exclusive with property 'defaultErrorDecoderBean'
     * 2) override-able by FeignClient.errorDecoder/errorDecoderBean
     * 
     * @return decoder class
     */
    private Class<? extends ErrorDecoder> defaultErrorDecoderClass = CustomErrorDecoder.class;

    /**
     * Default http client class.
     * The client class must implement the class feign.Client.
     * 
     * Note: 
     * 1) exclusive with property 'defaultClientBean'
     * 2) override-able by FeignClient.client/clientBean
     * 
     * @return client class
     */
    private Class<Client> defaultClientClass;
    
    /**
     * Default client bean name.
     * The cilent bean class must implement the class feign.Client.
     * 
     * Note: 
     * 1) exclusive with property 'defaultClientBean'
     * 2) override-able by FeignClient.client/clientBean
     * 
     * @return client bean name
     */
    private String defaultClientBean;

}
