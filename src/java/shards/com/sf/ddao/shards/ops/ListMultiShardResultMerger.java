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

package com.sf.ddao.shards.ops;

import com.sf.ddao.shards.MultiShardResultMerger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by psyrtsov
 */
public class ListMultiShardResultMerger implements MultiShardResultMerger<List<Object>> {
    public List<Object> reduce(List<List<Object>> resultList) {
        List<Object> res = new ArrayList<Object>();
        for (List<Object> list : resultList) {
            res.addAll(list);
        }
        return res;
    }
}
