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
import java.sql.ResultSet;

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
public class UserExistsAction implements ServerAction, MetaServiceStore {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(UserExistsAction.class);
    private static final String USER_EXISTS = "SELECT 1 FROM pg_roles WHERE rolname= ?";
    public static final String TASK_NAME = "userExists";

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
        boolean userExists = false;
        DomainServerImpl domainServer = null;
        Connection con = null;
        try {
            String userName = null;

            for (final ServerActionParameter param : params) {
                if (UserExistsAction.ParameterType.DB_USER.toString().equals(
                                param.getKey())) {
                    userName = (String)param.getValue();
                }
            }

            if (ActionHelper.isInvalidSchemaName(userName)) {
                return false;
            }

            domainServer = (DomainServerImpl)metaService;
            con = domainServer.getConnectionPool().getConnection(true);

            final PreparedStatement psCreate = con.prepareStatement(USER_EXISTS);
            psCreate.setString(1, userName);
            final boolean res = psCreate.execute();

            if (res) {
                final ResultSet rs = psCreate.getResultSet();

                if ((rs != null) && rs.next()) {
                    userExists = true;
                }

                if (rs != null) {
                    rs.close();
                }
            }

            psCreate.close();

            return userExists;
        } catch (Exception e) {
            LOG.error("Error while checking, if the given schema already exists", e);
            return userExists;
        } finally {
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
