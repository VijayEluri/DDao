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

package com.sf.ddao.factory;

import org.apache.commons.chain.Context;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by psyrtsov
 */
public interface BoundParameter {
    /**
     * bindParam parameter extracted from argument list to given prepared statement
     *
     * @param preparedStatement - prepared statement that has to be bound with parameter
     * @param idx               - index of parameter that should be bound,
     *                          should be used as second argument for PreparedStatement.setXXX
     * @param context           - method invocation argument list
     * @throws com.sf.ddao.factory.param.ParameterException
     *          - thrown when failed to bindParam parameter
     * @returns number of bound parameters
     */
    int bindParam(PreparedStatement preparedStatement, int idx, Context context) throws SQLException;
}
