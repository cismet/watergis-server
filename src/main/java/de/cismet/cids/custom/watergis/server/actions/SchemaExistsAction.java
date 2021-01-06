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
public class SchemaExistsAction implements ServerAction, MetaServiceStore {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(SchemaExistsAction.class);
    private static final String SCHEMA_EXISTS =
        "SELECT schema_name FROM information_schema.schemata WHERE schema_name = ?;";
    public static final String TASK_NAME = "schemaExists";

    //~ Enums ------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public enum ParameterType {

        //~ Enum constants -----------------------------------------------------

        SCHEMA
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
        boolean schemaExists = false;
        DomainServerImpl domainServer = null;
        Connection con = null;
        try {
            String schemaName = null;

            for (final ServerActionParameter param : params) {
                if (SchemaExistsAction.ParameterType.SCHEMA.toString().equals(
                                param.getKey())) {
                    schemaName = (String)param.getValue();
                }
            }

            if (ActionHelper.isInvalidSchemaName(schemaName)) {
                return false;
            }

            domainServer = (DomainServerImpl)metaService;
            con = domainServer.getConnectionPool().getConnection(true);

            final PreparedStatement psCreate = con.prepareStatement(SCHEMA_EXISTS);
            psCreate.setString(1, schemaName);
            final boolean res = psCreate.execute();

            if (res) {
                final ResultSet rs = psCreate.getResultSet();

                if ((rs != null) && rs.next()) {
                    schemaExists = true;
                }

                if (rs != null) {
                    rs.close();
                }
            }

            psCreate.close();

            return schemaExists;
        } catch (Exception e) {
            LOG.error("Error while checking, if the given schema already exists", e);
            return schemaExists;
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
