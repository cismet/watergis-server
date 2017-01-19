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
 * Checks, if the given ba_cd codes are unique.
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class UniquenessCheck extends AbstractCidsServerSearch {

    //~ Static fields/initializers ---------------------------------------------

    /** LOGGER. */
    private static final transient Logger LOG = Logger.getLogger(UniquenessCheck.class);

    public static final String DOMAIN_NAME = "DLM25W";
    private static final String QUERY_CONDITION = "(id <> %s and %s = '%s')";
    private static final String QUERY = "select %s from dlm25w.%s ";

    //~ Instance fields --------------------------------------------------------

    private final Map<Integer, String> valueMap;
    private final String field;
    private final String table;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WkkSearch object.
     *
     * @param  valueMap  baCdMap DOCUMENT ME!
     * @param  field     DOCUMENT ME!
     * @param  table     DOCUMENT ME!
     */
    public UniquenessCheck(final Map<Integer, String> valueMap, final String field, final String table) {
        this.valueMap = valueMap;
        this.field = field;
        this.table = table;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Collection performServerSearch() {
        final MetaService ms = (MetaService)getActiveLocalServers().get(DOMAIN_NAME);

        if (ms != null) {
            try {
                boolean first = true;
                String query = String.format(QUERY, field, table);

                for (final Integer id : valueMap.keySet()) {
                    final String value = valueMap.get(id);

                    if (first) {
                        query += " WHERE ";
                        first = false;
                    } else {
                        query += " OR ";
                    }

                    query += String.format(QUERY_CONDITION, id, field, value);
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
