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
import Sirius.server.middleware.types.MetaClass;
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
public class QpWwgrByNr extends AbstractCidsServerSearch {

    //~ Static fields/initializers ---------------------------------------------

    /** LOGGER. */
    private static final transient Logger LOG = Logger.getLogger(QpWwgrByNr.class);

    public static final String DOMAIN_NAME = "DLM25W";
    private static final String QUERY =
        "Select gr.id from dlm25w.qp q join dlm25w.k_ww_gr gr on (q.ww_gr = gr.id) where qp_nr = %s";

    //~ Instance fields --------------------------------------------------------

    private final String qpNr;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WkkSearch object.
     *
     * @param  qpNr  DOCUMENT ME!
     */
    public QpWwgrByNr(final String qpNr) {
        this.qpNr = qpNr;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Collection performServerSearch() {
        final MetaService ms = (MetaService)getActiveLocalServers().get(DOMAIN_NAME);

        if (ms != null) {
            try {
                final MetaClass mc = ms.getClassByTableName(getUser(), "dlm25w.k_ww_gr");
                final ArrayList<ArrayList> lists = ms.performCustomSearch(String.format(QUERY, qpNr));

                if ((lists != null) && (lists.size() > 0) && (lists.get(0) != null) && (lists.get(0).size() > 0)) {
                    final MetaObject route = ms.getMetaObject(getUser(), (Integer)lists.get(0).get(0), mc.getId());

                    final ArrayList<MetaObject> result = new ArrayList<MetaObject>();
                    result.add(route);

                    return result;
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
