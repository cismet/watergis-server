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
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class AllGewByGem extends AbstractCidsServerSearch {

    //~ Static fields/initializers ---------------------------------------------

    /** LOGGER. */
    private static final transient Logger LOG = Logger.getLogger(AllGewByGem.class);

    public static final String DOMAIN_NAME = "DLM25W";
    private static final String QUERY =
        "select id, art, ba_cd, ba_st_von, ba_st_bis, nr_re, nr_li, owner, gew_name, gu, wdm, tf, dim from dlm25w.select_gmd(%1$s, %2$s, %3$s) order by ba_st_von";

    //~ Instance fields --------------------------------------------------------

    private final int gemId;
    private final int[] routeIds;
    private final int[] wdmArray;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WkkSearch object.
     *
     * @param  gemId     DOCUMENT ME!
     * @param  routeIds  DOCUMENT ME!
     * @param  wdmArray  DOCUMENT ME!
     */
    public AllGewByGem(final int gemId, final int[] routeIds, final int[] wdmArray) {
        this.gemId = gemId;
        this.routeIds = routeIds;
        this.wdmArray = wdmArray;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Collection performServerSearch() {
        final MetaService ms = (MetaService)getActiveLocalServers().get(DOMAIN_NAME);

        if (ms != null) {
            try {
                final ArrayList<ArrayList> lists = ms.performCustomSearch(String.format(
                            QUERY,
                            gemId,
                            SQLFormatter.createSqlArrayString(routeIds),
                            SQLFormatter.createSqlArrayString(wdmArray)));
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
