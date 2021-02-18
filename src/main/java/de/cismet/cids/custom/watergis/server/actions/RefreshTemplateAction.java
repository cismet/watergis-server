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

    public static final String DRAIN_BASIN = "RefreshTemplate";
    public static final String RW_SEG_GEOM = "RefreshTemplate";
    public static final String TASK_NAME = "RefreshTemplate";

    //~ Enums ------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public enum ParameterType {

        //~ Enum constants -----------------------------------------------------

        TEMPLATE
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

        for (final ServerActionParameter param : params) {
            if (RefreshTemplateAction.ParameterType.TEMPLATE.toString().equals(
                            param.getKey())) {
                template = (String)param.getValue();
            }
        }

        final DomainServerImpl domainServer = (DomainServerImpl)metaService;

        if ((template != null) && template.equals(DRAIN_BASIN)) {
            TemplateRefresher.getInstance().refreshDrainBasin(domainServer.getConnectionPool());
        } else {
            TemplateRefresher.getInstance().refreshRwSegGm(domainServer.getConnectionPool());
        }

        return true;
    }

    @Override
    public String getTaskName() {
        return TASK_NAME;
    }
}
