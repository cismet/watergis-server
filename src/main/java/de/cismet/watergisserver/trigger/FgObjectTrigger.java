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
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
public class FgObjectTrigger extends AbstractDBAwareCidsTrigger {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(
            FgObjectTrigger.class);

    public static final int ID_TO_AVOID_CHECK = -20;

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

    @Override
    public void afterCommittedInsert(final CidsBean cidsBean, final User user) {
        Connection con = null;
        if (cidsBean.getMetaObject().getID() != ID_TO_AVOID_CHECK) {
            try {
                final long start = System.currentTimeMillis();
                con = getDbServer().getConnectionPool().getLongTermConnection();

                final PreparedStatement psExists = con.prepareStatement(
                        "select true from cs_class_attr where class_id = ? and attr_key = 'cidsLayer'");
                psExists.setInt(1, cidsBean.getMetaObject().getClassID());

                final ResultSet set = psExists.executeQuery();

                if (set.next()) {
                    final PreparedStatement ps = con.prepareStatement(
                            "insert into created_object(username, class_id, object_id, creation_time) values (?, ?, ?, now())");
                    ps.setString(1, user.getDomain() + "." + user.getName());
                    ps.setInt(2, cidsBean.getMetaObject().getClassID());
                    ps.setInt(3, cidsBean.getMetaObject().getID());
                    ps.execute();
                    ps.close();
                }

                set.close();
                psExists.close();
                log.error("time to update created objects table" + (System.currentTimeMillis() - start));
            } catch (Exception e) {
                log.error("Error while executing fg_object trigger.", e);
            } finally {
                if (con != null) {
                    getDbServer().getConnectionPool().releaseDbConnection(con);
                }
            }
        }
    }

    @Override
    public void afterCommittedUpdate(final CidsBean cidsBean, final User user) {
    }

    @Override
    public void afterCommittedDelete(final CidsBean cidsBean, final User user) {
    }
}
