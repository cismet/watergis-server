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
public class LawaCount extends AbstractCidsServerSearch {

    //~ Static fields/initializers ---------------------------------------------

    /** LOGGER. */
    private static final transient Logger LOG = Logger.getLogger(LawaCount.class);

    public static final String DOMAIN_NAME = "DLM25W";
    private static final String DIRECTION_QUERY_WITHOUT_OWNER = "select count(distinct la_cd) from dlm25w.fg_lak";
    private static final String DIRECTION_QUERY = "select count(distinct lak.la_cd) from dlm25w.fg_lak lak "
                + " join dlm25w.fg_bak_gwk gwk on (gwk.la_cd = lak.la_cd)\n"
                + " join dlm25w.fg_bak_linie bak_linie on (gwk.bak_st = bak_linie.id) \n"
                + " join dlm25w.fg_bak_punkt bak_von on (bak_linie.von = bak_von.id)\n"
                + " join dlm25w.fg_bak bak on (bak_von.route = bak.id)\n"
                + " left join dlm25w.k_ww_gr gr on (gr.id = bak.ww_gr) where gr.owner = '%1$s'";

    //~ Instance fields --------------------------------------------------------

    private final String owner;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WkkSearch object.
     *
     * @param  owner  DOCUMENT ME!
     */
    public LawaCount(final String owner) {
        this.owner = owner;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Collection performServerSearch() {
        final MetaService ms = (MetaService)getActiveLocalServers().get(DOMAIN_NAME);

        if (ms != null) {
            try {
                final String query = ((owner == null) ? DIRECTION_QUERY_WITHOUT_OWNER
                                                      : String.format(DIRECTION_QUERY, owner));
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
