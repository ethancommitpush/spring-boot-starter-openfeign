package com.github.ethancommitpush.feign.decoder;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() >= 400 && response.status() <= 599) {
            log.error("status {} reading {}, url: {}, request body: {}"
                            , response.status(), methodKey, response.request().url()
                            , response.request().requestBody().asString());
        }
        return errorDecoder.decode(methodKey, response);
    }

}