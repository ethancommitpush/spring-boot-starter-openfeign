package com.github.ethancommitpush.feign;

import com.github.ethancommitpush.feign.decoder.CustomErrorDecoder;

import org.springframework.boot.context.properties.ConfigurationProperties;

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

    private String logLevel = Logger.Level.BASIC.name();
    
    /**
     * Default decoder class. 
     * The decoder class must implement the class feign.codec.Decoder.
     * 
     * Note:
     * 1) exclusive with property 'defaultDecoderBean'
     * 2) override-able by FeignClient.decoderClass/decoderBean
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
     * 2) override-able by FeignClient.encoderClass/encoderBean
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
     * 2) override-able by FeignClient.encoderClass/encoderBean
     * 
     * @return encoder bean
     */
    private String defaultEncoderBean;

    /**
     * Default decoder bean name. 
     * The decoder bean class must implement the class feign.codec.Decoder.
     * 
     * Note:
     * 1) exclusive with property 'defaultDecoderBean'
     * 2) override-able by FeignClient.decoderClass/decoderBean
     * 
     * @return decoder bean
     */
    private String defaultDecoderBean;

    /**
     * Default error decoder bean name. 
     * The error decoder bean class must implement the class feign.codec.ErrorDecoder.
     * 
     * Note:
     * 1) exclusive with property 'defaultErorrDecoderBean'
     * 2) override-able by FeignClient.errorDecoderClass/errorDecoderBean
     * 
     * @return error decoder bean
     */
    private String defaultErrorDecoderBean;
    
    /**
     * Default error decoder class. 
     * The error decoder class must implement the class feign.codec.ErrorDecoder.
     * 
     * Note:
     * 1) exclusive with property 'defaultErrorDecoderBean'
     * 2) override-able by FeignClient.errorDecoderClass/errorDecoderBean
     * 
     * @return decoder class
     */
    private Class<? extends ErrorDecoder> defaultErrorDecoderClass = CustomErrorDecoder.class;

}
