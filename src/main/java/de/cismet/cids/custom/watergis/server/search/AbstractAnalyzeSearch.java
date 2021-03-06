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
public abstract class AbstractAnalyzeSearch extends AbstractCidsServerSearch {

    //~ Static fields/initializers ---------------------------------------------

    /** LOGGER. */
    private static final transient Logger LOG = Logger.getLogger(AbstractAnalyzeSearch.class);

    public static final String DOMAIN_NAME = "DLM25W";

    //~ Instance fields --------------------------------------------------------

    private final String dbQuery;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WkkSearch object.
     *
     * @param  owner   DOCUMENT ME!
     * @param  ids     DOCUMENT ME!
     * @param  export  the value of export
     */
    public AbstractAnalyzeSearch(final String owner, final int[] ids, final boolean export) {
        dbQuery = createQuery(owner, ids, export);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   owner   DOCUMENT ME!
     * @param   ids     DOCUMENT ME!
     * @param   export  the value of export
     *
     * @return  the java.lang.String
     */
    protected abstract String createQuery(final String owner, final int[] ids, boolean export);

    /**
     * DOCUMENT ME!
     *
     * @param   ids  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected String createSqlString(final int[] ids) {
        return SQLFormatter.createSqlArrayString(ids);
    }

    @Override
    public Collection performServerSearch() {
        final MetaService ms = (MetaService)getActiveLocalServers().get(DOMAIN_NAME);

        if (ms != null) {
            try {
                final String query = String.format(dbQuery);
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
