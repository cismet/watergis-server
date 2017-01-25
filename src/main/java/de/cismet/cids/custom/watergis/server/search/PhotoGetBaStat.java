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

import com.vividsolutions.jts.geom.Geometry;

import org.apache.log4j.Logger;

import java.rmi.RemoteException;

import java.util.ArrayList;
import java.util.Collection;

import de.cismet.cids.custom.helper.SQLFormatter;

import de.cismet.cids.server.search.AbstractCidsServerSearch;

/**
 * Retrieves next fg_ba station.
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class PhotoGetBaStat extends AbstractCidsServerSearch {

    //~ Static fields/initializers ---------------------------------------------

    /** LOGGER. */
    private static final transient Logger LOG = Logger.getLogger(PhotoGetBaStat.class);

    public static final String DOMAIN_NAME = "DLM25W";
    private static final String QUERY =
        "select ba.id, ba_cd, round((ST_line_locate_point(geo_field, '%1$s') * st_length(geo_field))::numeric, 2)::double precision as stat"
                + ",st_asBinary(ST_line_interpolate_point(geo_field, round((ST_line_locate_point(geo_field, '%1$s') "
                + "* st_length(geo_field))::numeric, 2)::double precision  / st_length(geo_field) )) as point from\n"
                + "(select ba.id, ba_cd, geo_field from dlm25w.fg_ba ba join geom on (geom = geom.id) \n"
                + "where st_distance(geo_field, '%1$s') <= %2$s\n"
                + "order by st_distance(geo_field, '%1$s') asc\n"
                + "limit 1) ba";

    //~ Instance fields --------------------------------------------------------

    private final Geometry geom;
    private final double maxDist;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new PhotoGetPhotoNumber object.
     *
     * @param  geom     DOCUMENT ME!
     * @param  maxDist  DOCUMENT ME!
     */
    public PhotoGetBaStat(final Geometry geom, final double maxDist) {
        this.geom = geom;
        this.maxDist = maxDist;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Collection performServerSearch() {
        final MetaService ms = (MetaService)getActiveLocalServers().get(DOMAIN_NAME);

        if (ms != null) {
            try {
                final ArrayList<ArrayList> lists = ms.performCustomSearch(String.format(QUERY, geom.toText(), maxDist));
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
