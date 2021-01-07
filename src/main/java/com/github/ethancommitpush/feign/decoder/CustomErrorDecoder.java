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
package com.github.ethancommitpush.feign.decoder;

import feign.Request;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * A custom errorDecoder to log exceptions.
 */
@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();

    /**
     * Decode an HTTP {@link Response}.
     * @param methodKey configKey of the java method that invoked the request.
     * @param response HTTP response.
     * @return Exception.
     */
    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() >= 400 && response.status() <= 599) {
            log.error("status {} reading {}, url: {}, request body: {}"
                    , response.status(), methodKey, response.request().url()
                    , requestBodyAsString(response.request()));
        }
        return errorDecoder.decode(methodKey, response);
    }

    /**
     * Retrieve request body as string.
     *
     * Derived from feign.Request.Body.asString()
     *
     * Since (inclusively) feign 10.7.3, Request.requestBody().asString() is not available, so
     * we need to workaround it.
     *
     * @param request
     * @return
     */
    public static String requestBodyAsString(Request request) {
        return !request.isBinary()
                ? new String(request.body(), request.charset())
                : "Binary data";
    }

}