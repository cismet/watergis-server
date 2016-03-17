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
public class FgBakCount extends AbstractCidsServerSearch {

    //~ Static fields/initializers ---------------------------------------------

    /** LOGGER. */
    private static final transient Logger LOG = Logger.getLogger(FgBakCount.class);

    public static final String DOMAIN_NAME = "DLM25W";
    private static final String QUERY_WITHOUT_OWNER =
        "select count(*) from dlm25w.fg_bak bak join dlm25w.fg_ba ba on (ba.bak_id = bak.id) where (%1$s is null or ba.id = any(%1$s))";
    private static final String QUERY =
        "select count(*) from dlm25w.fg_bak bak join dlm25w.fg_ba ba on (ba.bak_id = bak.id) "
                + " left join dlm25w.k_ww_gr gr on (gr.id = bak.ww_gr) where (%2$s is null or ba.id = any(%2$s)) and gr.owner = '%1$s'";
    private static final String QUERY_BAK_WITHOUT_OWNER =
        "select count(*) from dlm25w.fg_bak bak where (%1$s is null or bak.id = any(%1$s))";
    private static final String QUERY_BAK = "select count(*) from dlm25w.fg_bak bak "
                + " left join dlm25w.k_ww_gr gr on (gr.id = bak.ww_gr) where (%2$s is null or bak.id = any(%2$s)) and gr.owner = '%1$s'";

    //~ Instance fields --------------------------------------------------------

    private final String owner;
    private final int[] baIds;
    private final int[] bakIds;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WkkSearch object.
     *
     * @param  owner   DOCUMENT ME!
     * @param  baIds   ids DOCUMENT ME!
     * @param  bakIds  DOCUMENT ME!
     */
    public FgBakCount(final String owner, final int[] baIds, final int[] bakIds) {
        this.owner = owner;
        this.baIds = baIds;
        this.bakIds = bakIds;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Collection performServerSearch() {
        final MetaService ms = (MetaService)getActiveLocalServers().get(DOMAIN_NAME);

        if (ms != null) {
            try {
                String query = null;

                if (baIds != null) {
                    query = ((owner == null)
                            ? String.format(QUERY_WITHOUT_OWNER, SQLFormatter.createSqlArrayString(baIds))
                            : String.format(QUERY, owner, SQLFormatter.createSqlArrayString(baIds)));
                } else {
                    query = ((owner == null)
                            ? String.format(QUERY_BAK_WITHOUT_OWNER, SQLFormatter.createSqlArrayString(bakIds))
                            : String.format(QUERY_BAK, owner, SQLFormatter.createSqlArrayString(bakIds)));
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
