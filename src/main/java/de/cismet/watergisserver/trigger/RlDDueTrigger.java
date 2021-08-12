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
public class RlDDueTrigger extends AbstractDBAwareCidsTrigger {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(RlDDueTrigger.class);
    private static final String FG_BA_RL_CLASS_NAME = "de.cismet.cids.dynamics.dlm25w.fg_ba_rl";
    private static final String FG_BA_RL_TABLE_NAME = "duv.fg_ba_rl";
    private static final String FG_BA_D_CLASS_NAME = "de.cismet.cids.dynamics.dlm25w.fg_ba_d";
    private static final String FG_BA_D_TABLE_NAME = "duv.fg_ba_d";
    private static final String FG_BA_DUE_CLASS_NAME = "de.cismet.cids.dynamics.dlm25w.fg_ba_due";
    private static final String FG_BA_DUE_TABLE_NAME = "duv.fg_ba_due";

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
        return CidsTriggerKey.FORALL;
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
    private boolean isDuevObject(final CidsBean cidsBean) {
        return (cidsBean.getClass().getName().equals(FG_BA_D_CLASS_NAME)
                        || cidsBean.getClass().getName().equals(FG_BA_DUE_CLASS_NAME)
                        || cidsBean.getClass().getName().equals(FG_BA_RL_CLASS_NAME));
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
        if (isDuevObject(cidsBean)) {
            final Thread t = new Thread("recreate duv") {

                    @Override
                    public void run() {
                        final Connection con = null;
                        try {
                            final long start = System.currentTimeMillis();
                            // If the cidsBean is a new object, the meta object contains the new id while the cidsBean
                            // has still the id -1
                            final Object id = cidsBean.getProperty("ba_st.von.route.id");

                            if (id != null) {
                                final DbUpdater updater = new DbUpdater(getDbServer().getConnectionPool());
                                updater.addUpdate("select duv.recreate_fg_ba_duvByFg(" + id + ")");
                                updater.execute();
                                log.error("time to update duv by FG " + (System.currentTimeMillis() - start));
                            }
                        } catch (Exception e) {
                            log.error("Error while executing rl/d/due trigger.", e);
                        } finally {
                            if (con != null) {
                                getDbServer().getConnectionPool().releaseDbConnection(con);
                            }
                        }
                    }
                };

            t.start();
        }
    }
}
