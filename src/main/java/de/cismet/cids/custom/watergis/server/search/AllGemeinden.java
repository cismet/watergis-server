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

import de.cismet.cids.custom.helper.SQLFormatter;

import de.cismet.cids.server.search.AbstractCidsServerSearch;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class AllGemeinden extends AbstractCidsServerSearch {

    //~ Static fields/initializers ---------------------------------------------

    /** LOGGER. */
    private static final transient Logger LOG = Logger.getLogger(AllGemeinden.class);

    public static final String DOMAIN_NAME = "DLM25W";
    private static final String QUERY = "Select \n"
                + "id\n"
                + "from dlm25w.vw_alk_gmd";
    private static final String QUERY_WITH_ID =
        "Select g.id from dlm25w.vw_alk_gmd g join geom gg on (g.geom = gg.id) \n"
                + "where exists(select 1 from dlm25w.fg_ba ba join geom ge on (geom = ge.id) where ba.id = any (%1$s)\n"
                + " and st_intersects(gg.geo_field, ge.geo_field))";

    //~ Instance fields --------------------------------------------------------

    private List<Integer> baIdList;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WkkSearch object.
     */
    public AllGemeinden() {
        this(null);
    }

    /**
     * Creates a new WkkSearch object.
     *
     * @param  baIdList  DOCUMENT ME!
     */
    public AllGemeinden(final List<Integer> baIdList) {
        this.baIdList = baIdList;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Collection performServerSearch() {
        final MetaService ms = (MetaService)getActiveLocalServers().get(DOMAIN_NAME);

        if (ms != null) {
            try {
                if ((baIdList != null) && !baIdList.isEmpty()) {
                    final int[] ids = new int[baIdList.size()];

                    for (int i = 0; i < baIdList.size(); ++i) {
                        ids[i] = baIdList.get(i);
                    }

                    final ArrayList<ArrayList> lists = ms.performCustomSearch(String.format(
                                QUERY_WITH_ID,
                                SQLFormatter.createSqlArrayString(ids)));
                    return lists;
                } else {
                    final ArrayList<ArrayList> lists = ms.performCustomSearch(QUERY);
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
