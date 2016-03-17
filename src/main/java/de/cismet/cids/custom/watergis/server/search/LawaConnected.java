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
public class LawaConnected extends AbstractCidsServerSearch {

    //~ Static fields/initializers ---------------------------------------------

    /** LOGGER. */
    private static final transient Logger LOG = Logger.getLogger(LawaConnected.class);

    public static final String DOMAIN_NAME = "DLM25W";
    private static final String CONNECTION_QUERY = "select k.la_cd, unnest(array_append(\n"
                + "  st_asBinary(st_line_substring(geo_field, least(von.wert, bis.wert) / st_length(geo_field), \n"
                + "  case when (greatest(bis.wert, von.wert) / st_length(geo_field)) <= 1.0 then (greatest(bis.wert, von.wert) / st_length(geo_field)) else 1.0 end)\n"
                + ")))\n"
                + "from dlm25w.fg_bak_gwk gwk \n"
                + "join dlm25w.fg_bak_linie linie on (gwk.bak_st = linie.id) \n"
                + "join dlm25w.fg_bak_punkt von on (linie.von = von.id) \n"
                + "join dlm25w.fg_bak_punkt bis on (linie.bis = bis.id)\n"
                + "join dlm25w.fg_bak bak on (von.route = bak.id) \n"
                + "left join dlm25w.k_ww_gr gr on (gr.id = bak.ww_gr)"
                + "join geom on (bak.geom = geom.id) \n"
                + "left join dlm25w.k_gwk_lawa  k on (gwk.la_cd = k.id)\n"
                + "where (gr.owner = %1$s or %1$s is null)"
                + "group by k.la_cd\n"
                + "having st_geometrytype(dlm25w.line_merge((array_append(st_line_substring(\n"
                + "geo_field, \n"
                + "least(von.wert, bis.wert) / st_length(geo_field), \n"
                + "case when (greatest(bis.wert, von.wert) / st_length(geo_field)) <= 1.0 then (greatest(bis.wert, von.wert) / st_length(geo_field)) else 1.0 end\n"
                + ") )), 0.1)) <> 'ST_LineString'\n"
                + "order by la_cd";

    //~ Instance fields --------------------------------------------------------

    private String owner;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WkkSearch object.
     *
     * @param  owner  DOCUMENT ME!
     */
    public LawaConnected(final String owner) {
        this.owner = owner;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Collection performServerSearch() {
        final MetaService ms = (MetaService)getActiveLocalServers().get(DOMAIN_NAME);

        if (ms != null) {
            try {
                if (owner == null) {
                    owner = "null";
                } else {
                    owner = "'" + owner + "'";
                }
                final String query = String.format(CONNECTION_QUERY, owner);
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
