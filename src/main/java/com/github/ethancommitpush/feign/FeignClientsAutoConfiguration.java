package com.github.ethancommitpush.feign;

import com.github.ethancommitpush.feign.annotation.FeignClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnClass({ FeignClient.class })
@Import(FeignClientsRegistrar.class)
public class FeignClientsAutoConfiguration {
}