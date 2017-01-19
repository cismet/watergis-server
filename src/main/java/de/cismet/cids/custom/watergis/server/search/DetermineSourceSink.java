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
 * Determines the source and the sinke of the given river.
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class DetermineSourceSink extends AbstractCidsServerSearch {

    //~ Static fields/initializers ---------------------------------------------

    // wird im Moment nicht genutzt

    /** LOGGER. */
    private static final transient Logger LOG = Logger.getLogger(FgBaCdCheck.class);

    public static final String DOMAIN_NAME = "DLM25W";
    private static final String QUERY =
        "select case when st_distance(st_pointN(bg.geo_field, 1), bg2.geo_field) < 0.5 then b2.ba_cd else null end as senke,\n"
                + "       case when st_distance(st_pointN(bg.geo_field, st_numPoints(bg.geo_field)), bg2.geo_field) < 0.5 then b2.ba_cd else null end as quelle\n"
                + "                from \n"
                + "                dlm25w.fg_ba b\n"
                + "                join dlm25w.fg_bak bak on (b.bak_id = bak.id)\n"
                + "                join geom bg on (bg.id = bak.geom),\n"
                + "                dlm25w.fg_ba b2\n"
                + "                join dlm25w.fg_bak bak2 on (b2.bak_id = bak2.id)\n"
                + "                join geom bg2 on (bg2.id = bak2.geom)\n"
                + "--                join (select id, dlm25w.increase_line_length(geo_field, 0.1) as geo_field from geom where id in (select geom from dlm25w.fg_bak)) bg2 on (bg2.id = bak2.geom)\n"
                + "                where b.ba_cd = '%1s' and b.id <> b2.id and \n"
                + "                (\n"
                + "                st_intersects(st_buffer(st_pointN(bg.geo_field, 1), 0.2), bg2.geo_field)\n"
                + "                or\n"
                + "                st_intersects(st_buffer(st_pointN(bg.geo_field, st_numPoints(bg.geo_field)), 0.2), bg2.geo_field)\n"
                + "                )";

    //~ Instance fields --------------------------------------------------------

    private final String baCd;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WkkSearch object.
     *
     * @param  baCd  DOCUMENT ME!
     */
    public DetermineSourceSink(final String baCd) {
        this.baCd = baCd;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Collection performServerSearch() {
        final MetaService ms = (MetaService)getActiveLocalServers().get(DOMAIN_NAME);

        if (ms != null) {
            try {
                final String query = String.format(QUERY, baCd);

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
