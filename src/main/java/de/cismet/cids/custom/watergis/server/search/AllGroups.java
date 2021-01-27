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
import java.util.List;

import de.cismet.cids.server.search.AbstractCidsServerSearch;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class AllGroups extends AbstractCidsServerSearch {

    //~ Static fields/initializers ---------------------------------------------

    /** LOGGER. */
    private static final transient Logger LOG = Logger.getLogger(AllGroups.class);

    public static final String DOMAIN_NAME = "DLM25W";
    private static final String QUERY = "select name from cs_ug where name is not null order by name";

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WkkSearch object.
     */
    public AllGroups() {
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Collection performServerSearch() {
        final MetaService ms = (MetaService)getActiveLocalServers().get(DOMAIN_NAME);

        if (ms != null) {
            try {
                final List<String> result = new ArrayList<String>();
                final ArrayList<ArrayList> lists = ms.performCustomSearch(QUERY);

                if ((lists != null) && (lists.size() > 0)) {
                    for (final ArrayList tmp : lists) {
                        if ((tmp != null) && (tmp.size() > 0)) {
                            result.add((String)tmp.get(0));
                        }
                    }
                }

                return result;
            } catch (RemoteException ex) {
                LOG.error(ex.getMessage(), ex);
            }
        } else {
            LOG.error("active local server not found"); // NOI18N
        }

        return null;
    }
}
