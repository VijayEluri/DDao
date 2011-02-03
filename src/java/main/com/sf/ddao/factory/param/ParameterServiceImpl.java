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

import com.sf.ddao.alinker.ALinker;
import com.sf.ddao.alinker.inject.Link;

import java.lang.reflect.AnnotatedElement;
import java.util.HashMap;
import java.util.Map;

/**
 * Created-By: Pavel Syrtsov
 * Date: Apr 10, 2008
 * Time: 4:14:54 PM
 */
public class ParameterServiceImpl implements ParameterService {
    public static final Map<String, Class<? extends ParameterHandler>> classMap = new HashMap<String, Class<? extends ParameterHandler>>() {{
        put(ContextParameter.FUNC_NAME, ContextParameter.class);
        put(JoinListParameter.FUNC_NAME, JoinListParameter.class);
        put(ThreadLocalParameter.FUNC_NAME, ThreadLocalParameter.class);
        put(ForwardParameter.FUNC_NAME, ForwardParameter.class);
    }};
    @Link
    public ALinker aLinker;

    public void register(ParameterFactory parameterFactory) {
        for (String name : classMap.keySet()) {
            parameterFactory.register(name, this);
        }
    }

    public ParameterHandler create(AnnotatedElement element, String funcName, String paramName, boolean isRef) {
        final Class<? extends ParameterHandler> aClass = classMap.get(funcName);
        final ParameterHandler res;
        try {
            res = aClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        aLinker.init(res);
        res.init(element, paramName, isRef);
        return res;
    }
}