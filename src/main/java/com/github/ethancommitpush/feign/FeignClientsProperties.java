package com.github.ethancommitpush.feign;

import org.springframework.boot.context.properties.ConfigurationProperties;

import feign.Logger;
import feign.codec.Decoder;
import feign.codec.Encoder;
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

    private String logLevel = Logger.Level.BASIC.name();

    private Class<? extends Decoder> defaultDecoderClass = JacksonDecoder.class;

    private Class<? extends Encoder> defaultEncoderClass = JacksonEncoder.class;

}
