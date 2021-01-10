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

import feign.Logger;
import feign.slf4j.Slf4jLogger;

public enum FeignLoggerKind {
    
    SYSTEM_ERR,
    JUL,
    NO_OP,
    SLF4j;

    public static Logger resolve(FeignLoggerKind kind, Class<?> apiType) {
        switch(kind) {
            case SYSTEM_ERR: return new Logger.ErrorLogger();
            case JUL: return new Logger.JavaLogger(apiType);
            case NO_OP: return new Logger.NoOpLogger();
            case SLF4j: return new Slf4jLogger();
        }
        return null;
    }
}
