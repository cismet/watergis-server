/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.watergisserver.trigger;

import Sirius.server.sql.DBConnectionPool;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import de.cismet.commons.concurrency.CismetExecutors;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class DbUpdater {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(DbUpdater.class);

    //~ Instance fields --------------------------------------------------------

    private final List<String> updates = new ArrayList<String>();
    private final DBConnectionPool connectionPool;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DbUpdater object.
     *
     * @param  connectionPool  DOCUMENT ME!
     */
    public DbUpdater(final DBConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  cmd  DOCUMENT ME!
     */
    public void addUpdate(final String cmd) {
        updates.add(cmd);
    }

    /**
     * DOCUMENT ME!
     */
    public void execute() {
        final ExecutorService executor = CismetExecutors.newFixedThreadPool(updates.size());

        for (final String command : updates) {
            executor.execute(new DBExecutor(command));
        }

        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException ex) {
            // notinh to do
        }

        updates.clear();
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class DBExecutor implements Runnable {

        //~ Instance fields ----------------------------------------------------

        private final String command;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new DBExecutor object.
         *
         * @param  command  DOCUMENT ME!
         */
        public DBExecutor(final String command) {
            this.command = command;
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public void run() {
            Connection con = null;

            try {
                con = connectionPool.getLongTermConnection();
                final Statement s = con.createStatement();
                s.execute(command);
            } catch (SQLException ex) {
                LOG.error("Error while executing the following sql command: " + command, ex);
            } finally {
                if (con != null) {
                    connectionPool.releaseDbConnection(con);
                }
            }
        }
    }
}
