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

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import de.cismet.cids.custom.helper.ActionHelper;

import de.cismet.cids.server.actions.ServerAction;
import de.cismet.cids.server.actions.ServerActionParameter;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
@org.openide.util.lookup.ServiceProvider(service = ServerAction.class)
public class RemoveUserAction implements ServerAction, MetaServiceStore {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(RemoveUserAction.class);
    // The usage of prepared statements are not possible for create role
    private static final String REMOVE_USER = "DROP ROLE %1s;";

    public static final String TASK_NAME = "removeUser";

    //~ Enums ------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public enum ParameterType {

        //~ Enum constants -----------------------------------------------------

        DB_USER
    }

    //~ Instance fields --------------------------------------------------------

    private MetaService metaService;

    //~ Methods ----------------------------------------------------------------

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
            String dbUser = null;

            for (final ServerActionParameter param : params) {
                if (RemoveUserAction.ParameterType.DB_USER.toString().equals(
                                param.getKey())) {
                    dbUser = (String)param.getValue();
                }
            }

            if (ActionHelper.isInvalidUserName(dbUser)) {
                return false;
            }

            domainServer = (DomainServerImpl)metaService;
            con = domainServer.getConnectionPool().getLongTermConnection();

            final String quotedUser = ActionHelper.quoteIdentifier(con, dbUser);
            final Statement st = con.createStatement();
            st.executeUpdate(String.format(REMOVE_USER, quotedUser));
            st.close();

            return true;
        } catch (Exception e) {
            LOG.error("Error while removing user", e);
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
