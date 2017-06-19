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
public class FgBakTrigger extends AbstractDBAwareCidsTrigger {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(
            FgBakTrigger.class);
    private static final String FG_BAK_CLASS_NAME = "de.cismet.cids.dynamics.dlm25w.fg_bak";
    private static final String FG_BAK_TABLE_NAME = "dlm25w.fg_bak";

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
        return new CidsTriggerKey(CidsTriggerKey.ALL, FG_BAK_TABLE_NAME);
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
            try {
                final long start = System.currentTimeMillis();
                // If the cidsBean is a new object, the meta object contains the new id while the cidsBean has still
                // the id -1
                final Object id = cidsBean.getMetaObject().getID();
                final Statement s = getDbServer().getActiveDBConnection().getConnection().createStatement();
                // refresh the stations on fg_bak
                s.execute("select dlm25w.replace_fg_bak(" + id.toString() + ")");
                // refresh fg_ba
                s.execute("select dlm25w.import_fg_ba(" + id.toString() + ", '" + user.getName() + "')");
                // refresh the stations on fg_ba
                s.execute("select dlm25w.replace_fg_ba_by_fg_bak(" + id.toString() + ")");
                // refresh fg_lak
                s.execute("select dlm25w.import_fg_lak_by_fg_bak(" + id.toString() + ", '" + user.getName() + "')");
                // refresh the stations on fg_lak
                s.execute("select dlm25w.replace_fg_lak_by_fg_bak(" + id.toString() + ")");
                // refresh fg_la
                s.execute("select dlm25w.import_fg_la_by_fg_bak(" + id.toString() + ", '" + user.getName() + "')");
                // refresh stat layer
                s.execute("select dlm25w.add_fg_ba_stat(" + id.toString() + ")");
                s.execute("select dlm25w.add_fg_la_stat(" + id.toString() + ")");
                // refresh presentation layer
                s.execute("select dlm25w.import_fg_bak_pr_pf(" + id.toString() + ", '" + user.getName() + "')");
                s.execute("select dlm25w.import_fg_ba_pr_pfByBakId(" + id.toString() + ", '" + user.getName() + "')");
                s.execute("select dlm25w.import_fg_lak_pr_pf_ByFgBak(" + id.toString() + ", '" + user.getName()
                            + "')");
                s.execute("select dlm25w.import_fg_la_pr_pfByFgBak(" + id.toString() + ", '" + user.getName()
                            + "')");

                s.execute("select dlm25w.import_fg_ba_gmdByFgBak(" + id.toString() + ", '" + user.getName() + "')");
                s.execute("select dlm25w.import_fg_ba_gbByFgBak(" + id.toString() + ", '" + user.getName() + "')");
                s.execute("select dlm25w.import_qp_gaf_pByFgBak(" + id.toString() + ", '" + user.getName() + "')");
                s.execute("select dlm25w.import_qp_gaf_lbyfgbak(" + id.toString() + ", '" + user.getName() + "')");
                s.execute("select dlm25w.import_qp_gaf_l_pr_pfByBak(" + id.toString() + ", '" + user.getName()
                            + "')");
                s.execute("select dlm25w.import_fg_ba_pr_ablByBak(" + id.toString() + ")");
                s.execute("select dlm25w.import_fg_ba_pr_abpByBak(" + id.toString() + ")");
//                s.execute("select dlm25w.add_fg_ba_gerogByBak(" + id.toString() + ")");
//                s.execute("select dlm25w.add_fg_ba_gerogaByBak(" + id.toString() + ")");
//                s.execute("select dlm25w.add_fg_ba_geroga_rsByBak(" + id.toString() + ")");
                log.error("time to update stations " + (System.currentTimeMillis() - start));
            } catch (Exception e) {
                log.error("Error while executing fgBak trigger.", e);
            }
        }
    }
}
