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
public class RemoveSchemaAction implements ServerAction, MetaServiceStore {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(RemoveSchemaAction.class);
    private static final String REMOVE_SCHEMA = "DROP SCHEMA %1s cascade";

    public static final String TASK_NAME = "removeSchema";

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
        DomainServerImpl domainServer = null;
        Connection con = null;

        try {
            String schemaName = null;

            for (final ServerActionParameter param : params) {
                if (RemoveSchemaAction.ParameterType.SCHEMA.toString().equals(
                                param.getKey())) {
                    schemaName = (String)param.getValue();
                }
            }

            if (ActionHelper.isInvalidSchemaName(schemaName)) {
                return false;
            }

            domainServer = (DomainServerImpl)metaService;
            con = domainServer.getConnectionPool().getConnection(true);

            final String quotedSchema = ActionHelper.quoteIdentifier(con, schemaName);

            final Statement st = con.createStatement();
            st.executeUpdate(String.format(REMOVE_SCHEMA, quotedSchema));
            st.close();

            return true;
        } catch (Exception e) {
            LOG.error("Error while creating schema", e);
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
