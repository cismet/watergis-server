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
import Sirius.server.middleware.types.MetaObject;

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
public class DetermineClosestRoute extends AbstractCidsServerSearch {

    //~ Static fields/initializers ---------------------------------------------

    /** LOGGER. */
    private static final transient Logger LOG = Logger.getLogger(DetermineClosestRoute.class);

    private static final String SUITABLE_ROUTE_QUERY = "select %2$s.%1$s from %2$s "
                + "join geom on (geom = geom.id) order by st_distance(geo_field, '%3$s') asc limit 1;";
    public static final String DOMAIN_NAME = "DLM25W";

    //~ Instance fields --------------------------------------------------------

    private final Integer routeClass;
    private final String primaryKey;
    private final String tableName;
    private final String firstPointAsText;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DetermineClosestRoute object.
     *
     * @param  routeClass        DOCUMENT ME!
     * @param  primaryKey        DOCUMENT ME!
     * @param  tableName         DOCUMENT ME!
     * @param  firstPointAsText  DOCUMENT ME!
     */
    public DetermineClosestRoute(final Integer routeClass,
            final String primaryKey,
            final String tableName,
            final String firstPointAsText) {
        this.routeClass = routeClass;
        this.primaryKey = primaryKey;
        this.tableName = tableName;
        this.firstPointAsText = firstPointAsText;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Collection performServerSearch() {
        final MetaService ms = (MetaService)getActiveLocalServers().get(DOMAIN_NAME);

        if (ms != null) {
            try {
                final String query = String.format(SUITABLE_ROUTE_QUERY, primaryKey, tableName, firstPointAsText);
                final ArrayList<ArrayList> lists = ms.performCustomSearch(query);

                if ((lists != null) && (lists.size() > 0)) {
                    final ArrayList subList = lists.get(0);
                    if ((subList != null) && (subList.size() > 0)) {
                        final Object o = subList.get(0);

                        if (o instanceof Integer) {
                            final MetaObject route = ms.getMetaObject(getUser(), (Integer)o, routeClass);

                            final ArrayList<MetaObject> result = new ArrayList<MetaObject>();
                            result.add(route);

                            return result;
                        }
                    }
                }
            } catch (RemoteException ex) {
                LOG.error(ex.getMessage(), ex);
            }
        } else {
            LOG.error("active local server not found"); // NOI18N
        }

        return null;
    }
}
