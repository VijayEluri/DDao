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

package com.sf.ddao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by: Pavel Syrtsov
 * Date: Apr 14, 2007
 * Time: 6:01:46 PM
 * we could use design with bean utils Converter,
 * but then we would.n be able to delegate choosing of right
 * getXXX on result set object.
 */
public interface ColumnMapper {
    Object get(ResultSet rs, int idx) throws SQLException;
}