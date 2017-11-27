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

import java.sql.Statement;

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
public class FgBaPrAbpTrigger extends AbstractDBAwareCidsTrigger {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(
            FgBaPrAbpTrigger.class);
    private static final String FG_BA_SCHA_CLASS_NAME = "de.cismet.cids.dynamics.dlm25w.fg_ba_scha";
    private static final String FG_BA_WEHR_CLASS_NAME = "de.cismet.cids.dynamics.dlm25w.fg_ba_wehr";
    private static final String FG_BA_SCHW_CLASS_NAME = "de.cismet.cids.dynamics.dlm25w.fg_ba_schw";
    private static final String FG_BA_KR_CLASS_NAME = "de.cismet.cids.dynamics.dlm25w.fg_ba_kr";
    private static final String FG_BA_ANLP_CLASS_NAME = "de.cismet.cids.dynamics.dlm25w.fg_ba_anlp";
    private static final String FG_BA_EA_CLASS_NAME = "de.cismet.cids.dynamics.dlm25w.fg_ba_ea";

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
        return new CidsTriggerKey(CidsTriggerKey.ALL, CidsTriggerKey.ALL);
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
    private boolean isRelevantObject(final CidsBean cidsBean) {
        return cidsBean.getClass().getName().equals(FG_BA_SCHA_CLASS_NAME)
                    || cidsBean.getClass().getName().equals(FG_BA_WEHR_CLASS_NAME)
                    || cidsBean.getClass().getName().equals(FG_BA_SCHW_CLASS_NAME)
                    || cidsBean.getClass().getName().equals(FG_BA_KR_CLASS_NAME)
                    || cidsBean.getClass().getName().equals(FG_BA_ANLP_CLASS_NAME)
                    || cidsBean.getClass().getName().equals(FG_BA_EA_CLASS_NAME);
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
        if (isRelevantObject(cidsBean)) {
            try {
                final long start = System.currentTimeMillis();
                final Object id = cidsBean.getProperty("ba_st.route.id");
                final Statement s = getDbServer().getConnectionPool().getConnection(true).createStatement();
                // refresh fg_ba_gmd layer
                if (id != null) {
                    s.execute("select dlm25w.import_fg_ba_pr_abp(" + id.toString() + ")");
                }
                log.error("time to update stations " + (System.currentTimeMillis() - start));
            } catch (Exception e) {
                log.error("Error while executing fgBak trigger.", e);
            }
        }
    }
}
