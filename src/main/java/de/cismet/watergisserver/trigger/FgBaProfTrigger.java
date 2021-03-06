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
public class FgBaProfTrigger extends AbstractDBAwareCidsTrigger {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(
            FgBaProfTrigger.class);
    private static final String FG_BAK_CLASS_NAME = "de.cismet.cids.dynamics.dlm25w.fg_ba_prof";
    private static final String FG_BAK_TABLE_NAME = "dlm25w.fg_ba_prof";

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
            Connection con = null;
            try {
                final long start = System.currentTimeMillis();
                // If the cidsBean is a new object, the meta object contains the new id while the cidsBean has still
                // the id -1
                final Object id = cidsBean.getProperty("ba_st.von.route.bak_id");
                con = getDbServer().getConnectionPool().getConnection(true);
                final Statement s = con.createStatement();
                s.execute("select dlm25w.import_fg_ba_geroByBak(" + id.toString() + ")");
                s.execute("select dlm25w.import_fg_ba_gerogByBak(" + id.toString() + ")");
                s.execute("select dlm25w.import_fg_ba_gerogaByBak(" + id.toString() + ")");
                s.execute("select dlm25w.import_fg_ba_gerog_rsByBak(" + id.toString() + ")");
//                s.execute("select dlm25w.import_abstand_3ByBak(" + id.toString() + ")");
//                s.execute("select dlm25w.import_abstand_5ByBak(" + id.toString() + ")");
//                s.execute("select dlm25w.import_abstand_10ByBak(" + id.toString() + ")");
//                s.execute("select dlm25w.import_abstand_20ByBak(" + id.toString() + ")");
//                s.execute("select dlm25w.import_abstand_30ByBak(" + id.toString() + ")");
//                s.execute("select dlm25w.import_feldbloeckeByBak(" + id.toString() + ")");
//                s.execute("select dlm25w.import_randstreifenByBak(" + id.toString() + ")");
//                s.execute("select dlm25w.import_sperrflaecheByBak(" + id.toString() + ")");
//                s.execute("select dlm25w.import_fg_ba_geroga_rsByBak(" + id.toString() + ")");
                log.error("time to update stations " + (System.currentTimeMillis() - start));
            } catch (Exception e) {
                log.error("Error while executing fgBaProf trigger.", e);
            } finally {
                if (con != null) {
                    getDbServer().getConnectionPool().releaseDbConnection(con);
                }
            }

            final Thread t = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            Connection con = null;

                            try {
                                con = getDbServer().getConnectionPool().getConnection(true);
                                final Statement s = con.createStatement();
                                s.execute(
                                    "select dlm25w.import_fg_ba_geroga_rsByBak("
                                            + cidsBean.getProperty("ba_st.von.route.bak_id")
                                            + ")");
                            } catch (Exception e) {
                                log.error(
                                    "Error while executing async fgBak trigger."
                                            + String.valueOf(cidsBean.getProperty("ba_st.von.route.bak_id")),
                                    e);
                            } finally {
                                if (con != null) {
                                    getDbServer().getConnectionPool().releaseDbConnection(con);
                                }
                            }
                        }
                    });

            t.start();
        }
    }
}
