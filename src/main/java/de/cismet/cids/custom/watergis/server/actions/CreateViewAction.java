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
import Sirius.server.middleware.types.MetaClass;
import Sirius.server.newuser.User;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.Statement;

import de.cismet.cids.custom.helper.ActionHelper;

import de.cismet.cids.server.actions.ServerAction;
import de.cismet.cids.server.actions.ServerActionParameter;
import de.cismet.cids.server.cidslayer.CidsLayerInfo;

import de.cismet.cids.tools.CidsLayerUtil;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
@org.openide.util.lookup.ServiceProvider(service = ServerAction.class)
public class CreateViewAction implements ServerAction, MetaServiceStore {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(CreateViewAction.class);
    private static final String CREATE_VIEW = "CREATE VIEW %1s as %2s";
    private static final String ADD_PERMISSION = "GRANT SELECT ON TABLE %1s TO %2s;";

    public static final String TASK_NAME = "createView";

    //~ Enums ------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public enum ParameterType {

        //~ Enum constants -----------------------------------------------------

        CLASS, USER, SCHEMA, DB_USER
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

        try {
            Integer clazzId = null;
            User user = null;
            String schemaName = null;
            String dbUser = null;

            for (final ServerActionParameter param : params) {
                if (CreateViewAction.ParameterType.CLASS.toString().equals(
                                param.getKey())) {
                    clazzId = (Integer)param.getValue();
                }
                if (CreateViewAction.ParameterType.USER.toString().equals(
                                param.getKey())) {
                    user = (User)param.getValue();
                }
                if (CreateViewAction.ParameterType.SCHEMA.toString().equals(
                                param.getKey())) {
                    schemaName = (String)param.getValue();
                }
                if (CreateViewAction.ParameterType.DB_USER.toString().equals(
                                param.getKey())) {
                    dbUser = (String)param.getValue();
                }
            }

            domainServer = (DomainServerImpl)metaService;
            con = domainServer.getConnectionPool().getConnection(true);
            dbUser = ActionHelper.quoteIdentifier(con, dbUser);
            schemaName = ActionHelper.quoteIdentifier(con, schemaName);

            if (ActionHelper.isInvalidUserName(dbUser)) {
                return false;
            }

            if (ActionHelper.isInvalidSchemaName(schemaName)) {
                return false;
            }

            final MetaClass cl = metaService.getClass(user, clazzId);
            final CidsLayerInfo layerInfo = CidsLayerUtil.getCidsLayerInfo(cl, user);

            final Statement st = con.createStatement();
            st.executeUpdate(String.format(
                    CREATE_VIEW,
                    schemaName
                            + "."
                            + cl.getName(),
                    layerInfo.getSelectString().replace("ST_AsEWKb", "")));
            st.executeUpdate(String.format(ADD_PERMISSION, schemaName + "." + cl.getName(), dbUser));

            return true;
        } catch (Exception e) {
            LOG.error("Error while extracting the data sources", e);
            return false;
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
