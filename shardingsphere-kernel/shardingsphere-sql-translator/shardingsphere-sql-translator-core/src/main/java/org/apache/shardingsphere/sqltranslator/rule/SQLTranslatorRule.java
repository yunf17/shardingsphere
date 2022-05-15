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

package org.apache.shardingsphere.sqltranslator.rule;

import org.apache.shardingsphere.infra.database.type.DatabaseType;
import org.apache.shardingsphere.infra.exception.ShardingSphereException;
import org.apache.shardingsphere.infra.rule.identifier.scope.GlobalRule;
import org.apache.shardingsphere.sql.parser.sql.common.statement.SQLStatement;
import org.apache.shardingsphere.sqltranslator.api.config.SQLTranslatorRuleConfiguration;
import org.apache.shardingsphere.sqltranslator.exception.SQLTranslationException;
import org.apache.shardingsphere.sqltranslator.spi.SQLTranslator;
import org.apache.shardingsphere.sqltranslator.factory.SQLTranslatorFactory;

/**
 * SQL translator rule.
 */
public final class SQLTranslatorRule implements GlobalRule {
    
    private final SQLTranslator translator;
    
    private final boolean useOriginalSQLWhenTranslatingFailed;
    
    public SQLTranslatorRule(final SQLTranslatorRuleConfiguration ruleConfig) {
        translator = SQLTranslatorFactory.getInstance(ruleConfig.getType());
        useOriginalSQLWhenTranslatingFailed = ruleConfig.isUseOriginalSQLWhenTranslatingFailed();
    }
    
    /**
     * Translate SQL.
     * 
     * @param sql to be translated SQL
     * @param sqlStatement to be translated SQL statement
     * @param frontendDatabaseType frontend database type
     * @param backendDatabaseType backend database type
     * @return translated SQL
     */
    public String translate(final String sql, final SQLStatement sqlStatement, final DatabaseType frontendDatabaseType, final DatabaseType backendDatabaseType) {
        if (frontendDatabaseType.equals(backendDatabaseType)) {
            return sql;
        }
        try {
            return translator.translate(sql, sqlStatement, frontendDatabaseType, backendDatabaseType);
        } catch (final SQLTranslationException ex) {
            if (useOriginalSQLWhenTranslatingFailed) {
                return sql;
            }
            throw new ShardingSphereException(ex);
        }
    }
    
    @Override
    public String getType() {
        return SQLTranslatorRule.class.getSimpleName();
    }
}