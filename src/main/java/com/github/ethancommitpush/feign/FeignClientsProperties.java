package com.github.ethancommitpush.feign;

import org.springframework.boot.context.properties.ConfigurationProperties;

import feign.Logger;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ConfigurationProperties("feign")
@Setter
@Getter
@ToString
public class FeignClientsProperties {

    private String logLevel = Logger.Level.BASIC.name();

}
