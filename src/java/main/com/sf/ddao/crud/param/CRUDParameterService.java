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

package com.sf.ddao.crud.param;

import com.sf.ddao.chain.CtxHelper;
import com.sf.ddao.conn.ConnectionHandlerHelper;
import com.sf.ddao.crud.CRUDDao;
import com.sf.ddao.factory.param.ParameterFactory;
import com.sf.ddao.factory.param.ParameterHandler;
import com.sf.ddao.factory.param.ParameterService;
import org.apache.commons.chain.Context;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Date: Oct 27, 2009
 * Time: 3:34:24 PM
 */
public class CRUDParameterService implements ParameterService {
    public static final Map<String, Class<? extends ParameterHandler>> classMap = new HashMap<String, Class<? extends ParameterHandler>>() {{
        put(CRUDBindPropsParameter.CRUD_BIND_PROPS, CRUDBindPropsParameter.class);
        put(CRUDTableNameParameter.CRUD_TABLE_NAME, CRUDTableNameParameter.class);
        put(CRUDPropListParameter.CRUD_PROP_LIST, CRUDPropListParameter.class);
    }};

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
        res.init(element, paramName, isRef);
        return res;
    }

    public static Class<?> getCRUDDaoBean(Context ctx) {
        ConnectionHandlerHelper connectionHandlerHelper = CtxHelper.get(ctx, ConnectionHandlerHelper.class);
        Class iFace = connectionHandlerHelper.getDaoClass();
        for (Type type : iFace.getGenericInterfaces()) {
            final ParameterizedType parameterizedType = (ParameterizedType) type;
            if (parameterizedType.getRawType().equals(CRUDDao.class)) {
                final Type[] typeArguments = parameterizedType.getActualTypeArguments();
                return (Class<?>) typeArguments[0];
            }
        }
        throw new RuntimeException(iFace + "expected to extend " + CRUDDao.class);
    }
}
