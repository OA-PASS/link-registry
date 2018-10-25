/*
 * Copyright 2018 Johns Hopkins University
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dataconservancy.pass.registry.link;

import static org.dataconservancy.pass.registry.link.DDL.LINK_REGISTRY_TABLE;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.zaxxer.hikari.HikariDataSource;

/**
 * @author apb@jhu.edu
 */
@RunWith(MockitoJUnitRunner.class)
public class DDLTest {

    static final String JDBC_IN_MEMORY = "jdbc:sqlite::memory:";

    @Mock
    DataSource dataSource;

    @Test
    public void validDDLTest() throws Exception {
        final HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(JDBC_IN_MEMORY);
        dataSource = ds;

        final DDL toTest = new DDL(dataSource);

        toTest.initDb(1);

        try (Connection c = dataSource.getConnection()) {
            try (Statement s = c.createStatement()) {
                s.executeQuery("SELECT * from " + LINK_REGISTRY_TABLE);

                try {
                    s.executeQuery("SELECT * from DOES_NOT_EXIST");
                    fail("Should have thrown an exception for missing table");
                } catch (final SQLException e) {
                    // expected
                }
            }
        }
    }

    @Test
    public void multipleDDLTest() throws Exception {
        final HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(JDBC_IN_MEMORY);
        dataSource = ds;

        final DDL toTest = new DDL(dataSource);

        toTest.initDb(1);
        toTest.initDb();
    }

    @Test
    public void retryTest() throws Exception {
        when(dataSource.getConnection()).thenThrow(SQLException.class);

        final DDL toTest = new DDL(dataSource);

        final int tries = 2;

        toTest.initDb(2);

        verify(dataSource, times(tries)).getConnection();

    }

}
