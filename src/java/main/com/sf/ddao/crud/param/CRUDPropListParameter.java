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

import com.sf.ddao.crud.CRUDDao;
import com.sf.ddao.factory.param.ParameterException;
import com.sf.ddao.factory.param.ParameterHandler;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.chain.Context;

import java.beans.PropertyDescriptor;
import java.lang.reflect.AnnotatedElement;
import java.sql.PreparedStatement;

import static com.sf.ddao.crud.param.CRUDParameterService.getCRUDDaoBean;

/**
 * Date: Oct 27, 2009
 * Time: 3:36:12 PM
 */
public class CRUDPropListParameter implements ParameterHandler {
    public static final String CRUD_PROP_LIST = "crudPropList";
    private PropertyDescriptor[] descriptors;

    public void init(AnnotatedElement element, String param, boolean isRef) {
        assert isRef;
    }

    public String extractParam(Context context) throws ParameterException {
        throw new UnsupportedOperationException();
    }

    public int bindParam(PreparedStatement preparedStatement, int idx, Context ctx) throws ParameterException {
        throw new UnsupportedOperationException();
    }

    public void appendParam(Context ctx, StringBuilder sb) throws ParameterException {
        if (descriptors == null) {
            init(ctx);
        }
        int c = 0;
        for (PropertyDescriptor descriptor : descriptors) {
            if (CRUDDao.IGNORED_PROPS.contains(descriptor.getName())) {
                continue;
            }
            if (c > 0) {
                sb.append(",");
            }
            sb.append(descriptor.getName());
            c++;
        }
    }

    private synchronized void init(Context ctx) {
        if (descriptors != null) {
            return;
        }
        Class<?> beanClass = getCRUDDaoBean(ctx);
        descriptors = PropertyUtils.getPropertyDescriptors(beanClass);
    }
}
