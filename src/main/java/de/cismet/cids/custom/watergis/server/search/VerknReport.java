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
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class VerknReport extends AbstractCidsServerSearch {

    //~ Static fields/initializers ---------------------------------------------

    /** LOGGER. */
    private static final transient Logger LOG = Logger.getLogger(VerknReport.class);

    public static final String DOMAIN_NAME = "DLM25W";
    // todo: Kommentar vor Zeilen mit st_LineCrossingDirection entfernen
    // st_LineCrossingDirection does not work properly in postgis 1.4 (segmentation faults and so on)
    private static final String QUERY =
        "select  case when st_distance(ba.geo_field, geo_field2) > 0.5 then null else ST_LineLocatePoint(ba.geo_field, intersectedGeom) * st_length(ba.geo_field) end,\n"
                + "	case when ST_LineLocatePoint(geo_field1, intersectedGeom) = 1 or ST_LineLocatePoint(geo_field2, intersectedGeom) * st_length(geo_field2) < 0.5 then ba_cd else ' ' end as einmuend,\n"
                + "	case when ST_LineLocatePoint(geo_field1, intersectedGeom) <> 1 and ST_LineLocatePoint(geo_field2, intersectedGeom) * st_length(geo_field2) >= 0.5 then ba_cd else ' ' end as entspr,\n"
                + "	CASE WHEN ST_LineLocatePoint(geo_field1, intersectedGeom) = 1 or ST_LineLocatePoint(geo_field1, intersectedGeom) = 0 THEN null ELSE case when dlm25w.crossOrTouchDirection(geo_field2, geo_field1) = -1 then 're' else 'li' end END as rl\n"
                + "	, (\n"
                + "	--Quelle und entspringend\n"
                + "--	(ST_LineLocatePoint(geo_field1, intersectedGeom) = 1 and ST_LineLocatePoint(geo_field2, intersectedGeom) * st_length(geo_field2) >= 0.5)\n"
                + "--       or \n"
                + "	--Senke und einmündend\n"
                + "	(ST_LineLocatePoint(geo_field1, intersectedGeom)  * st_length(geo_field1) <= 0.1 and ST_LineLocatePoint(geo_field2, intersectedGeom) * st_length(geo_field2) < 0.5)\n"
                + "	--kreuzendes Gewaesser\n"
                + "	or \n"
                + "     (ST_LineLocatePoint(geo_field2, intersectedGeom) * st_length(geo_field2) > 0.5 \n"
                + "	and ST_LineLocatePoint(geo_field2, intersectedGeom) * st_length(geo_field2) < st_length(geo_field2) - 0.5\n"
                + "     and ST_LineLocatePoint(geo_field1, intersectedGeom) * st_length(geo_field1) > 0.5 \n"
                + "     and ST_LineLocatePoint(geo_field1, intersectedGeom) * st_length(geo_field1) < st_length(geo_field1) - 0.5\n"
                + "	 )\n"
                + "       ) as ignore\n"
                + "\n"
                + "from \n"
                + "       (select b2.ba_cd, bg.geo_field geo_field1, bg2.geo_field geo_field2, unnest(dlm25w.multi_geometry_to_array( st_intersection(bg.geo_field, bg2.geo_field))) intersectedGeom\n"
                + "       from \n"
                + "       dlm25w.fg_ba b\n"
                + "       join dlm25w.fg_bak bak on (b.bak_id = bak.id)\n"
                + "       join (select id, dlm25w.increase_line_length(geo_field, 0.1) as geo_field from geom where id = (select geom from dlm25w.fg_bak where ba_cd = '%s')) bg on (bg.id = bak.geom),\n"
                + "       dlm25w.fg_ba b2\n"
                + "       join dlm25w.fg_bak bak2 on (b2.bak_id = bak2.id)\n"
                + "       join (select id, dlm25w.increase_line_length(geo_field, 0.1) as geo_field from geom where id in (select geom from dlm25w.fg_bak)) bg2 on (bg2.id = bak2.geom)\n"
                + "       where b.ba_cd = '%s' and b.id <> b2.id and st_intersects(bg.geo_field, bg2.geo_field)\n"
                + "             and (st_geometryType(st_intersection(bg.geo_field, bg2.geo_field)) = 'ST_Point' or st_geometryType(st_intersection(bg.geo_field, bg2.geo_field)) = 'ST_MultiPoint')\n"
                + "       order by  ST_LineLocatePoint(bg.geo_field, unnest(dlm25w.multi_geometry_to_array( st_intersection(bg.geo_field, bg2.geo_field)))) desc,\n"
                + "       (ST_LineLocatePoint(bg2.geo_field, unnest(dlm25w.multi_geometry_to_array( st_intersection(bg.geo_field, bg2.geo_field)))) * st_length(bg2.geo_field) < 0.5) desc,\n"
                + "       b2.ba_cd) points,\n"
                + "       (select geo_field from dlm25w.fg_ba join geom on (geom = geom.id) where ba_cd = '%s') ba";

    //~ Instance fields --------------------------------------------------------

    private final String baCd;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WkkSearch object.
     *
     * @param  baCd  DOCUMENT ME!
     */
    public VerknReport(final String baCd) {
        this.baCd = baCd;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Collection performServerSearch() {
        final MetaService ms = (MetaService)getActiveLocalServers().get(DOMAIN_NAME);

        if (ms != null) {
            try {
                final String query = String.format(QUERY, baCd, baCd, baCd);
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
