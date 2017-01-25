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
public class AllGewOffenBySb extends AbstractCidsServerSearch {

    //~ Static fields/initializers ---------------------------------------------

    /** LOGGER. */
    private static final transient Logger LOG = Logger.getLogger(AllGewOffenBySb.class);

    public static final String DOMAIN_NAME = "DLM25W";
    private static final String QUERY =
        "select id,owner,gu,wdm,gew_name,ba_cd,ba_st_von,ba_st_bis,nr_re,nr_li,profil,wk_fg_length, bv_re,bl_n_li,fl_b_re,bl_n_re,fl_b,fl_bt_re,fl_n,fl_ger,br_gew_re,fl_bn_re,bh_re,wbbl,fl_qs_ger,fl_bn,typ,bl_li,fl_bn_li,bh_li,bl_t_li,bemerkung,br_gew,ausbaujahr,fl_so,br_gew_li,ho_a,fl_gew,fl_qs_gew,br_so,ho_e,fl_bt,mw,fl_bt_li,gefaelle,bl_t_re,bv_li,fl_b_li,bl_re,obj_nr from dlm25w.select_sb_offen(%1$s, %2$s)";

    //~ Instance fields --------------------------------------------------------

    private final int[] routeIds;
    private final int[] wdmArray;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WkkSearch object.
     *
     * @param  routeIds  DOCUMENT ME!
     * @param  wdmArray  DOCUMENT ME!
     */
    public AllGewOffenBySb(final int[] routeIds, final int[] wdmArray) {
        this.routeIds = routeIds;
        this.wdmArray = wdmArray;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Collection performServerSearch() {
        final MetaService ms = (MetaService)getActiveLocalServers().get(DOMAIN_NAME);

        if (ms != null) {
            try {
                final String query = String.format(
                        QUERY,
                        SQLFormatter.createSqlArrayString(routeIds),
                        SQLFormatter.createSqlArrayString(wdmArray));
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
