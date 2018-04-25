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

import de.cismet.cids.server.search.AbstractCidsServerSearch;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class MergeBakAe extends AbstractCidsServerSearch {

    //~ Static fields/initializers ---------------------------------------------

    /** LOGGER. */
    private static final transient Logger LOG = Logger.getLogger(MergeBakAe.class);

    private static final String QUERY_WITHOUT_OWNER = "select dlm25w.merge_fg_bak_ae(null);"; // NOI18N
    private static final String QUERY = "select dlm25w.merge_fg_bak_ae('%1$s');";             // NOI18N
    private static final String QUERY_BY_ID = "select dlm25w.merge_fg_bak_ae_by_id(%1$s);";   // NOI18N
    public static final String DOMAIN_NAME = "DLM25W";

    //~ Instance fields --------------------------------------------------------

    private String owner = null;
    private Integer bakId = null;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WkkSearch object.
     *
     * @param  owner  DOCUMENT ME!
     */
    public MergeBakAe(final String owner) {
        this.owner = owner;
    }

    /**
     * Creates a new WkkSearch object.
     *
     * @param  bakId  owner DOCUMENT ME!
     */
    public MergeBakAe(final Integer bakId) {
        this.bakId = bakId;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Collection performServerSearch() {
        final MetaService ms = (MetaService)getActiveLocalServers().get(DOMAIN_NAME);

        if (ms != null) {
            try {
                if (bakId != null) {
                    final String query = String.format(QUERY_BY_ID, bakId);
                    final ArrayList<ArrayList> lists = ms.performCustomSearch(query);

                    return lists;
                } else {
                    final String query = ((owner == null) ? QUERY_WITHOUT_OWNER : String.format(QUERY, owner));
                    final ArrayList<ArrayList> lists = ms.performCustomSearch(query);

                    return lists;
                }
            } catch (RemoteException ex) {
                LOG.error(ex.getMessage(), ex);
            }
        } else {
            LOG.error("active local server not found"); // NOI18N
        }

        return null;
    }
}
