/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.watergisserver.trigger;

import Sirius.server.newuser.User;

import org.openide.util.lookup.ServiceProvider;

import java.sql.Connection;
import java.sql.Statement;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.cids.trigger.AbstractDBAwareCidsTrigger;
import de.cismet.cids.trigger.CidsTrigger;
import de.cismet.cids.trigger.CidsTriggerKey;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
@ServiceProvider(service = CidsTrigger.class)
public class FgBaDTrigger extends AbstractDBAwareCidsTrigger {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(FgBaDTrigger.class);
    private static final String FG_BA_D_CLASS_NAME = "de.cismet.cids.dynamics.dlm25w.fg_ba_d";
    private static final String FG_BA_D_TABLE_NAME = "dlm25w.fg_ba_d";
    private static final ThreadPoolExecutor SINGLE_THREAD_EXECUTOR = new ThreadPoolExecutor(
            1,
            1,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());

    //~ Methods ----------------------------------------------------------------

    @Override
    public void afterDelete(final CidsBean cidsBean, final User user) {
    }

    @Override
    public void afterInsert(final CidsBean cidsBean, final User user) {
    }

    @Override
    public void afterUpdate(final CidsBean cidsBean, final User user) {
    }

    @Override
    public void beforeDelete(final CidsBean cidsBean, final User user) {
    }

    @Override
    public void beforeInsert(final CidsBean cidsBean, final User user) {
    }

    @Override
    public void beforeUpdate(final CidsBean cidsBean, final User user) {
    }

    @Override
    public CidsTriggerKey getTriggerKey() {
        return new CidsTriggerKey(CidsTriggerKey.ALL, FG_BA_D_TABLE_NAME);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   o  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int compareTo(final CidsTrigger o) {
        return 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   cidsBean  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private boolean isFgBakObject(final CidsBean cidsBean) {
        return (cidsBean.getClass().getName().equals(FG_BA_D_CLASS_NAME));
    }

    @Override
    public void afterCommittedInsert(final CidsBean cidsBean, final User user) {
        restat(cidsBean, user);
    }

    @Override
    public void afterCommittedUpdate(final CidsBean cidsBean, final User user) {
        restat(cidsBean, user);
    }

    @Override
    public void afterCommittedDelete(final CidsBean cidsBean, final User user) {
        restat(cidsBean, user);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  cidsBean  DOCUMENT ME!
     * @param  user      DOCUMENT ME!
     */
    private void restat(final CidsBean cidsBean, final User user) {
        if (isFgBakObject(cidsBean)) {
            if (SINGLE_THREAD_EXECUTOR.getPoolSize() < 2) {
                final Thread t = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                Connection con = null;

                                try {
                                    final long start = System.currentTimeMillis();
                                    con = getDbServer().getConnectionPool().getLongTermConnection();
                                    final Statement s = con.createStatement();

                                    s.execute("select dlm25w.update_haltethema()");

                                    LOG.error(
                                        "time to update async for fg_ba_d "
                                                + String.valueOf(cidsBean.getMetaObject().getID())
                                                + " :"
                                                + (System.currentTimeMillis() - start));
                                } catch (Exception e) {
                                    LOG.error(
                                        "Error while executing async fg_ba_d trigger."
                                                + String.valueOf(cidsBean.getMetaObject().getID()),
                                        e);
                                } finally {
                                    if (con != null) {
                                        getDbServer().getConnectionPool().releaseDbConnection(con);
                                    }
                                }
                            }
                        });

                SINGLE_THREAD_EXECUTOR.execute(t);
            }
        }
    }
}
