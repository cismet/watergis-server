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
package de.cismet.cids.custom.helper;

import Sirius.server.sql.DBConnectionPool;

import org.openide.util.Exceptions;

import java.sql.Connection;
import java.sql.Statement;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class DuvRefresher {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DuvRefresher.class);

    //~ Instance fields --------------------------------------------------------

    private DBConnectionPool connectionPool = null;
    private final LinkedBlockingQueue<Integer> jobQueue = new LinkedBlockingQueue<>();

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DuvRefresher object.
     *
     * @param  connectionPool  DOCUMENT ME!
     */
    private DuvRefresher(final DBConnectionPool connectionPool) {
        this.connectionPool = connectionPool;

        start();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  fgId  DOCUMENT ME!
     */
    public void addFgToRefresh(final int fgId) {
        if (!jobQueue.contains(fgId)) {
            jobQueue.add(fgId);

            LOG.info("id " + fgId + " added to the duv refresh queue");
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void start() {
        final Thread t = new Thread("recreate duv") {

                @Override
                public void run() {
                    while (true) {
                        Integer nextId = null;

                        try {
                            nextId = jobQueue.take();
                        } catch (InterruptedException ex) {
                            // nothing to do
                        }

                        if (nextId != null) {
                            Connection con = null;

                            try {
                                final long start = System.currentTimeMillis();

                                con = connectionPool.getLongTermConnection();
                                final Statement s = con.createStatement();
                                con.createStatement().execute("select duv.recreate_fg_ba_duvByFg(" + nextId + ")");

                                LOG.error("time to update duv by FG " + (System.currentTimeMillis() - start));
                            } catch (Exception e) {
                                LOG.error("Error while update duv by fg.", e);
                            } finally {
                                if (con != null) {
                                    connectionPool.releaseDbConnection(con);
                                }
                            }
                        }
                    }
                }
            };

        t.setDaemon(true);
        t.start();
    }

    /**
     * DOCUMENT ME!
     *
     * @param   connectionPool  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static DuvRefresher getInstance(final DBConnectionPool connectionPool) {
        return LazyInitialiser.getDuvRefresher(connectionPool);
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private static final class LazyInitialiser {

        //~ Static fields/initializers -----------------------------------------

        private static DuvRefresher INSTANCE = null;

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @param   connectionPool  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        private static synchronized DuvRefresher getDuvRefresher(final DBConnectionPool connectionPool) {
            if (INSTANCE == null) {
                INSTANCE = new DuvRefresher(connectionPool);
            }

            return INSTANCE;
        }
    }
}
