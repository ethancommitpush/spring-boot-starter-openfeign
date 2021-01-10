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
package com.github.ethancommitpush.feign.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented

/**
 * Annotation for declaring on interfaces to automatically generate feign clients
 */
public @interface FeignClient {

    /**
     * @return an URL prefix for concatenating with relative path. It can be either
     * an absolute URL (e.g. https://postman-echo.com) or a placeholders (e.g. ${postman-echo.domain})
     */
    String url();

    /**
     * Encoder class for the specified Feign client interface, to encode parameters
     * in certain way. The encoder class must implement the class feign.codec.Encoder.
     * 
     * Note: 
     * 1) exclusive with attribute 'encoderBean'
     * 2) if specified, override FeignClientsProperties.defaultEncoderClass/defaultEncoderBean
     * 
     * @return encoder class for the specified Feign client interface
     */
    Class<?> encoder() default void.class;

    /**
     * Decoder class for the specified Feign client interface, to decode parameters
     * in certain way. The decoder class must implement the class feign.codec.Decoder.
     * 
     * Note:
     * 1) exclusive with attribute 'decoderBean'
     * 2) if specified, override FeignClientsProperties.defaultDecoderClass/defaultDecoderBean
     * 
     * @return decoder class for the specified Feign client interface
     */
    Class<?> decoder() default void.class;

    /**
     * Encoder bean name for the specified Feign client interface, to encode parameters
     * in certain way. The encoder bean class must implement the class feign.codec.Encoder.
     * 
     * Note: 
     * 1) exclusive with attribute 'encoder'
     * 2) if specified, override FeignClientsProperties.defaultEncoderClass/defaultEncoderBean
     * 
     * @return encoder bean for the specified Feign client interface
     */
    String encoderBean() default "";

    /**
     * Decoder bean name for the specified Feign client interface, to decode parameters
     * in certain way. The decoder bean class must implement the class feign.codec.Decoder.
     * 
     * Note:
     * 1) exclusive with attribute 'decoder'
     * 2) if specified, override FeignClientsProperties.defaultDecoderClass/defaultDecoderBean
     * 
     * @return decoder bean for the specified Feign client interface
     */
    String decoderBean() default "";

    /**
     * Error decoder bean name for the specified Feign client interface, to decode error
     * in certain way. The error decoder bean class must implement the class feign.codec.ErrorDecoder.
     * 
     * Note:
     * 1) exclusive with attribute 'errorDecoder'
     * 2) if specified, override FeignClientsProperties.defaultErrorDecoderClass/defaultErrorDecoderBean
     * 
     * @return error decoder bean for the specified Feign client interface
     */
    String errorDecoderBean() default "";

    /**
     * Error decoder class for the specified Feign client interface, to decode error
     * in certain way. The error decoder class must implement the class feign.codec.ErrorDecoder.
     * 
     * Note:
     * 1) exclusive with attribute 'errorDecoderBean'
     * 2) if specified, override FeignClientsProperties.defaultErrorDecoderClass/defaultErrorDecoderBean
     * 
     * @return error decoder class for the specified Feign client interface
     */
    Class<?> errorDecoder() default void.class;

    /**
     * http client class for the specified Feign client interface. 
     * The client class must implement the class feign.Client.
     * 
     * Note:
     * 1) exclusive with attribute 'clientBean'
     * 2) if specified, override FeignClientsProperties.defaultClientClass/defaultClientBean
     * 
     * @return client class for the specified Feign client interface
     */
    Class<?> client() default void.class;

    /**
     * http client bean name for the specified Feign client interface. 
     * The client bean class must implement the class feign.Client.
     * 
     * Note: 
     * 1) exclusive with attribute 'client'
     * 2) if specified, override FeignClientsProperties.defaultClientClass/defaulClientBean
     * 
     * @return client bean for the specified Feign client interface
     */
    String clientBean() default "";

}