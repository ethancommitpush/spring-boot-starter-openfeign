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
     * Encoder bean name for the specified Feign client interface, to encode parameters
     * in certain way. The encoder bean class must implement the class feign.codec.Encoder.
     */
    String encoder() default "";

    /**
     * Decoder bean name for the specified Feign client interface, to decode parameters
     * in certain way. The decoder bean class must implement the class feign.codec.Decoder.
     */
    String decoder() default "";

    /**
     * Error decoder bean name for the specified Feign client interface, to decode error
     * in certain way. The error decoder bean class must implement the class feign.codec.ErrorDecoder.
     */
    String errorDecoder() default "";

    /**
     * http client bean name for the specified Feign client interface.
     * The client bean class must implement the class feign.Client.
     */
    String client() default "";

}
