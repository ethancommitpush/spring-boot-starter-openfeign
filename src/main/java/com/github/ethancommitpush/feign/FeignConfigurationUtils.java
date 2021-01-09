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

public class FeignConfigurationUtils {

    public static Decoder resolveDecoder(BeanFactory beanFactory, String decoderBeanName,
            Class<? extends Decoder> decoderClass) {
        boolean hasBeanName = !StringUtils.isEmpty(decoderBeanName);
        boolean hasClass = (decoderClass != null);

        if (hasBeanName && hasClass) {
            throw new IllegalArgumentException("feign client decoder bean is exclusive with decoder class");
        }

        if (hasBeanName) {
            return beanFactory.getBean(decoderBeanName, Decoder.class);
        }

        if (hasClass) {
            try {
                return (Decoder) decoderClass.getDeclaredConstructor().newInstance();
            } catch (ReflectiveOperationException e) {
                throw new IllegalArgumentException(e);
            }
        }

        return null;
    }

    public static Encoder resolveEncoder(BeanFactory beanFactory, String encoderBeanName,
            Class<? extends Encoder> encoderClass) {
        boolean hasBeanName = !StringUtils.isEmpty(encoderBeanName);
        boolean hasClass = (encoderClass != null);

        if (hasBeanName && hasClass) {
            throw new IllegalArgumentException("feign client encoder bean is exclusive with encoder class");
        }

        if (hasBeanName) {
            return beanFactory.getBean(encoderBeanName, Encoder.class);
        }

        if (hasClass) {
            try {
                return (Encoder) encoderClass.getDeclaredConstructor().newInstance();
            } catch (ReflectiveOperationException e) {
                throw new IllegalArgumentException(e);
            }
        }

        return null;
    }

}
