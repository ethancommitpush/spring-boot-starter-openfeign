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
import org.junit.Assert;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.BeanFactory;

public class FeignConfigurationUtilsTest {
    
    static interface TargetInterface {

    }

    static class TargetClassOK implements TargetInterface {

    }

    static class TargetClassWrong implements TargetInterface {
        TargetClassWrong() {
            throw new RuntimeException("mock error");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_resolveOverrideableBean_bothBeanNameAndClass() {
        BeanFactory bf = mock(BeanFactory.class);
        
        FeignConfigurationUtils.resolveOverrideableBean(TargetInterface.class, 
            bf, "target", TargetClassOK.class);
    }

    @Test
    public void test_resolveOverrideableBean_withBeanName() {
        TargetClassOK expected = new TargetClassOK();

        BeanFactory bf = mock(BeanFactory.class);
        when(bf.getBean("target", TargetInterface.class)).thenReturn(expected);
        
        TargetInterface actual = FeignConfigurationUtils.resolveOverrideableBean(TargetInterface.class, 
            bf, "target", null);
        Assert.assertSame(expected, actual);
    }

    @Test
    public void test_resolveOverrideableBean_withClass() {
        BeanFactory bf = mock(BeanFactory.class);
        
        TargetInterface actual = FeignConfigurationUtils.resolveOverrideableBean(TargetInterface.class, 
            bf, "", TargetClassOK.class);
        Assert.assertTrue(actual instanceof TargetClassOK);

        verify(bf, never()).getBean(anyString(), (Class<?>)any());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_resolveOverrideableBean_wrongClass() {
        BeanFactory bf = mock(BeanFactory.class);
        
        FeignConfigurationUtils.resolveOverrideableBean(TargetInterface.class, 
            bf, "", TargetClassWrong.class);
    }


}
