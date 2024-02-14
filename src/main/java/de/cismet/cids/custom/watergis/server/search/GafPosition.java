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

import de.cismet.cids.custom.helper.CrsHelper;
import de.cismet.cids.custom.helper.SQLFormatter;

import de.cismet.cids.server.search.AbstractCidsServerSearch;

/**
 * Retrieves next fg_ba station.
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class GafPosition extends AbstractCidsServerSearch {

    //~ Static fields/initializers ---------------------------------------------

    /** LOGGER. */
    private static final transient Logger LOG = Logger.getLogger(GafPosition.class);

    public static final String DOMAIN_NAME = "DLM25W";
    private static final String QUERY = "select st_asbinary(st_intersection(geo_field, st_setSrid('%1$s'::geometry, "
                + CrsHelper.SRID + ")))\n"
                + "from\n"
                + "dlm25w.fg_ba\n"
                + "join geom on (geom = geom.id)\n"
                + "where st_intersects(geo_field, st_setSrid('%1$s'::geometry, " + CrsHelper.SRID + ") limit %2$s";

    //~ Instance fields --------------------------------------------------------

    private final Geometry geom;
    private int limit;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new PhotoGetPhotoNumber object.
     *
     * @param  geom   DOCUMENT ME!
     * @param  limit  DOCUMENT ME!
     */
    public GafPosition(final Geometry geom, final int limit) {
        this.geom = geom;
        this.limit = limit;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Collection performServerSearch() {
        final MetaService ms = (MetaService)getActiveLocalServers().get(DOMAIN_NAME);

        if (ms != null) {
            try {
                final ArrayList<ArrayList> lists = ms.performCustomSearch(String.format(QUERY, geom.toText(), limit));
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
