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

import de.cismet.cids.custom.helper.SQLFormatter;

import de.cismet.cids.server.search.AbstractCidsServerSearch;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class SeeReport extends AbstractCidsServerSearch {

    //~ Static fields/initializers ---------------------------------------------

    /** LOGGER. */
    private static final transient Logger LOG = Logger.getLogger(SeeReport.class);

    public static final String DOMAIN_NAME = "DLM25W";
    // the limit inside the sub select in the from clause prevents that the db use the geom index and improve the
    // performance
    private static final String QUERY =
        "select ST_LineLocatePoint(bg.geo_field, st_startPoint(unnest(dlm25w.multi_geometry_to_array(st_intersection(g.geo_field, bg.geo_field)))))  * st_length(bg.geo_field) von,\n"
                + "ST_LineLocatePoint(bg.geo_field, st_endPoint(unnest(dlm25w.multi_geometry_to_array(st_intersection(g.geo_field, bg.geo_field))))) * st_length(bg.geo_field) bis,\n"
                + "uesg_name, wbbl, s.id, info\n"
                + "from dlm25w.sg_detail s\n"
                + "join (select g.* from dlm25w.sg_detail s join geom g on (s.geom = g.id) limit 5000000) g on (s.geom = g.id),\n"
                + "dlm25w.fg_ba b\n"
                + "join geom bg on (bg.id = b.geom)\n"
                + "where b.ba_cd = '%s' and st_intersects(g.geo_field, bg.geo_field)\n"
                + "order by least(ST_LineLocatePoint(bg.geo_field, st_startPoint(st_intersection(g.geo_field, bg.geo_field)))  * st_length(bg.geo_field),\n"
                + "ST_LineLocatePoint(bg.geo_field, st_endPoint(st_intersection(g.geo_field, bg.geo_field))) * st_length(bg.geo_field))";
//    private static final String QUERYIds =
//        "select ST_LineLocatePoint(bg.geo_field, st_startPoint(unnest(dlm25w.multi_geometry_to_array(st_intersection(g.geo_field, bg.geo_field)))))  * st_length(bg.geo_field) von,\n"
//                + "ST_LineLocatePoint(bg.geo_field, st_endPoint(unnest(dlm25w.multi_geometry_to_array(st_intersection(g.geo_field, bg.geo_field))))) * st_length(bg.geo_field) bis,\n"
//                + "b.ba_cd, b.id, s.id\n"
//                + "from dlm25w.sg_detail s\n"
//                + "join (select g.* from dlm25w.sg_detail s join geom g on (s.geom = g.id) limit 5000000) g on (s.geom = g.id),\n"
//                + "dlm25w.fg_ba b\n"
//                + "join geom bg on (bg.id = b.geom)\n"
//                + "where b.id = any(%s) and st_intersects(g.geo_field, bg.geo_field)\n"
//                + "order by least(ST_LineLocatePoint(bg.geo_field, st_startPoint(st_intersection(g.geo_field, bg.geo_field)))  * st_length(bg.geo_field),\n"
//                + "ST_LineLocatePoint(bg.geo_field, st_endPoint(st_intersection(g.geo_field, bg.geo_field))) * st_length(bg.geo_field))";
    private static final String QUERYIds =
        "select least(von.wert, bis.wert) von, greatest(von.wert, bis.wert) bis, b.ba_cd, b.id, a.id as id\n"
                + "from dlm25w.fg_ba_anll a join dlm25w.k_anll k on (a.anll = k.id ) join dlm25w.fg_ba_linie l on (a.ba_st = l.id)\n"
                + "	join dlm25w.fg_ba_punkt von on (von.id = l.von) join dlm25w.fg_ba_punkt bis on (bis.id = l.bis)\n"
                + "	join dlm25w.fg_ba b on (von.route = b.id) join geom bg on (bg.id = b.geom)\n"
                + "where b.id = any(%s) and k.name ilike 'See' \n"
                + "order by least(von.wert, bis.wert), greatest(von.wert, bis.wert)";

    //~ Instance fields --------------------------------------------------------

    private String baCd = null;
    private int[] baIds = null;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WkkSearch object.
     *
     * @param  baCd  DOCUMENT ME!
     */
    public SeeReport(final String baCd) {
        this.baCd = baCd;
    }

    /**
     * Creates a new WkkSearch object.
     *
     * @param  baIds  DOCUMENT ME!
     */
    public SeeReport(final int[] baIds) {
        this.baIds = baIds;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Collection performServerSearch() {
        final MetaService ms = (MetaService)getActiveLocalServers().get(DOMAIN_NAME);

        if (ms != null) {
            try {
                final String query;

                if (baIds != null) {
                    query = String.format(QUERYIds, SQLFormatter.createSqlArrayString(baIds));
                } else {
                    query = String.format(QUERY, baCd);
                }
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
