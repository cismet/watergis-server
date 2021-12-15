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
import java.sql.SQLException;
import java.sql.Statement;

import de.cismet.cids.server.actions.ServerAction;
import de.cismet.cids.server.actions.ServerActionParameter;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
@org.openide.util.lookup.ServiceProvider(service = ServerAction.class)
public class RefreshTemplateAction implements ServerAction, MetaServiceStore {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(RefreshTemplateAction.class);
    public static final String DRAIN_BASIN = "RefreshDrainBasin";
    public static final String RW_SEG_GEOM = "RefreshRwSegGeom";
    public static final String EZG_K_RL = "RefreshEzgKrl";
    public static final String TASK_NAME = "RefreshTemplate";

    //~ Enums ------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public enum ParameterType {

        //~ Enum constants -----------------------------------------------------

        TEMPLATE, WAIT
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
        String template = null;
        String wait = "false";

        for (final ServerActionParameter param : params) {
            if (RefreshTemplateAction.ParameterType.TEMPLATE.toString().equals(
                            param.getKey())) {
                template = (String)param.getValue();
            } else if (RefreshTemplateAction.ParameterType.WAIT.toString().equals(
                            param.getKey())) {
                wait = (String)param.getValue();
            }
        }

        if ((wait != null) && wait.equalsIgnoreCase("true")) {
            final DomainServerImpl domainServer = (DomainServerImpl)metaService;

            if ((template != null) && template.equals(DRAIN_BASIN)) {
                Connection con = null;
                try {
                    try {
                        con = domainServer.getConnectionPool().getConnection(true);
                        final Statement statement = con.createStatement();
                        statement.execute("select dlm25w.refreshDrainBasin()");
                        statement.close();
                    } catch (SQLException ex) {
                        LOG.error("Cannot refresh drainBasin", ex);
                    }
                } finally {
                    if (con != null) {
                        domainServer.getConnectionPool().releaseDbConnection(con);
                    }
                }
            } else if ((template != null) && template.equals(RW_SEG_GEOM)) {
                Connection con = null;
                try {
                    try {
                        con = domainServer.getConnectionPool().getConnection(true);
                        final Statement statement = con.createStatement();
                        statement.execute("select dlm25w.refreshRwSegGeom()");
                        statement.close();
                    } catch (SQLException ex) {
                        LOG.error("Cannot refresh drainBasin", ex);
                    }
                } finally {
                    if (con != null) {
                        domainServer.getConnectionPool().releaseDbConnection(con);
                    }
                }
            } else if ((template != null) && template.equals(EZG_K_RL)) {
                Connection con = null;
                try {
                    try {
                        con = domainServer.getConnectionPool().getConnection(true);
                        final Statement statement = con.createStatement();
                        statement.execute("select dlm25w.refreshSEzgKRl()");
                        statement.close();
                    } catch (SQLException ex) {
                        LOG.error("Cannot refresh drainBasin", ex);
                    }
                } finally {
                    if (con != null) {
                        domainServer.getConnectionPool().releaseDbConnection(con);
                    }
                }
            }
        } else {
            final DomainServerImpl domainServer = (DomainServerImpl)metaService;

            if ((template != null) && template.equals(DRAIN_BASIN)) {
                TemplateRefresher.getInstance().refreshDrainBasin(domainServer.getConnectionPool());
            } else if ((template != null) && template.equals(RW_SEG_GEOM)) {
                TemplateRefresher.getInstance().refreshRwSegGm(domainServer.getConnectionPool());
            } else if ((template != null) && template.equals(EZG_K_RL)) {
                TemplateRefresher.getInstance().refreshEzgKrl(domainServer.getConnectionPool());
            }
        }

        return true;
    }

    @Override
    public String getTaskName() {
        return TASK_NAME;
    }
}
