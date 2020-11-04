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

import java.util.ArrayList;
import java.util.Collection;

import de.cismet.cids.custom.helper.CompressedGeometry;

import de.cismet.cids.server.search.AbstractCidsServerSearch;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class Difference extends AbstractCidsServerSearch {

    //~ Static fields/initializers ---------------------------------------------

    /** LOGGER. */
    private static final transient Logger LOG = Logger.getLogger(Difference.class);

    public static final String DOMAIN_NAME = "DLM25W";
    private static final String QUERY = "Select st_asBinary(st_difference('%1s', '%2s'))";

    //~ Instance fields --------------------------------------------------------

    private final Geometry geo;
    private final Geometry geo2;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WkkSearch object.
     *
     * @param  geo   DOCUMENT ME!
     * @param  geo2  dist DOCUMENT ME!
     */
    public Difference(final Geometry geo, final Geometry geo2) {
//        this.geo = new CompressedGeometry(geo);
        this.geo = geo;
        this.geo2 = geo2;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Collection performServerSearch() {
        final MetaService ms = (MetaService)getActiveLocalServers().get(DOMAIN_NAME);

        if (ms != null) {
            try {
                final ArrayList<ArrayList> lists = ms.performCustomSearch(String.format(
                            QUERY,
                            geo,
                            geo2));
                return lists;
            } catch (Exception ex) {
                LOG.error(ex.getMessage(), ex);
            }
        } else {
            LOG.error("active local server not found"); // NOI18N
        }

        return null;
    }
}
