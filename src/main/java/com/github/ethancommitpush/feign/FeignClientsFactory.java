package com.github.ethancommitpush.feign;

import feign.Feign;
import feign.Logger;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.httpclient.ApacheHttpClient;
import lombok.Getter;
import lombok.Setter;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import javax.net.ssl.SSLContext;

import java.security.cert.X509Certificate;

/**
 * 
 */
@Getter
@Setter
public class FeignClientsFactory implements FactoryBean<Object>, InitializingBean {
    
    private Class<?> apiType;
    
    private String url;
    
    @Autowired 
    private Encoder encoder;

    @Autowired
    private Decoder decoder;

    @Autowired
    private ErrorDecoder errorDecoder;
    
    private String logLevel;

    @Override
    public  void afterPropertiesSet() throws Exception {
        System.out.println("---------------------------------- apiType=" + apiType + ", url=" + url 
                + ", errorDecoder=" + errorDecoder
                + ", decoder=" + decoder
                + ", encoder=" + encoder + ", logLevel=" + logLevel);
    }

    @Override
    public Object getObject() throws Exception {
        return feignBuild(apiType, url, encoder, logLevel);
    }

    /**
     * Get a default httpClient which trust self-signed certificates.
     * 
     * @return default httpClient.
     */
    private CloseableHttpClient getHttpClient() {
        CloseableHttpClient httpClient = null;
        try {
            // To trust self-signed certificates
            TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
            SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
            httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
        } catch (Exception e) {
        }
        return httpClient;
    }

    /**
     * Generate feign client.
     * 
     * @param apiType  class type of the interface which declared with
     *                 &#64;FeignClient.
     * @param url      url from the attributes of the &#64;FeignClient annotation.
     * @param encoder  encoder from the attributes of the &#64;FeignClient
     *                 annotation.
     * @param logLevel log level.
     * @return generated feign client.
     */
    private <T> T feignBuild(Class<T> apiType, String url, Encoder encoder, String logLevel) {
        Feign.Builder builder = Feign.builder().client(new ApacheHttpClient(getHttpClient()))
                .encoder(getEncoder())
                .decoder(getDecoder())
                .logger(new Logger.ErrorLogger())
                .logLevel(Logger.Level.valueOf(logLevel));

        if (getErrorDecoder() != null) {
            builder.errorDecoder(getErrorDecoder());
        }

        return builder.target(apiType, url);
    }

    @Override
    public Class<?> getObjectType() {
        return this.apiType;
    }

}
