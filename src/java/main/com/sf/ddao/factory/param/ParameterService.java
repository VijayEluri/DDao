/*
 * Copyright 2008 Pavel Syrtsov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations
 *  under the License.
 */

package com.sf.ddao.factory.param;

import java.lang.reflect.AnnotatedElement;

/**
 * Date: Oct 27, 2009
 * Time: 2:16:25 PM
 */
public interface ParameterService {
    void register(ParameterFactory parameterFactory);

    ParameterHandler create(AnnotatedElement element, String funcName, String paramName, boolean isRef);
}
