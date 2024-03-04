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

import java.io.Serializable;

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

        CLASS, USER, SCHEMA, DB_USER, ADDITIONAL_FIELDS
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
            AdditionalField[] additionalFields = null;

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
                if (CreateViewAction.ParameterType.ADDITIONAL_FIELDS.toString().equals(
                                param.getKey())) {
                    additionalFields = (AdditionalField[])param.getValue();
                }
            }

            domainServer = (DomainServerImpl)metaService;
            con = domainServer.getConnectionPool().getLongTermConnection();
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
            String whereExtension = "";

            if (cl.getTableName().equalsIgnoreCase("dlm25w.sg_detail")) {
                if (user.getUserGroup().getName().toLowerCase().startsWith("wbv")) {
                    if (layerInfo.getSelectString().toLowerCase().contains("where")) {
                        whereExtension =
                            " AND (dlm25wPk_ww_gr1.ww_gr >= 4000 OR dlm25wPk_ww_gr1.ww_gr < 3000 OR dlm25wPk_ww_gr1.owner = '"
                                    + user.getUserGroup().getName()
                                    + "')";
                    } else {
                        whereExtension =
                            " WHERE (dlm25wPk_ww_gr1.ww_gr >= 4000 OR dlm25wPk_ww_gr1.ww_gr < 3000 OR dlm25wPk_ww_gr1.owner = '"
                                    + user.getUserGroup().getName()
                                    + "')";
                    }
                }
            }
//            else {
//                if (layerInfo.getSelectString().contains("dlm25wPk_ww_gr1") && (user != null)
//                            && (user.getUserGroup() != null)
//                            && !user.getUserGroup().getName().toLowerCase().startsWith("admin")) {
//                    if (layerInfo.getSelectString().toLowerCase().contains("where")) {
//                        whereExtension = " AND (NOT EXISTS(select 1 FROM dlm25w.k_ww_gr WHERE owner = '"
//                                    + user.getUserGroup().getName()
//                                    + "') OR dlm25wPk_ww_gr1.owner = '"
//                                    + user.getUserGroup().getName()
//                                    + "')";
//                    } else {
//                        whereExtension = " WHERE (NOT EXISTS(select 1 FROM dlm25w.k_ww_gr WHERE owner = '"
//                                    + user.getUserGroup().getName()
//                                    + "') OR dlm25wPk_ww_gr1.owner = '"
//                                    + user.getUserGroup().getName()
//                                    + "')";
//                    }
//                }
//            }

            final Statement st = con.createStatement();
            String selectStatement = layerInfo.getSelectString()
                        .replace("ST_AsEWKb(geom.geo_field)", "st_setSrid(geom.geo_field, 5650)");
            selectStatement = selectStatement.replace("ST_AsEWKb(geom)", "st_setSrid(geom, 5650)");
            selectStatement = selectStatement.replace("ST_AsEWKb", "");

            if ((additionalFields != null) && selectStatement.toLowerCase().contains(" from ")) {
                final String start = selectStatement.substring(0, selectStatement.toLowerCase().indexOf(" from "));
                final String end = selectStatement.substring(selectStatement.toLowerCase().indexOf(" from "));

                selectStatement = start;

                for (final AdditionalField field : additionalFields) {
                    selectStatement += ", "
                                + field.getValue()
                                + " as "
                                + field.getName();
                }

                selectStatement += end;
            }

            st.executeUpdate(String.format(
                    CREATE_VIEW,
                    schemaName
                            + "."
                            + cl.getName(),
                    selectStatement
                            + whereExtension));
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

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public static class AdditionalField implements Serializable {

        //~ Instance fields ----------------------------------------------------

        private String name;
        private Integer pos;
        private String value;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new AdditionalField object.
         *
         * @param  name   DOCUMENT ME!
         * @param  pos    DOCUMENT ME!
         * @param  value  DOCUMENT ME!
         */
        public AdditionalField(final String name, final Integer pos, final String value) {
            this.name = name;
            this.pos = pos;
            this.value = value;
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @return  the name
         */
        public String getName() {
            return name;
        }

        /**
         * DOCUMENT ME!
         *
         * @param  name  the name to set
         */
        public void setName(final String name) {
            this.name = name;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  the pos
         */
        public Integer getPos() {
            return pos;
        }

        /**
         * DOCUMENT ME!
         *
         * @param  pos  the pos to set
         */
        public void setPos(final Integer pos) {
            this.pos = pos;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  the value
         */
        public String getValue() {
            return value;
        }

        /**
         * DOCUMENT ME!
         *
         * @param  value  the value to set
         */
        public void setValue(final String value) {
            this.value = value;
        }
    }
}
