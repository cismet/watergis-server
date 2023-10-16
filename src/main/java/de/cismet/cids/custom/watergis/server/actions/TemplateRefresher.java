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
package de.cismet.cids.custom.watergis.server.actions;

import Sirius.server.sql.DBConnectionPool;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class TemplateRefresher {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(RefreshTemplateAction.class);

    //~ Instance fields --------------------------------------------------------

    private ThreadPoolExecutor drainBasinExecuter = null;
    private ThreadPoolExecutor rwseggeomExecuter = null;
    private ThreadPoolExecutor ezgKrlExecuter = null;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new TemplateRefresher object.
     */
    private TemplateRefresher() {
        drainBasinExecuter = new ThreadPoolExecutor(
                1,
                1,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
        rwseggeomExecuter = new ThreadPoolExecutor(
                1,
                1,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
        ezgKrlExecuter = new ThreadPoolExecutor(
                1,
                1,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static TemplateRefresher getInstance() {
        return LazyInitialiser.INSTANCE;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  dbPool  DOCUMENT ME!
     */
    public void refreshDrainBasin(final DBConnectionPool dbPool) {
        if (drainBasinExecuter.getQueue().size() < 2) {
            drainBasinExecuter.submit(new Runnable() {

                    @Override
                    public void run() {
                        Connection con = null;
                        try {
                            try {
                                con = dbPool.getLongTermConnection();
                                final Statement statement = con.createStatement();
                                statement.execute("select dlm25w.refreshDrainBasin()");
                                statement.close();
                            } catch (SQLException ex) {
                                LOG.error("Cannot refresh drainBasin", ex);
                            }
                        } finally {
                            if ((dbPool != null) && (con != null)) {
                                dbPool.releaseDbConnection(con);
                            }
                        }
                    }
                });
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  dbPool  DOCUMENT ME!
     */
    public void refreshRwSegGm(final DBConnectionPool dbPool) {
        if (rwseggeomExecuter.getQueue().size() < 2) {
            rwseggeomExecuter.submit(new Runnable() {

                    @Override
                    public void run() {
                        Connection con = null;
                        try {
                            try {
                                con = dbPool.getLongTermConnection();
                                final Statement statement = con.createStatement();
                                statement.execute("select dlm25w.refreshRwSegGeom()");
                                statement.close();
                            } catch (SQLException ex) {
                                LOG.error("Cannot refresh RwSegGeom", ex);
                            }
                        } finally {
                            if ((dbPool != null) && (con != null)) {
                                dbPool.releaseDbConnection(con);
                            }
                        }
                    }
                });
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  dbPool  DOCUMENT ME!
     */
    public void refreshEzgKrl(final DBConnectionPool dbPool) {
        if (ezgKrlExecuter.getQueue().size() < 2) {
            ezgKrlExecuter.submit(new Runnable() {

                    @Override
                    public void run() {
                        Connection con = null;
                        try {
                            try {
                                con = dbPool.getLongTermConnection();
                                final Statement statement = con.createStatement();
                                statement.execute("select dlm25w.refreshSEzgKRl()");
                                statement.close();
                            } catch (SQLException ex) {
                                LOG.error("Cannot refresh ezgKrl", ex);
                            }
                        } finally {
                            if ((dbPool != null) && (con != null)) {
                                dbPool.releaseDbConnection(con);
                            }
                        }
                    }
                });
        }
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private static final class LazyInitialiser {

        //~ Static fields/initializers -----------------------------------------

        private static final TemplateRefresher INSTANCE = new TemplateRefresher();

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new LazyInitialiser object.
         */
        private LazyInitialiser() {
        }
    }
}
