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
import java.util.List;

import de.cismet.cids.custom.helper.SQLFormatter;

import de.cismet.cids.server.search.AbstractCidsServerSearch;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class PreparedRandstreifenGeoms extends AbstractCidsServerSearch {

    //~ Static fields/initializers ---------------------------------------------

    /** LOGGER. */
    private static final transient Logger LOG = Logger.getLogger(PreparedRandstreifenGeoms.class);

    public static final String DOMAIN_NAME = "DLM25W";
    private static final String QUERY = "Select st_asBinary(st_buffer(ST_Collect(geo_field), 0))"
                + " from (%1s) a";
    private static final String FG_QUERY =
        "select geo_field from dlm25w.fg_ba_gerog join geom on (geom = geom.id) where typ = 'so' and st_intersects(geo_field, '%1s')";
    private static final String FG_BR_QUERY =
        "select st_buffer(geo_field, %1s) as geo_field from dlm25w.fg_ba join geom on (geom = geom.id) where st_intersects(geo_field, '%1s')";
    private static final String FG_FL_QUERY =
        "select geo_field from dlm25w.fg_ba_fl join geom on (geom = geom.id) where st_intersects(geo_field, '%1s')";
    private static final String SEE_QUERY =
        "select geo_field from dlm25w.sg_see join geom on (geom = geom.id) where st_intersects(geo_field, '%1s')";
    private static final String SEE_KL_QUERY =
        "select geo_field from dlm25w.sg_see_kl join geom on (geom = geom.id) where st_intersects(geo_field, '%1s')";
    private static final String OSTSEE_QUERY =
        "select dlm25w.withoutHoles(geo_field) as geo_field from dlm25w.ezg_mv_ostsee join geom on (geom = geom.id) where st_intersects(geo_field, '%1s')";

    private static final String FG_QUERY_WITHOUT_GEO =
        "select geo_field from dlm25w.fg_ba_gerog join geom on (geom = geom.id) where typ = 'so'";
    private static final String FG_BR_QUERY_WITHOUT_GEO =
        "select st_buffer(geo_field, %1s) as geo_field from dlm25w.fg_ba join geom on (geom = geom.id)";
    private static final String FG_FL_QUERY_WITHOUT_GEO =
        "select geo_field from dlm25w.fg_ba_fl join geom on (geom = geom.id)";
    private static final String SEE_QUERY_WITHOUT_GEO =
        "select geo_field from dlm25w.sg_see join geom on (geom = geom.id)";
    private static final String SEE_KL_QUERY_WITHOUT_GEO =
        "select geo_field from dlm25w.sg_see_kl join geom on (geom = geom.id)";
    private static final String OSTSEE_QUERY_WITHOUT_GEO =
        "select dlm25w.withoutHoles(geo_field) as geo_field from dlm25w.ezg_mv_ostsee join geom on (geom = geom.id)";

    //~ Instance fields --------------------------------------------------------

    private final Geometry bbox;
    private final boolean fg;
    private final boolean fgBr;
    private final double br;
    private final boolean fgFl;
    private final boolean see;
    private final boolean seeKl;
    private final boolean ostsee;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WkkSearch object.
     *
     * @param  bbox    DOCUMENT ME!
     * @param  fg      DOCUMENT ME!
     * @param  fgBr    DOCUMENT ME!
     * @param  fgFl    DOCUMENT ME!
     * @param  see     DOCUMENT ME!
     * @param  seeKl   DOCUMENT ME!
     * @param  ostsee  DOCUMENT ME!
     * @param  br      DOCUMENT ME!
     */
    public PreparedRandstreifenGeoms(final Geometry bbox,
            final boolean fg,
            final boolean fgBr,
            final boolean fgFl,
            final boolean see,
            final boolean seeKl,
            final boolean ostsee,
            final double br) {
        this.bbox = bbox;
        this.fg = fg;
        this.fgBr = fgBr;
        this.fgFl = fgFl;
        this.see = see;
        this.seeKl = seeKl;
        this.ostsee = ostsee;
        this.br = br;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Collection performServerSearch() {
        final MetaService ms = (MetaService)getActiveLocalServers().get(DOMAIN_NAME);

        if (ms != null) {
            try {
                String tables = null;

                if (fg) {
                    tables = String.format(((bbox == null) ? FG_QUERY_WITHOUT_GEO : FG_QUERY), bbox);
                }

                if (fgBr) {
                    if (tables == null) {
                        if (bbox == null) {
                            tables = String.format(FG_BR_QUERY_WITHOUT_GEO, br);
                        } else {
                            tables = String.format(FG_BR_QUERY, br, bbox);
                        }
                    } else {
                        if (bbox == null) {
                            tables += " union " + String.format(FG_BR_QUERY_WITHOUT_GEO, br);
                        } else {
                            tables += " union " + String.format(FG_BR_QUERY, br, bbox);
                        }
                    }
                }

                if (fgFl) {
                    if (tables == null) {
                        tables = String.format(((bbox == null) ? FG_FL_QUERY_WITHOUT_GEO : FG_FL_QUERY), bbox);
                    } else {
                        tables += " union "
                                    + String.format(((bbox == null) ? FG_FL_QUERY_WITHOUT_GEO : FG_FL_QUERY), bbox);
                    }
                }

                if (see) {
                    if (tables == null) {
                        tables = String.format(((bbox == null) ? SEE_QUERY_WITHOUT_GEO : SEE_QUERY), bbox);
                    } else {
                        tables += " union " + String.format(((bbox == null) ? SEE_QUERY_WITHOUT_GEO : SEE_QUERY), bbox);
                    }
                }

                if (seeKl) {
                    if (tables == null) {
                        tables = String.format(((bbox == null) ? SEE_KL_QUERY_WITHOUT_GEO : SEE_KL_QUERY), bbox);
                    } else {
                        tables += " union "
                                    + String.format(((bbox == null) ? SEE_KL_QUERY_WITHOUT_GEO : SEE_KL_QUERY), bbox);
                    }
                }

                if (ostsee) {
                    if (tables == null) {
                        tables = String.format(((bbox == null) ? OSTSEE_QUERY_WITHOUT_GEO : OSTSEE_QUERY), bbox);
                    } else {
                        tables += " union "
                                    + String.format(((bbox == null) ? OSTSEE_QUERY_WITHOUT_GEO : OSTSEE_QUERY), bbox);
                    }
                }

                final String query = String.format(QUERY, tables);
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
