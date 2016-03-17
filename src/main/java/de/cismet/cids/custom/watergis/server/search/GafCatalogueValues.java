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

import de.cismet.cids.custom.helper.SQLFormatter;

import de.cismet.cids.server.search.AbstractCidsServerSearch;

/**
 * Retrieves the next photo number.
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class GafCatalogueValues extends AbstractCidsServerSearch {

    //~ Static fields/initializers ---------------------------------------------

    /** LOGGER. */
    private static final transient Logger LOG = Logger.getLogger(GafCatalogueValues.class);

    public static final String DOMAIN_NAME = "DLM25W";
    private static final String QUERY_KZ = "select kz, hyk from dlm25w.k_qp_gaf_kz";
    private static final String QUERY_BK = "select bk from dlm25w.k_qp_gaf_bk";
    private static final String QUERY_RK = "select rk from dlm25w.k_qp_gaf_rk";

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new PhotoGetPhotoNumber object.
     */
    public GafCatalogueValues() {
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Collection performServerSearch() {
        final MetaService ms = (MetaService)getActiveLocalServers().get(DOMAIN_NAME);

        if (ms != null) {
            try {
                final ArrayList<ArrayList> results = new ArrayList<ArrayList>();
                final ArrayList<ArrayList> listKz = ms.performCustomSearch(QUERY_KZ);
                final ArrayList<ArrayList> listRk = ms.performCustomSearch(QUERY_RK);
                final ArrayList<ArrayList> listBk = ms.performCustomSearch(QUERY_BK);

                results.add(listKz);
                results.add(listRk);
                results.add(listBk);

                return results;
            } catch (RemoteException ex) {
                LOG.error(ex.getMessage(), ex);
            }
        } else {
            LOG.error("active local server not found"); // NOI18N
        }

        return null;
    }
}
