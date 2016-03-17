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
public class AllGewGeschlByGem extends AbstractCidsServerSearch {

    //~ Static fields/initializers ---------------------------------------------

    /** LOGGER. */
    private static final transient Logger LOG = Logger.getLogger(AllGewGeschlByGem.class);

    public static final String DOMAIN_NAME = "DLM25W";
    private static final String QUERY =
        "select id, owner, gu, wdm, gew_name, tf, dim, art, ba_cd, ba_st_von, ba_st_bis, nr_re, nr_li, ls, prof, ma, objNr, tr, ausbaujahr, wbbl, code, zustKl, br, brOben, hoehe, hEin, hAus, gefaelle, dhAus, dhEin, hAb, hAuf, aufstieg from dlm25w.select_gmd_geschl(%1$s, %2$s, %3$s)";
    private static final String QUERY_WITHOUT_GEM =
        "select id, owner, gu, wdm, gew_name, tf, dim, art, ba_cd, ba_st_von, ba_st_bis, nr_re, nr_li, ls, prof, ma, objNr, tr, ausbaujahr, wbbl, code, zustKl, br, brOben, hoehe, hEin, hAus, gefaelle, dhAus, dhEin, hAb, hAuf, aufstieg from dlm25w.select_gmd_geschl(%1$s, %2$s)";

    //~ Instance fields --------------------------------------------------------

    private final int gemId;
    private final int[] routeIds;
    private final int[] wdmArray;
    private boolean withoutGem = false;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WkkSearch object.
     *
     * @param  routeIds  DOCUMENT ME!
     * @param  wdmArray  DOCUMENT ME!
     */
    public AllGewGeschlByGem(final int[] routeIds, final int[] wdmArray) {
        this.gemId = 0;
        this.routeIds = routeIds;
        this.wdmArray = wdmArray;
        withoutGem = true;
    }

    /**
     * Creates a new WkkSearch object.
     *
     * @param  gemId     DOCUMENT ME!
     * @param  routeIds  DOCUMENT ME!
     * @param  wdmArray  DOCUMENT ME!
     */
    public AllGewGeschlByGem(final int gemId, final int[] routeIds, final int[] wdmArray) {
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
                if (withoutGem) {
                    final String query = String.format(
                            QUERY_WITHOUT_GEM,
                            SQLFormatter.createSqlArrayString(routeIds),
                            SQLFormatter.createSqlArrayString(wdmArray));
                    final ArrayList<ArrayList> lists = ms.performCustomSearch(query);
                    return lists;
                } else {
                    final String query = String.format(
                            QUERY,
                            gemId,
                            SQLFormatter.createSqlArrayString(routeIds),
                            SQLFormatter.createSqlArrayString(wdmArray));
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
