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

package org.apache.shardingsphere.test.integration.container.atomic.storage.impl;

import org.apache.shardingsphere.infra.database.type.DatabaseTypeFactory;
import org.apache.shardingsphere.test.integration.container.atomic.storage.DockerStorageContainer;
import org.apache.shardingsphere.test.integration.env.container.wait.JDBCConnectionWaitStrategy;
import org.apache.shardingsphere.test.integration.env.runtime.DataSourceEnvironment;

import java.sql.DriverManager;

/**
 * PostgreSQL container.
 */
public final class PostgreSQLContainer extends DockerStorageContainer {
    
    public PostgreSQLContainer(final String scenario) {
        super(DatabaseTypeFactory.getInstance("PostgreSQL"), "postgres:12-alpine", scenario);
    }
    
    @Override
    protected void configure() {
        withCommand("--max_connections=600", "--wal_level=logical");
        addEnv("POSTGRES_USER", getUsername());
        addEnv("POSTGRES_PASSWORD", getPassword());
        withExposedPorts(getPort());
        super.configure();
        setWaitStrategy(new JDBCConnectionWaitStrategy(
                () -> DriverManager.getConnection(DataSourceEnvironment.getURL(getDatabaseType(), "localhost", getFirstMappedPort(), "postgres"), getUsername(), getPassword())));
    }
    
    @Override
    public String getJdbcUrl(final String databaseName) {
        return DataSourceEnvironment.getURL(getDatabaseType(), getHost(), getFirstMappedPort(), databaseName);
    }
    
    @Override
    public String getUsername() {
        return "root";
    }
    
    @Override
    public String getPassword() {
        return "root";
    }
    
    @Override
    public int getPort() {
        return 5432;
    }
}