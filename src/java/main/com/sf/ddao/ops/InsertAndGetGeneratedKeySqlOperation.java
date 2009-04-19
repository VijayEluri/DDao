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

package com.sf.ddao.ops;

import com.sf.ddao.alinker.initializer.InitializerException;
import com.sf.ddao.DaoException;
import com.sf.ddao.InsertAndGetGeneratedKey;
import com.sf.ddao.SqlOperation;
import com.sf.ddao.SqlAnnotation;
import com.sf.ddao.factory.StatementFactory;
import com.sf.ddao.factory.StatementFactoryException;
import com.sf.ddao.factory.StatementFactoryManager;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created-By: Pavel Syrtsov
 * Date: Aug 11, 2007
 * Time: 2:47:16 PM
 */
public class InsertAndGetGeneratedKeySqlOperation implements SqlOperation {
    private StatementFactory statementFactory;

    public Object invoke(Connection connection, Method method, Object[] args) {
        try {
            PreparedStatement preparedStatement = statementFactory.createStatement(connection, args);

            int res = preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                res = resultSet.getInt(1);
            }
            resultSet.close();
            preparedStatement.close();
            if (method.getReturnType() == Integer.TYPE || method.getReturnType() == Integer.class) {
                return res;
            }
            return null;
        } catch (Exception t) {
            throw new DaoException("Failed to execute sql operation for " + method, t);
        }
    }

    public void init(Method method, SqlAnnotation sqlAnnotation) throws InitializerException {
        InsertAndGetGeneratedKey insertAndGetGeneratedKey = (InsertAndGetGeneratedKey) sqlAnnotation;
        String sql = insertAndGetGeneratedKey.value();
        try {
            statementFactory = StatementFactoryManager.createStatementFactory(method, sql);
        } catch (StatementFactoryException e) {
            throw new InitializerException("Failed to setup sql operation " + sql + " for method " + method, e);
        }
    }
}

