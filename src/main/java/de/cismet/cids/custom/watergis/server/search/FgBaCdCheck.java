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
import java.util.Map;

import de.cismet.cids.server.search.AbstractCidsServerSearch;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class FgBaCdCheck extends AbstractCidsServerSearch {

    //~ Static fields/initializers ---------------------------------------------

    /** LOGGER. */
    private static final transient Logger LOG = Logger.getLogger(FgBaCdCheck.class);

    public static final String DOMAIN_NAME = "DLM25W";
    private static final String QUERY_CONDITION = "(id <> %s and ba_cd = '%s')";
    private static final String QUERY = "select ba_cd from dlm25w.fg_bak bak ";

    //~ Instance fields --------------------------------------------------------

    private final Map<Integer, String> baCdMap;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WkkSearch object.
     *
     * @param  baCdMap  DOCUMENT ME!
     */
    public FgBaCdCheck(final Map<Integer, String> baCdMap) {
        this.baCdMap = baCdMap;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Collection performServerSearch() {
        final MetaService ms = (MetaService)getActiveLocalServers().get(DOMAIN_NAME);

        if (ms != null) {
            try {
                boolean first = true;
                String query = QUERY;

                for (final Integer id : baCdMap.keySet()) {
                    final String baCd = baCdMap.get(id);

                    if (first) {
                        query += " WHERE ";
                        first = false;
                    } else {
                        query += " OR ";
                    }

                    query += String.format(QUERY_CONDITION, id, baCd);
                }

                final ArrayList<ArrayList> lists = ms.performCustomSearch(query);
                return lists;
            } catch (RemoteException ex) {
                LOG.error(ex.getMessage(), ex);
            }
        } else {
            LOG.error("active local server not found"); // NOI18N
        }

        return null;
    }
}
