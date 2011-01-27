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

package com.sf.ddao.astore.impl;

import com.sf.ddao.alinker.ALinker;
import com.sf.ddao.alinker.initializer.InitializerException;
import com.sf.ddao.alinker.inject.Link;
import com.sf.ddao.astore.AsyncDB;
import com.sf.ddao.astore.AsyncDBBatchGet;
import com.sf.ddao.astore.DBBatchGet;
import com.sf.ddao.chain.CtxHelper;
import com.sf.ddao.chain.MethodCallCtx;
import com.sf.ddao.factory.StatementFactory;
import com.sf.ddao.factory.param.DefaultParameter;
import com.sf.ddao.factory.param.ParameterHandler;
import com.sf.ddao.handler.Intializible;
import com.sf.ddao.orm.RSMapperFactory;
import com.sf.ddao.orm.RSMapperFactoryRegistry;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

/**
 * Created by psyrtsov
 */
public class AsyncDBBatchGetOperation implements Command, Intializible {
    private final ALinker aLinker;
    private final ParameterHandler cacheKeyParam;
    private final StatementFactory statementFactory;
    private Method method;
    private Class<? extends DBBatchGet> dbBatchGetClass;
    private RSMapperFactory mapRSMapperFactory;


    @Link
    public AsyncDBBatchGetOperation(DefaultParameter cacheKeyParam, StatementFactory statementFactory, ALinker aLinker) {
        this.cacheKeyParam = cacheKeyParam;
        this.statementFactory = statementFactory;
        this.aLinker = aLinker;
    }

    public boolean execute(Context context) throws Exception {
        final MethodCallCtx callCtx = CtxHelper.get(context, MethodCallCtx.class);
        Collection keyList = (Collection) cacheKeyParam.extractParam(context);
        final AsyncDB asyncDB = AsyncStoreHandler.getAsyncDB(context);
        DBBatchGet dbBatchGet = aLinker.create(dbBatchGetClass);
        dbBatchGet.init(this, context);
        @SuppressWarnings({"unchecked"})
        Object res = asyncDB.batchGet(keyList, dbBatchGet);
        callCtx.setLastReturn(res);
        return CONTINUE_PROCESSING;
    }

    public void init(AnnotatedElement element, Annotation annotation) throws InitializerException {
        AsyncDBBatchGet asyncDBBatchGet = (AsyncDBBatchGet) annotation;
        final String sql = asyncDBBatchGet.sql();
        final String cacheKey = asyncDBBatchGet.cacheKey();
        dbBatchGetClass = asyncDBBatchGet.dbBatchGet();
        method = (Method) element;
        final Class<?> returnClass = method.getReturnType();
        if (!Map.class.isAssignableFrom(returnClass)) {
            throw new InitializerException("Method annotated with " + AsyncDBBatchGet.class + " has to have return type Map");
        }
        try {
            mapRSMapperFactory = RSMapperFactoryRegistry.create(method);
            cacheKeyParam.init(element, cacheKey, true);
            statementFactory.init(element, sql);
        } catch (Exception e) {
            throw new InitializerException("Failed to initialize sql operation for " + element, e);
        }
    }

    public ParameterHandler getCacheKeyParam() {
        return cacheKeyParam;
    }

    public StatementFactory getStatementFactory() {
        return statementFactory;
    }

    public Method getMethod() {
        return method;
    }

    public RSMapperFactory getMapORMapperFactory() {
        return mapRSMapperFactory;
    }
}
