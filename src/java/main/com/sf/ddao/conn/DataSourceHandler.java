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

package com.sf.ddao.conn;

import com.sf.ddao.alinker.initializer.InitializerException;
import com.sf.ddao.handler.Intializible;
import com.sf.ddao.DataSourceDao;

import javax.sql.DataSource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This ConnectionHandler is unsing static hash map to get DataSource object,
 * can be helpful when we want flexibility of defining data source parameters from some config file
 * but don't want to deal with JNDI
 * 
 * Created-By: Pavel Syrtsov
 * Date: Apr 10, 2008
 * Time: 9:39:39 PM
 */
public class DataSourceHandler extends ConnectionHandlerHelper implements Intializible {
    public static final ConcurrentMap<String, DataSource> dataSourceMap = new ConcurrentHashMap<String, DataSource>();
    private String dsName;

    public Connection createConnection(Method method, Object[] args) throws SQLException {
        DataSource dataSource = dataSourceMap.get(dsName);
        if (dataSource == null) {
            throw new NullPointerException("DataSource with name " + dsName + " should be regstered at " + DataSourceHandler.class);
        }
        return dataSource.getConnection();
    }

    public void init(Class<?> iFace, Annotation annotation, List<Class<?>> iFaceList) throws InitializerException {
        DataSourceDao daoAnnotation = (DataSourceDao) annotation;
        dsName = daoAnnotation.value();
        super.init(iFace, annotation, iFaceList);
    }
}