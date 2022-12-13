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

import Sirius.server.middleware.impls.domainserver.DomainServerImpl;
import Sirius.server.middleware.interfaces.domainserver.MetaService;
import Sirius.server.middleware.interfaces.domainserver.MetaServiceStore;
import Sirius.server.newuser.User;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import de.cismet.cids.custom.helper.ActionHelper;

import de.cismet.cids.server.actions.ServerAction;
import de.cismet.cids.server.actions.ServerActionParameter;
import de.cismet.cids.server.actions.UserAwareServerAction;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
@org.openide.util.lookup.ServiceProvider(service = ServerAction.class)
public class AddProfileAction implements ServerAction, MetaServiceStore, UserAwareServerAction {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(AddProfileAction.class);
    // The usage of prepared statements are not possible for create role
    private static final String INSERT_PKT =
        "INSERT INTO dlm25w.qp_pkte (qp_upl,qp_npl,gaf_id,stat,y,z,kz,rk,bk,hw,rw,hyk,fis_g_user, fis_g_date, geom) values (?,?,?,?,?,?,?,?,?,?,?,?,?, now(), st_point(?,?))";

    public static final String TASK_NAME = "addProfileAction";

    //~ Enums ------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public enum ParameterType {

        //~ Enum constants -----------------------------------------------------

        DATA
    }

    //~ Instance fields --------------------------------------------------------

    private User user;

    private MetaService metaService;

    //~ Methods ----------------------------------------------------------------

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void setUser(final User user) {
        this.user = user;
    }

    @Override
    public void setMetaService(final MetaService ms) {
        this.metaService = ms;
    }

    @Override
    public MetaService getMetaService() {
        return this.metaService;
    }

    @Override
    public Object execute(final Object body, final ServerActionParameter... params) {
        DomainServerImpl domainServer = null;
        Connection con = null;
        final PreparedStatement psCreate = null;

        try {
            Object[][] data = null;

            for (final ServerActionParameter param : params) {
                if (AddProfileAction.ParameterType.DATA.toString().equals(
                                param.getKey())) {
                    data = (Object[][])param.getValue();
                }
            }

            domainServer = (DomainServerImpl)metaService;
            con = domainServer.getConnectionPool().getConnection(true);

            final Statement st = con.createStatement();
            final PreparedStatement pst = con.prepareStatement(INSERT_PKT);

            if (data != null) {
                for (final Object[] o : data) {
                    final Double rk = (Double)o[7];
                    final Double bk = (Double)o[8];

                    pst.setObject(1, (Integer)o[0]);
                    pst.setObject(2, (Integer)o[1]);
                    pst.setString(3, (String)o[2]);
                    pst.setObject(4, (Double)o[3]);
                    pst.setObject(5, (Double)o[4]);
                    pst.setObject(6, (Double)o[5]);
                    pst.setString(7, (String)o[6]);
                    pst.setObject(8, ((rk != null) ? rk.intValue() : null));
                    pst.setObject(9, ((bk != null) ? bk.intValue() : null));
                    pst.setObject(10, (Double)o[9]);
                    pst.setObject(11, (Double)o[10]);
                    pst.setString(12, (String)o[11]);
                    pst.setString(13, user.getName());
                    pst.setObject(14, (Double)o[10]);
                    pst.setObject(15, (Double)o[9]);

                    pst.addBatch();
                }
                pst.executeBatch();
                pst.close();
            }

            return true;
        } catch (Exception e) {
            LOG.error("Error while inserting qp_pkte objects", e);
            return false;
        } finally {
            if (psCreate != null) {
                try {
                    psCreate.close();
                } catch (SQLException ex) {
                    // nothing to do
                }
            }
            if ((domainServer != null) && (con != null)) {
                domainServer.getConnectionPool().releaseDbConnection(con);
            }
        }
    }

    @Override
    public String getTaskName() {
        return TASK_NAME;
    }
}
