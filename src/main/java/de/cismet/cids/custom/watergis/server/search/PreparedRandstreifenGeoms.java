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

import Sirius.server.middleware.impls.domainserver.DomainServerImpl;
import Sirius.server.middleware.interfaces.domainserver.MetaService;

import com.vividsolutions.jts.geom.Geometry;

import org.apache.log4j.Logger;

import java.rmi.RemoteException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Collection;

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
    private static final String QUERY =
        "Select st_asBinary(st_buffer(ST_Collect(ST_SnapToGrid(geo_field, 0.01)), 0.01))"
                + " from (%1s) a";
    private static final String FG_QUERY =
        "select geo_field from dlm25w.fg_ba_gerog join geom on (geom = geom.id) where typ in ('so', 'b_li', 'b_re', 'bn_li', 'bn_re', 'bt_li', 'bt_re')  and st_intersects(geo_field, '%1s')";
    private static final String FG_QUERY_WITH_ID =
        "select geo_field from dlm25w.fg_ba_gerog join geom on (geom = geom.id) where typ in ('so', 'b_li', 'b_re', 'bn_li', 'bn_re', 'bt_li', 'bt_re')  and st_intersects(geo_field, '%1s') and ba_cd in (%2s)";
    private static final String FG_CLOSED_QUERY =
        "select st_buffer(geom, %1s) as geo_field from dlm25w.select_fgba_closed(null, null)";
    private static final String FG_CLOSED_QUERY_WITH_ID =
        "select st_buffer(geom, %1s) as geo_field from dlm25w.select_fgba_closed(%2s, null)";
    private static final String FG_BR_QUERY =
        "select st_buffer(geom, %1s) as geo_field from dlm25w.select_fgba_open_without_prof(null, null, '%1s') where art = 'o'";
    private static final String FG_BR_QUERY_WITH_ID =
        "select st_buffer(geom, %1s) as geo_field from dlm25w.select_fgba_open_without_prof(%2s, null, '%3s') where art = 'o'";
    private static final String FG_FL_QUERY =
        "select geo_field from dlm25w.fg_ba_fl join geom on (geom = geom.id) where st_intersects(geo_field, '%1s')";
    private static final String SEE_QUERY =
        "select geo_field from dlm25w.sg_see join geom on (geom = geom.id) where st_intersects(geo_field, '%1s')";
    private static final String SEE_KL_QUERY =
        "select geo_field from dlm25w.sg_see_kl join geom on (geom = geom.id) where st_intersects(geo_field, '%1s')";
    private static final String OSTSEE_QUERY =
        "select dlm25w.withoutHoles(geo_field) as geo_field from dlm25w.ezg_mv_ostsee join geom on (geom = geom.id) where st_intersects(geo_field, '%1s')";

    private static final String FG_QUERY_WITHOUT_GEO =
        "select geo_field from dlm25w.fg_ba_gerog join geom on (geom = geom.id) where typ in ('so', 'b_li', 'b_re', 'bn_li', 'bn_re', 'bt_li', 'bt_re')";
    private static final String FG_QUERY_WITHOUT_GEO_WITH_ID =
        "select geo_field from dlm25w.fg_ba_gerog join geom on (geom = geom.id) where typ in ('so', 'b_li', 'b_re', 'bn_li', 'bn_re', 'bt_li', 'bt_re') and ba_cd in (%1s)";
    private static final String FG_BR_QUERY_WITHOUT_GEO =
        "select st_buffer(geom, %1s) as geo_field from dlm25w.select_fgba_open_without_prof(null, null) where art = 'o'";
    private static final String FG_BR_QUERY_WITHOUT_GEO_WITH_ID =
        "select st_buffer(geom, %1s) as geo_field from dlm25w.select_fgba_open_without_prof(%2s, null) where art = 'o'";
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
    private final boolean geschlFg;
    private final double br;
    private final double geschlBr;
    private final boolean fgFl;
    private final boolean see;
    private final boolean seeKl;
    private final boolean ostsee;
    private final String[] baCd;
    private final Integer[] fgBaIdBr;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WkkSearch object.
     *
     * @param  bbox      DOCUMENT ME!
     * @param  fg        DOCUMENT ME!
     * @param  fgBr      DOCUMENT ME!
     * @param  geschlFg  DOCUMENT ME!
     * @param  fgFl      DOCUMENT ME!
     * @param  see       DOCUMENT ME!
     * @param  seeKl     DOCUMENT ME!
     * @param  ostsee    DOCUMENT ME!
     * @param  br        DOCUMENT ME!
     * @param  geschlBr  DOCUMENT ME!
     * @param  baCd      DOCUMENT ME!
     * @param  fgBaIdBr  DOCUMENT ME!
     */
    public PreparedRandstreifenGeoms(final Geometry bbox,
            final boolean fg,
            final boolean fgBr,
            final boolean geschlFg,
            final boolean fgFl,
            final boolean see,
            final boolean seeKl,
            final boolean ostsee,
            final double br,
            final double geschlBr,
            final String[] baCd,
            final Integer[] fgBaIdBr) {
        this.bbox = bbox;
        this.fg = fg;
        this.fgBr = fgBr;
        this.geschlFg = geschlFg;
        this.fgFl = fgFl;
        this.see = see;
        this.seeKl = seeKl;
        this.ostsee = ostsee;
        this.br = br;
        this.geschlBr = geschlBr;
        this.baCd = baCd;
        this.fgBaIdBr = fgBaIdBr;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Collection performServerSearch() {
        final MetaService ms = (MetaService)getActiveLocalServers().get(DOMAIN_NAME);

        if (ms != null) {
            try {
                String tables = null;

                if (fg) {
                    if (baCd == null) {
                        tables = String.format(((bbox == null) ? FG_QUERY_WITHOUT_GEO : FG_QUERY), bbox);
                    } else {
                        if (bbox == null) {
                            tables = String.format(FG_QUERY_WITHOUT_GEO_WITH_ID, toList(baCd));
                        } else {
                            tables = String.format(FG_QUERY_WITH_ID, bbox, toList(baCd));
                        }
                    }
                }

                if (fgBr) {
                    if (fgBaIdBr == null) {
                        if (tables == null) {
                            if (bbox == null) {
                                tables = String.format(FG_BR_QUERY_WITHOUT_GEO, br);
                            } else {
                                tables = String.format(FG_BR_QUERY, br, bbox);
                            }
                        } else {
                            if (bbox == null) {
                                tables += " union "
                                            + String.format(FG_BR_QUERY_WITHOUT_GEO, br);
                            } else {
                                tables += " union "
                                            + String.format(FG_BR_QUERY, br, bbox);
                            }
                        }
                    } else {
                        if (tables == null) {
                            if (bbox == null) {
                                tables = String.format(FG_BR_QUERY_WITHOUT_GEO_WITH_ID, br, toArrayString(fgBaIdBr));
                            } else {
                                tables = String.format(FG_BR_QUERY_WITH_ID, br, toArrayString(fgBaIdBr), bbox);
                            }
                        } else {
                            if (bbox == null) {
                                tables += " union "
                                            + String.format(
                                                FG_BR_QUERY_WITHOUT_GEO_WITH_ID,
                                                br,
                                                toArrayString(fgBaIdBr));
                            } else {
                                tables += " union "
                                            + String.format(FG_BR_QUERY_WITH_ID, br, toArrayString(fgBaIdBr), bbox);
                            }
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

                if (geschlFg) {
                    if (tables == null) {
                        tables = String.format(((baCd == null) ? FG_CLOSED_QUERY : FG_CLOSED_QUERY_WITH_ID),
                                geschlBr,
                                toList(baCd));
                    } else {
                        tables += " union "
                                    + String.format(((baCd == null) ? FG_CLOSED_QUERY : FG_CLOSED_QUERY_WITH_ID),
                                        geschlBr,
                                        toList(baCd));
                    }
                }

                if (see) {
                    if (tables == null) {
                        tables = String.format(((bbox == null) ? SEE_QUERY_WITHOUT_GEO : SEE_QUERY), bbox);
                    } else {
                        tables += " union "
                                    + String.format(((bbox == null) ? SEE_QUERY_WITHOUT_GEO : SEE_QUERY), bbox);
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

                if (ms instanceof DomainServerImpl) {
                    try {
                        final DomainServerImpl serv = (DomainServerImpl)ms;
                        final Connection con = serv.getConnectionPool().getConnection(true);
                        final Statement s = con.createStatement();
                        final ResultSet rs = s.executeQuery(query);
                        final ArrayList<ArrayList> result = serv.collectResults(rs);
                        serv.getConnectionPool().releaseDbConnection(con);
                        return result;
                    } catch (Exception e) {
                        final String msg = "Error during sql statement: "
                                    + query;
                        LOG.error(msg, e);
                        throw new RemoteException(msg, e);
                    }
                } else {
                    final ArrayList<ArrayList> lists = ms.performCustomSearch(query);
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

    /**
     * DOCUMENT ME!
     *
     * @param   baCd  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private String toList(final String[] baCd) {
        if (baCd == null) {
            return "null";
        }
        StringBuffer sb = null;

        for (final String tmp : baCd) {
            if (sb == null) {
                sb = new StringBuffer("'" + tmp + "'");
            } else {
                sb.append(",").append("'").append(tmp).append("'");
            }
        }

        return sb.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param   ids  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private String toArrayString(final Integer[] ids) {
        StringBuffer sb = null;

        for (final int tmp : ids) {
            if (sb == null) {
                sb = new StringBuffer("ARRAY[" + tmp);
            } else {
                sb.append(",").append(tmp);
            }
        }

        sb.append("]");

        return sb.toString();
    }
}
