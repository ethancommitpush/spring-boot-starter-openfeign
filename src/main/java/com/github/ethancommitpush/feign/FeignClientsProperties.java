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
    
    /**
     * Default decoder class for the specified Feign client interface, to decode parameters
     * in certain way. The decoder class must implement the class feign.codec.Decoder.
     * 
     * Note:
     * 1) exclusive with property 'decoderBean'
     * 2) override-able by FeignClient.decoderClass/decoderBean
     * 
     * @return decoder class for the specified Feign client interface
     */
    private Class<? extends Decoder> defaultDecoderClass = JacksonDecoder.class;

    /**
     * Default encoder class for the specified Feign client interface, to encode parameters
     * in certain way. The encoder class must implement the class feign.codec.Encoder.
     * 
     * Note: 
     * 1) exclusive with property 'encoderBean'
     * 2) override-able by FeignClient.encoderClass/encoderBean
     * 
     * @return encoder class for the specified Feign client interface
     */
    private Class<? extends Encoder> defaultEncoderClass = JacksonEncoder.class;
    
    /**
     * Default encoder bean name for the specified Feign client interface, to encode parameters
     * in certain way. The encoder bean class must implement the class feign.codec.Encoder.
     * 
     * Note: 
     * 1) exclusive with property 'encoderBean'
     * 2) override-able by FeignClient.encoderClass/encoderBean
     * 
     * @return encoder bean for the specified Feign client interface
     */
    private String defaultEncoderBean;

    /**
     * Default encoder bean name for the specified Feign client interface, to decode parameters
     * in certain way. The decoder bean class must implement the class feign.codec.Decoder.
     * 
     * Note:
     * 1) exclusive with property 'decoderBean'
     * 2) override-able by FeignClient.decoderClass/decoderBean
     * 
     * @return decoder bean for the specified Feign client interface
     */
    private String defaultDecoderBean;

}
