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
public class AllRoutes extends AbstractCidsServerSearch {

    //~ Static fields/initializers ---------------------------------------------

    /** LOGGER. */
    private static final transient Logger LOG = Logger.getLogger(AllRoutes.class);

    public static final String DOMAIN_NAME = "DLM25W";
    private static final String QUERY = "Select \n"
                + "dlm25w.fg_ba.ba_cd, dlm25w.fg_ba.id\n"
                + "from dlm25w.fg_ba \n"
                + "ORDER BY ba_cd asc";
//    private static final String QUERY_WITH_RESTRICTION = "select v.route from \n" +
//"dlm25w.fg_ba_gmd g\n" +
//"join dlm25w.fg_ba_linie l on (g.ba_st = l.id)\n" +
//"join dlm25w.fg_ba_punkt v on (l.von = v.id)\n" +
//"where  g.id = any() \n"
//                + "ORDER BY fg_ba_id asc";

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WkkSearch object.
     */
    public AllRoutes() {
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Collection performServerSearch() {
        final MetaService ms = (MetaService)getActiveLocalServers().get(DOMAIN_NAME);

        if (ms != null) {
            try {
                final ArrayList<ArrayList> lists = ms.performCustomSearch(QUERY);
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
