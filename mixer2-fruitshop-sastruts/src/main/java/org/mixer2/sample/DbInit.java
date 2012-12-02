package org.mixer2.sample;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.SqlFileProcedureCall;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;

public class DbInit {

    Logger logger = Logger.getLogger(DbInit.class);

    private static final String initSql = "sql/dbinit.sql";

    /**
     * database initialize.
     * @throws SQLException
     * @throws IOException
     */
    public DbInit() throws SQLException, IOException {

        logger.info("########## DBInit !##########");

        // get JdbcManager object.
        // Don't use auto DI'ed object because the web application has not
        // started yet at this step.
        S2Container container = SingletonS2ContainerFactory.getContainer();
        JdbcManager jdbcManager = (JdbcManager) container
                .getComponent("jdbcManager");

        // execute sql
        SqlFileProcedureCall call = jdbcManager.callBySqlFile(initSql);
        call.execute();
    }

}
