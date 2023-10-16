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
public class FgLakAeTrigger extends AbstractDBAwareCidsTrigger {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(
            FgLakAeTrigger.class);
    private static final String FG_BAK_CLASS_NAME = "de.cismet.cids.dynamics.dlm25w.fg_lak_ae";
    private static final String FG_BAK_GWK_TABLE_NAME = "dlm25w.fg_lak_ae";

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
        return new CidsTriggerKey(CidsTriggerKey.ALL, FG_BAK_GWK_TABLE_NAME);
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
        return (cidsBean.getClass().getName().equals(FG_BAK_CLASS_NAME));
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
            Connection con = null;
            try {
                final long start = System.currentTimeMillis();
                final Object id = cidsBean.getProperty("lak_st.von.route.id");
                final Object la_cd = cidsBean.getProperty("lak_st.von.route.la_cd.la_cd");
                if (id != null) {
                    con = getDbServer().getConnectionPool().getLongTermConnection();
                    final Statement s = con.createStatement();
                    // refresh fg_la
                    s.execute("select dlm25w.import_fg_la(" + la_cd.toString() + ", '" + user.getName() + "')");
                    s.execute("select dlm25w.replace_fg_la(id) from dlm25w.fg_la where la_cd = " + la_cd.toString());
                    // refresh la stat layer
                    s.execute("select dlm25w.add_fg_la_stat_by_lak(" + id.toString() + ")");
                    s.execute("select dlm25w.add_fg_la_stat_10_by_lak(" + id.toString() + ")");
                    s.execute("select dlm25w.import_fg_la_pr_pf(l.id, '" + user.getName()
                                + "') from dlm25w.fg_la l join dlm25w.k_gwk_lawa k on (k.id = l.la_cd) where k.la_cd = "
                                + la_cd.toString());
                    // refresh gmd s.execute("select dlm25w.import_fg_ba_gmdByFgBak(" + id.toString() + ", '" +
                    // user.getName() + "')"); s.execute("select dlm25w.import_fg_ba_gbByFgBak(" + id.toString() + ", '"
                    // + user.getName() + "')");
                    log.error("time to update stations " + (System.currentTimeMillis() - start));
                }
            } catch (Exception e) {
                log.error("Error while executing fgLakAe trigger.", e);
            } finally {
                if (con != null) {
                    getDbServer().getConnectionPool().releaseDbConnection(con);
                }
            }
        }
    }
}
