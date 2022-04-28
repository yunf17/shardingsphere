/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.readwritesplitting.type.impl;

import lombok.RequiredArgsConstructor;
import org.apache.shardingsphere.readwritesplitting.type.ReadwriteSplittingDataSourceProcessor;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Static readwrite splitting data source processor.
 */
@RequiredArgsConstructor
public final class StaticReadwriteSplittingDataSourceProcessor implements ReadwriteSplittingDataSourceProcessor {
    
    private final String writeDataSourceName;
    
    private final List<String> readDataSourceNames;
    
    @Override
    public String getWriteDataSource() {
        return writeDataSourceName;
    }
    
    @Override
    public List<String> getReadDataSources() {
        return readDataSourceNames;
    }
    
    @Override
    public Map<String, Collection<String>> getDataSourceMapper(final String name) {
        Map<String, Collection<String>> result = new HashMap<>(1, 1);
        Collection<String> actualDataSourceNames = new LinkedList<>();
        actualDataSourceNames.add(writeDataSourceName);
        actualDataSourceNames.addAll(readDataSourceNames);
        result.put(name, actualDataSourceNames);
        return result;
    }
}