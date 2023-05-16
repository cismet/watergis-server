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
public class EzgDetailTrigger extends AbstractDBAwareCidsTrigger {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(
            EzgDetailTrigger.class);
    private static final String EZG_DETAIL_CLASS_NAME = "de.cismet.cids.dynamics.dlm25w.ezg_detail";
    private static final String EZG_DETAIL_NAME = "dlm25w.ezg_detail";
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
        return new CidsTriggerKey(CidsTriggerKey.ALL, EZG_DETAIL_NAME);
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
    private boolean isEzgMvWbvJahrObject(final CidsBean cidsBean) {
        return (cidsBean.getClass().getName().equals(EZG_DETAIL_CLASS_NAME));
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
        if (isEzgMvWbvJahrObject(cidsBean)) {
            if (SINGLE_THREAD_EXECUTOR.getPoolSize() < 2) {
                final Runnable r = new Runnable() {

                        @Override
                        public void run() {
                            Connection con = null;

                            try {
                                con = getDbServer().getConnectionPool().getConnection(true);
                                final Statement s = con.createStatement();
                                s.execute("select dlm25w.import_ezg_mv_rbd()");
                                s.execute("select dlm25w.import_ezg_mv_wa()");
                                s.execute("select dlm25w.import_ezg_mv_planu()");
                                s.execute("select dlm25w.import_ezg_mv_detail()");
                                s.execute("select dlm25w.import_kg_zonen_f()");
                                s.execute("select dlm25w.import_kg_zonen_l()");
                            } catch (Exception e) {
                                LOG.error(
                                    "Error while executing async ezgMvWbvJahr trigger.",
                                    e);
                            } finally {
                                if (con != null) {
                                    getDbServer().getConnectionPool().releaseDbConnection(con);
                                }
                            }
                        }
                    };

                SINGLE_THREAD_EXECUTOR.execute(r);
            }
        }
    }
}
