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
import Sirius.server.sql.PreparableStatement;

import org.apache.log4j.Logger;

import org.openide.util.Exceptions;

import java.rmi.RemoteException;

import java.sql.Types;

import java.util.ArrayList;
import java.util.Collection;

import de.cismet.cids.server.search.AbstractCidsServerSearch;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class RemoveOldLocks extends AbstractCidsServerSearch {

    //~ Static fields/initializers ---------------------------------------------

    /** LOGGER. */
    private static final transient Logger LOG = Logger.getLogger(RemoveOldLocks.class);

    private static final String QUERY = "select dlm25w.deleteLocksFromUser(?, ?)";
    public static final String DOMAIN_NAME = "DLM25W";

    //~ Instance fields --------------------------------------------------------

    private String owner;
    private String computerName;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WkkSearch object.
     *
     * @param  owner         DOCUMENT ME!
     * @param  computerName  DOCUMENT ME!
     */
    public RemoveOldLocks(final String owner, final String computerName) {
        this.owner = owner;
        this.computerName = computerName;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Collection performServerSearch() {
        final MetaService ms = (MetaService)getActiveLocalServers().get(DOMAIN_NAME);

        if (ms != null) {
            try {
                final PreparableStatement ps = new PreparableStatement(
                        QUERY,
                        new int[] { Types.VARCHAR, Types.VARCHAR });
                ps.setObjects(owner, computerName);
                ms.performCustomSearch(ps);
            } catch (RemoteException ex) {
                LOG.error(ex.getMessage(), ex);
            }
        } else {
            LOG.error("active local server not found"); // NOI18N
        }

        return null;
    }
}
