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
public class RouteEnvelopes extends AbstractCidsServerSearch {

    //~ Static fields/initializers ---------------------------------------------

    /** LOGGER. */
    private static final transient Logger LOG = Logger.getLogger(RouteEnvelopes.class);

    public static final String DOMAIN_NAME = "DLM25W";
    private static final String QUERY = "Select \n"
                + "st_asBinary(st_envelope(geom.geo_field)) as geom,\n"
                + "dlm25w.fg_ba.ba_cd, dlm25w.fg_ba.id \n"
                + "from dlm25w.fg_ba \n"
                + "join geom on (geom = geom.id) \n"
                + "left join dlm25w.k_ww_gr dlm25wPk_ww_gr1 on (dlm25w.fg_ba.ww_gr = dlm25wPk_ww_gr1.id) \n"
                + "%1$s"
                + "ORDER BY ba_cd asc";

    //~ Instance fields --------------------------------------------------------

    private final String whereCondition;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WkkSearch object.
     *
     * @param  whereCondition  owner DOCUMENT ME!
     */
    public RouteEnvelopes(final String whereCondition) {
        if (whereCondition == null) {
            this.whereCondition = " ";
        } else {
            this.whereCondition = "where " + whereCondition;
        }
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Collection performServerSearch() {
        final MetaService ms = (MetaService)getActiveLocalServers().get(DOMAIN_NAME);

        if (ms != null) {
            try {
                final String query = String.format(QUERY, whereCondition);
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
