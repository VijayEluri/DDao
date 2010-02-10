/**
 * Copyright 2008 Pavel Syrtsov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations
 * under the License.
 */

package com.sf.ddao.alinker;

import com.sf.ddao.alinker.factory.DefaultFactoryManager;
import com.sf.ddao.alinker.factory.FactoryManager;
import com.sf.ddao.alinker.initializer.DefaultInitializerManager;
import com.sf.ddao.alinker.initializer.InitializerException;
import com.sf.ddao.alinker.initializer.InitializerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.AnnotatedElement;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * <p/>
 * Created-By: Pavel Syrtsov
 * Date: Apr 10, 2008
 * Time: 7:38:55 PM
 */
public class ALinker {
    public static final Logger log = LoggerFactory.getLogger(ALinker.class.getName());
    private final FactoryManager factoryManager;
    private final InitializerManager initializerManager;
    private ConcurrentMap<Context, Semaphore> initLocks = new ConcurrentHashMap<Context, Semaphore>();

    private static class InstanceHolder {
        private static ALinker instance = new ALinker();
    }

    public static ALinker instance() {
        return InstanceHolder.instance;
    }

    public ALinker() {
        this.factoryManager = new DefaultFactoryManager(this);
        this.initializerManager = new DefaultInitializerManager(this);
        ((DefaultFactoryManager) factoryManager).init();
        ((DefaultInitializerManager) initializerManager).init();
    }

    public ALinker(FactoryManager factoryManager, InitializerManager initializerManager) {
        this.factoryManager = factoryManager;
        this.initializerManager = initializerManager;
    }

    public <T> T create(Class<T> clazz, AnnotatedElement destination) throws FactoryException, InitializerException {
        Context<T> ctx = new Context<T>(clazz, null, destination, -1);
        return create(ctx);
    }

    public <T> T create(Class<T> clazz) throws FactoryException, InitializerException {
        Context<T> ctx = new Context<T>(clazz, null, null, -1);
        return create(ctx);
    }

    public void init(Object subj) throws InitializerException {
        Class aClass = subj.getClass();
        //noinspection unchecked
        Context<Object> ctx = new Context<Object>(aClass, null, null, -1);
        init(subj, ctx);
    }

    public <T> T create(Context<T> ctx) throws FactoryException, InitializerException {
        Factory<T> factory = factoryManager.getFactory(this, ctx);
        T subj;
        if (factory instanceof CachingFactory) {
            CachingFactory<T> cachingFactory = (CachingFactory<T>) factory;
            //noinspection SynchronizationOnLocalVariableOrMethodParameter
            synchronized (cachingFactory) {
                subj = cachingFactory.getCachedObject(ctx);
                if (subj != null) {
                    // it`s cached and as such should not be initilized again
                    // psdo: problem is that this object may be in process of initializing by other thread
                    return subj;
                }
                subj = cachingFactory.create(this, ctx);
                initLocks.put(ctx, new Semaphore(0));
            }
        } else {
            subj = factory.create(this, ctx);
        }
        try {
            init(subj, ctx);
        } finally {
            final Semaphore semaphore = initLocks.remove(ctx);
            if (semaphore != null) {
                semaphore.release(Integer.MAX_VALUE);
            }
        }
        return subj;
    }

    public <T> void init(T subj, Context<T> ctx) throws InitializerException {
        Iterable<Initializer> initializers = initializerManager.getInitializers(ctx);
        for (Initializer initializer : initializers) {
            //noinspection unchecked
            initializer.init(this, ctx, subj);
        }
    }

    public FactoryManager getFactoryManager() {
        return factoryManager;
    }

    public InitializerManager getInitializerManager() {
        return initializerManager;
    }

    public ConcurrentMap<Context, Semaphore> getInitLocks() {
        return initLocks;
    }

    public void waitInit(long timeout) throws InterruptedException {
        for (Map.Entry<Context, Semaphore> entry : initLocks.entrySet()) {
            final Context context = entry.getKey();
            final Semaphore semaphore = entry.getValue();
            log.info("Wait for initialization of " + context);
            if (!semaphore.tryAcquire(timeout, TimeUnit.MILLISECONDS)) {
                log.warn("Wait for {}  timeout!", context);
            }
        }
    }
}
