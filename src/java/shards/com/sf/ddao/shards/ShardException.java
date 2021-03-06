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

package com.sf.ddao.shards;

/**
 * todo: add class comments
 * Created-By: Pavel Syrtsov
 * Date: Jun 22, 2008
 * Time: 9:31:38 AM
 */
public class ShardException extends RuntimeException {
    public ShardException() {
    }

    public ShardException(String message) {
        super(message);
    }

    public ShardException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShardException(Throwable cause) {
        super(cause);
    }
}
