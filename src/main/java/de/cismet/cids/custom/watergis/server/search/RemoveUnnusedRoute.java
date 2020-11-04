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
import Sirius.server.sql.PreparableStatement;

import com.vividsolutions.jts.geom.Geometry;

import org.apache.log4j.Logger;

import java.rmi.RemoteException;

import java.sql.Types;

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
public class RemoveUnnusedRoute extends AbstractCidsServerSearch {

    //~ Static fields/initializers ---------------------------------------------

    /** LOGGER. */
    private static final transient Logger LOG = Logger.getLogger(RemoveUnnusedRoute.class);

    public static final String DOMAIN_NAME = "DLM25W";
    public static final int FG_BA = 1;
    public static final int FG_LA = 2;
    public static final int FG_BAK = 3;
    public static final int FG_LAK = 4;
    public static final int SU = 5;
    public static final int FG_BA_DUV = 6;
    public static final int SG_UMRING = 7;
    private static final String QUERY_FGBA = "select dlm25w.remove_unused_fgbapoints(?)";
    private static final String QUERY_FGLA = "select dlm25w.remove_unused_fglapoints(?)";
    private static final String QUERY_FGBAK = "select dlm25w.remove_unused_fgbakpoints(?)";
    private static final String QUERY_FGLAK = "select dlm25w.remove_unused_fglakpoints(?)";
    private static final String QUERY_SU = "select dlm25w.remove_unused_supoints(?)";
    private static final String QUERY_FGBADUV = "select duv.remove_unused_fgbaduvpoints(?)";
    private static final String QUERY_SGUMRING = "select duv.remove_unused_sgumringpoints(?)";

    //~ Instance fields --------------------------------------------------------

    private final int id;
    private final int routeType;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new PhotoGetPhotoNumber object.
     *
     * @param  id         fgBak geom DOCUMENT ME!
     * @param  routeType  DOCUMENT ME!
     */
    public RemoveUnnusedRoute(final int id, final int routeType) {
        this.id = id;
        this.routeType = routeType;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Collection performServerSearch() {
        final MetaService ms = (MetaService)getActiveLocalServers().get(DOMAIN_NAME);

        if (ms != null) {
            try {
                final PreparableStatement ps;

                switch (routeType) {
                    case FG_BA: {
                        ps = new PreparableStatement(QUERY_FGBA, new int[] { Types.INTEGER });
                        break;
                    }
                    case FG_BAK: {
                        ps = new PreparableStatement(QUERY_FGBAK, new int[] { Types.INTEGER });
                        break;
                    }
                    case FG_LA: {
                        ps = new PreparableStatement(QUERY_FGLA, new int[] { Types.INTEGER });
                        break;
                    }
                    case FG_LAK: {
                        ps = new PreparableStatement(QUERY_FGLAK, new int[] { Types.INTEGER });
                        break;
                    }
                    case SU: {
                        ps = new PreparableStatement(QUERY_SU, new int[] { Types.INTEGER });
                        break;
                    }
                    case FG_BA_DUV: {
                        ps = new PreparableStatement(QUERY_FGBADUV, new int[] { Types.INTEGER });
                        break;
                    }
                    case SG_UMRING: {
                        ps = new PreparableStatement(QUERY_SGUMRING, new int[] { Types.INTEGER });
                        break;
                    }
                    default: {
                        ps = new PreparableStatement(QUERY_FGBA, new int[] { Types.INTEGER });
                        break;
                    }
                }
                ps.setObjects(id);
                final ArrayList<ArrayList> lists = ms.performCustomSearch(ps);
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
