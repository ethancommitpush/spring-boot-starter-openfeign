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

import org.springframework.beans.factory.BeanFactory;
import org.springframework.util.StringUtils;

import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;

public class FeignConfigurationUtils {

    @SuppressWarnings("unchecked")
    public static <T> T resolveOverrideableBean(Class<T> hint, BeanFactory beanFactory, String beanName,
            Class<?> beanClass) {
        boolean hasBeanName = !StringUtils.isEmpty(beanName);
        boolean hasClass = (beanClass != null && beanClass != void.class);

        if (hasBeanName && hasClass) {
            throw new IllegalArgumentException(
                String.format("feign client %s bean is exclusive with %s class", hint.getSimpleName(), hint.getSimpleName()));
        }

        if (hasBeanName) {
            return beanFactory.getBean(beanName, hint);
        }

        if (hasClass) {
            try {
                return (T) beanClass.getDeclaredConstructor().newInstance();
            } catch (ReflectiveOperationException e) {
                throw new IllegalArgumentException(e);
            }
        }

        return null;
    }

    public static Decoder resolveDecoder(BeanFactory beanFactory, String decoderBeanName,
            Class<? extends Decoder> decoderClass) {
        return resolveOverrideableBean(Decoder.class, beanFactory, decoderBeanName, decoderClass);
    }

    public static Encoder resolveEncoder(BeanFactory beanFactory, String encoderBeanName,
            Class<? extends Encoder> encoderClass) {
        return resolveOverrideableBean(Encoder.class, beanFactory, encoderBeanName, encoderClass);
    }

    public static ErrorDecoder resolveErrorDecoder(BeanFactory beanFactory, String errorDecoderBeanName,
            Class<? extends ErrorDecoder> errorDecoderClass) {
        return resolveOverrideableBean(ErrorDecoder.class, beanFactory, errorDecoderBeanName, errorDecoderClass);
    }

}
