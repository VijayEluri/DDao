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

package com.sf.ddao.cache;

import com.sf.ddao.alinker.factory.UseFactory;
import com.sf.ddao.cache.impl.CacheFactoryProxy;

import java.util.List;

/**
 * Created by: Pavel
 * Date: Jan 4, 2009
 * Time: 8:03:53 PM
 */

@UseFactory(CacheFactoryProxy.class)
public interface Cache<K, V> {
    V get(K key);

    boolean set(K key, V value) throws CacheException;

    boolean add(K key, V value);

    boolean replace(K key, V value);

    boolean delete(K key);

    List<V> getMulti(List<K> keyList);

    long incr(K key, int delta);

    void removeAll();
}