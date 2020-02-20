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

import org.apache.log4j.Logger;

import java.rmi.RemoteException;

import java.sql.Types;

import java.util.Collection;

import de.cismet.cids.server.search.AbstractCidsServerSearch;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class CleanupCreatedObjectTable extends AbstractCidsServerSearch {

    //~ Static fields/initializers ---------------------------------------------

    /** LOGGER. */
    private static final transient Logger LOG = Logger.getLogger(CleanupCreatedObjectTable.class);

    private static final String QUERY = "select dlm25w.delete_inclomplete_object_list(?, ?)";
    public static final String DOMAIN_NAME = "DLM25W";

    //~ Instance fields --------------------------------------------------------

    private final String user;
    private final int classId;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WkkSearch object.
     *
     * @param  user     owner DOCUMENT ME!
     * @param  classId  computerName DOCUMENT ME!
     */
    public CleanupCreatedObjectTable(final String user, final Integer classId) {
        this.user = user;
        this.classId = classId;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Collection performServerSearch() {
        final MetaService ms = (MetaService)getActiveLocalServers().get(DOMAIN_NAME);

        if (ms != null) {
            try {
                final PreparableStatement ps = new PreparableStatement(
                        QUERY,
                        new int[] { Types.INTEGER, Types.VARCHAR });
                ps.setObjects(classId, user);
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
