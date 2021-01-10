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
package com.github.ethancommitpush.feign;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.github.ethancommitpush.feign.decoder.CustomErrorDecoder;
import com.github.ethancommitpush.feign.example.TargetInterface;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.env.Environment;

import feign.Client;
import feign.FeignException;
import feign.Request;
import feign.Request.Options;
import feign.RequestTemplate;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;

public class FeignClientsFactoryTest {

    @Mock
    private BeanFactory beanFactory;

    @Mock
    private Environment environment;

    @Mock
    private Encoder feignEncoder;

    @Mock
    private Decoder feignDecoder;

    @Mock
    private ErrorDecoder feignErrorDecoder;

    @Mock
    private Client feignClient;

    private FeignClientsProperties properties = new FeignClientsProperties();

    @InjectMocks
    FeignClientsFactory<TargetInterface> target;

    Map<String, Object> attributes;

    @Before
    public void beforeEach() {
        MockitoAnnotations.initMocks(this);

        this.attributes = new HashMap<String, Object>();
        this.target.setAttributes(this.attributes);

        this.properties.setLoggerKind(FeignLoggerKind.NO_OP);
        this.target.setProperties(this.properties);
    }

    @Test
    public void test_getObject_happy() throws Exception {
        this.target.setApiType(TargetInterface.class);

        this.attributes.put("url", "http://test");
        when(this.environment.resolvePlaceholders("http://test")).thenReturn("http://test");

        Object actual = this.target.getObject();
        Assert.assertNotNull(actual);
        Assert.assertTrue(actual instanceof TargetInterface);
    }

    @Test
    public void test_getUrl_happy() {
        String value = "${postman-echo.domain}";
        this.attributes.put("url", value);
        when(this.environment.resolvePlaceholders(value)).thenReturn("https://postman-echo.com");

        String actual = this.target.getUrl();
        Assert.assertEquals("https://postman-echo.com", actual);
    }

    static class MyEncoder implements Encoder {

        @Override
        public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
            throw new RuntimeException("should not be here");
        }
    }

    @Test
    public void test_resolveEncoder_withDefaultOne() {
        Encoder actual = this.target.resolveEncoder();
        Assert.assertSame(this.feignEncoder, actual);
    }

    @Test
    public void test_resolveEncoder_withAttribute_bean() {
        Encoder expected = new MyEncoder();

        this.attributes.put("encoderBean", "myEncoderBean");
        when(this.beanFactory.getBean("myEncoderBean", Encoder.class)).thenReturn(expected);

        Encoder actual = this.target.resolveEncoder();
        Assert.assertSame(expected, actual);
    }

    @Test
    public void test_resolveEncoder_withAttribute_class() {
        this.attributes.put("encoder", MyEncoder.class);

        Encoder actual = this.target.resolveEncoder();
        Assert.assertTrue(actual.getClass() == MyEncoder.class);
    }

    static class MyDecoder implements Decoder {

        @Override
        public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
            throw new RuntimeException("should not be here");
        }
    }

    @Test
    public void test_resolveDecoder_withDefaultOne() {
        Decoder actual = this.target.resolveDecoder();
        Assert.assertSame(this.feignDecoder, actual);
    }

    @Test
    public void test_resolveDecoder_withAttribute_bean() {
        Decoder expected = new MyDecoder();

        this.attributes.put("decoderBean", "myDecoderBean");
        when(this.beanFactory.getBean("myDecoderBean", Decoder.class)).thenReturn(expected);

        Decoder actual = this.target.resolveDecoder();
        Assert.assertSame(expected, actual);
    }

    @Test
    public void test_resolveDecoder_withAttribute_class() {
        this.attributes.put("decoder", MyDecoder.class);

        Decoder actual = this.target.resolveDecoder();
        Assert.assertTrue(actual.getClass() == MyDecoder.class);
    }

    static class MyErrorDecoder extends CustomErrorDecoder {
    }

    @Test
    public void test_resolveErrorDecoder_withDefaultOne() {
        ErrorDecoder actual = this.target.resolveErrorDecoder();
        Assert.assertSame(this.feignErrorDecoder, actual);
    }

    @Test
    public void test_resolveErrorDecoder_withAttribute_bean() {
        ErrorDecoder expected = new CustomErrorDecoder();

        this.attributes.put("errorDecoderBean", "myErrorDecoderBean");
        when(this.beanFactory.getBean("myErrorDecoderBean", ErrorDecoder.class)).thenReturn(expected);

        ErrorDecoder actual = this.target.resolveErrorDecoder();
        Assert.assertSame(expected, actual);
    }

    @Test
    public void test_resolveErrorDecoder_withAttribute_class() {
        this.attributes.put("errorDecoder", MyErrorDecoder.class);

        ErrorDecoder actual = this.target.resolveErrorDecoder();
        Assert.assertTrue(actual.getClass() == MyErrorDecoder.class);
    }

    @Test
    public void test_resolveAttribute_placeHolder() {
        String value = "${test.ok}";
        when(this.environment.resolvePlaceholders(value)).thenReturn("got");

        String actual = this.target.resolveAttribute(value);
        Assert.assertEquals("got", actual);

        when(this.environment.resolvePlaceholders(any())).thenReturn(value);
        Assert.assertEquals(value, this.target.resolveAttribute(value));
    }

    static class MyClient implements Client {

        @Override
        public Response execute(Request request, Options options) throws IOException {
            throw new RuntimeException("should not be here");
        }
    }

    @Test
    public void test_resolveClient_withDefaultOne() {
        Client actual = this.target.resolveClient();
        Assert.assertSame(this.feignClient, actual);
    }

    @Test
    public void test_resolveClient_withAttribute_bean() {
        Client expected = new MyClient();

        this.attributes.put("clientBean", "myClientBean");
        when(this.beanFactory.getBean("myClientBean", Client.class)).thenReturn(expected);

        Client actual = this.target.resolveClient();
        Assert.assertSame(expected, actual);
    }

    @Test
    public void test_resolveClient_withAttribute_class() {        
        this.attributes.put("client", MyClient.class);

        Client actual = this.target.resolveClient();
        Assert.assertTrue(actual.getClass() == MyClient.class);
    }

}
