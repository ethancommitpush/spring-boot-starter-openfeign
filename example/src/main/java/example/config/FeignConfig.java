package example.config;

import feign.Response;
import feign.codec.Decoder;
import feign.jackson.JacksonDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.lang.reflect.Type;

@Configuration
@Slf4j
public class FeignConfig {

    @Bean
    public Decoder feignDecoder() {
        return new JacksonDecoder() {
            @Override
            public Object decode(Response response, Type type) throws IOException {
                log.info("inside overridden feignDecoder.decoder()");
                return super.decode(response, type);
            }
        };
    }

    @Bean
    public Decoder myDecoder() {
        return new JacksonDecoder() {
            @Override
            public Object decode(Response response, Type type) throws IOException {
                log.info("inside myDecoder.decoder()");
                return super.decode(response, type);
            }
        };
    }

}