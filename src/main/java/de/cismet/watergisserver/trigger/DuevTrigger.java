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
public class DuevTrigger extends AbstractDBAwareCidsTrigger {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DuevTrigger.class);
    private static final String FG_BA_DUV_CLASS_NAME = "de.cismet.cids.dynamics.duv.fg_ba_duv";
    private static final String FG_BA_DUV_TABLE_NAME = "duv.fg_ba_duv";

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
        return new CidsTriggerKey(CidsTriggerKey.ALL, FG_BA_DUV_TABLE_NAME);
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
        return (cidsBean.getClass().getName().equals(FG_BA_DUV_CLASS_NAME));
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
            final Connection con = null;
            try {
                final long start = System.currentTimeMillis();
                // If the cidsBean is a new object, the meta object contains the new id while the cidsBean has still
                // the id -1
                final Object id = cidsBean.getProperty("id");

                if (id != null) {
                    final Thread t = new Thread(new Runnable() {

                                @Override
                                public void run() {
                                    Connection con = null;

                                    try {
                                        con = getDbServer().getConnectionPool().getConnection(true);
                                        final Statement s = con.createStatement();
                                        s.execute("select duv.recreate_duv('" + id + "')");
                                    } catch (Exception e) {
                                        log.error(
                                            "Error while executing async duew trigger."
                                                    + String.valueOf(cidsBean.getMetaObject().getID()),
                                            e);
                                    } finally {
                                        if (con != null) {
                                            getDbServer().getConnectionPool().releaseDbConnection(con);
                                        }
                                    }
                                }
                            });

                    t.start();

                    log.error("time to update duv " + (System.currentTimeMillis() - start));
                }
            } catch (Exception e) {
                log.error("Error while executing lfk trigger.", e);
            } finally {
                if (con != null) {
                    getDbServer().getConnectionPool().releaseDbConnection(con);
                }
            }
        }
    }
}
