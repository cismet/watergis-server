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
package de.cismet.cids.custom.watergis.server.search;

import Sirius.server.middleware.interfaces.domainserver.MetaService;

import org.apache.log4j.Logger;

import java.rmi.RemoteException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.cismet.cids.server.search.AbstractCidsServerSearch;

import de.cismet.connectioncontext.ConnectionContext;
import de.cismet.connectioncontext.ConnectionContextStore;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class WsgRechtSearch extends AbstractCidsServerSearch implements ConnectionContextStore {

    //~ Static fields/initializers ---------------------------------------------

    /** LOGGER. */
    private static final transient Logger LOG = Logger.getLogger(WsgRechtSearch.class);

    public static final String DOMAIN_NAME = "DLM25W";
    private static final String QUERY_STAT = "select wsg_anz, wsg_fl, anz_brd, anz_ddr from dlm25w.wr_sg_wsg_stat;";
    private static final String QUERY_RECHT = "select recht, st_area(geom) from dlm25w.wr_sg_wsg_rgl;";

    //~ Instance fields --------------------------------------------------------

    private ConnectionContext connectionContext = ConnectionContext.createDummy();

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WkkSearch object.
     */
    public WsgRechtSearch() {
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Collection performServerSearch() {
        final MetaService ms = (MetaService)getActiveLocalServers().get(DOMAIN_NAME);

        if (ms != null) {
            try {
                final List<Object> result = new ArrayList<Object>();
                ArrayList<ArrayList> lists = ms.performCustomSearch(QUERY_STAT);

                if ((lists != null) && (lists.size() > 0)) {
                    final ArrayList tmp = lists.get(0);

                    if ((tmp != null) && (tmp.size() > 1)) {
                        result.add(tmp.get(0));
                        result.add(tmp.get(1));
                        result.add(tmp.get(2));
                        result.add(tmp.get(3));
                    }
                }

                lists = ms.performCustomSearch(QUERY_RECHT);
                Object brd = null;
                Object ddr = null;

                if ((lists != null) && (lists.size() > 0)) {
                    for (final ArrayList tmp : lists) {
                        if ((tmp != null) && (tmp.size() > 1)) {
                            if ((tmp.get(0) != null) && tmp.get(0).equals("BRD")) {
                                brd = tmp.get(1);
                            } else if ((tmp.get(0) != null) && tmp.get(0).equals("DDR")) {
                                ddr = tmp.get(1);
                            }
                        }
                    }
                }

                result.add(brd);
                result.add(ddr);

                return result;
            } catch (RemoteException ex) {
                LOG.error(ex.getMessage(), ex);
            }
        } else {
            LOG.error("active local server not found"); // NOI18N
        }

        return null;
    }

    @Override
    public void initWithConnectionContext(final ConnectionContext cc) {
        this.connectionContext = connectionContext;
    }

    @Override
    public ConnectionContext getConnectionContext() {
        return this.connectionContext;
    }
}
