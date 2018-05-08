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

import de.cismet.cids.custom.helper.SQLFormatter;

import de.cismet.cids.server.search.AbstractCidsServerSearch;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class RouteProblemsCountAndClasses extends AbstractCidsServerSearch {

    //~ Static fields/initializers ---------------------------------------------

    /** LOGGER. */
    private static final transient Logger LOG = Logger.getLogger(RouteProblemsCountAndClasses.class);

    public static final String DOMAIN_NAME = "DLM25W";
    private static final String QUERY_WITH_CLASS =
        "select dlm25w.correction_count_and_names(%1$s, %2$s, %3$s, %4$s, %5$s)";

    //~ Instance fields --------------------------------------------------------

    private String owner;
    private int[] ids;
    private int[] classIds = null;
    private boolean fgBakIds = true;
    private boolean export = false;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WkkSearch object.
     *
     * @param  owner   DOCUMENT ME!
     * @param  ids     DOCUMENT ME!
     * @param  export  DOCUMENT ME!
     */
    public RouteProblemsCountAndClasses(final String owner, final int[] ids, final boolean export) {
        this.owner = owner;
        this.ids = ids;
        this.export = export;
        if (owner != null) {
            this.owner = "'" + owner + "'";
        }
    }

    /**
     * Creates a new RouteProblemsCount object.
     *
     * @param  owner     DOCUMENT ME!
     * @param  ids       DOCUMENT ME!
     * @param  classIds  DOCUMENT ME!
     * @param  fgBakIds  DOCUMENT ME!
     * @param  export    DOCUMENT ME!
     */
    public RouteProblemsCountAndClasses(final String owner,
            final int[] ids,
            final int[] classIds,
            final boolean fgBakIds,
            final boolean export) {
        this.owner = owner;
        this.ids = ids;
        this.classIds = classIds;
        this.fgBakIds = fgBakIds;
        this.export = export;
        if (owner != null) {
            this.owner = "'" + owner + "'";
        }
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Collection performServerSearch() {
        final MetaService ms = (MetaService)getActiveLocalServers().get(DOMAIN_NAME);

        if (ms != null) {
            try {
                final ArrayList<ArrayList> lists = ms.performCustomSearch(String.format(
                            QUERY_WITH_CLASS,
                            owner,
                            SQLFormatter.createSqlArrayString(ids),
                            SQLFormatter.createSqlArrayString(classIds),
                            String.valueOf(fgBakIds),
                            export));
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
