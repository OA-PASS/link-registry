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

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author apb@jhu.edu
 */
public class DDL {

    /** Name of the link registry table */
    public static final String LINK_REGISTRY_TABLE = "link_registry";

    static final Logger LOG = LoggerFactory.getLogger(DDL.class);

    private final DataSource db;

    private static final String DDL = "/link-registry.sql";

    DDL(DataSource db) {
        this.db = db;
    }

    void initDb() {
        initDb(Integer.MAX_VALUE);
    }

    void initDb(int tries) {
        for (int attempt = 0; attempt < tries; attempt++) {
            try {
                doDDL();
                break;
            } catch (final SQLException e) {
                try {
                    Thread.sleep(1000);
                } catch (final InterruptedException i) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Interrupted", i);
                }
            }
        }
    }

    int doDDL() throws SQLException {
        try (Connection conn = db.getConnection()) {
            try (Statement statement = conn.createStatement()) {
                try {
                    LOG.info("Checking database");
                    final int created = statement.executeUpdate(
                            IOUtils.toString(this.getClass().getResourceAsStream(DDL), UTF_8));

                    LOG.info("Database is ready");

                    return created;
                } catch (final IOException e) {
                    throw new RuntimeException("Could not read DDL", e);
                }
            }
        }
    }
}
