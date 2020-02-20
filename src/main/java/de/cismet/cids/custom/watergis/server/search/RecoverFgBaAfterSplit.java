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

import Sirius.server.middleware.impls.domainserver.DomainServerImpl;
import Sirius.server.middleware.interfaces.domainserver.MetaService;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Collection;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class RecoverFgBaAfterSplit extends WritableSearch {

    //~ Static fields/initializers ---------------------------------------------

    /** LOGGER. */
    private static final transient Logger LOG = Logger.getLogger(MergeFgBakGwk.class);

    private static final String QUERY = "select dlm25w.recover_fg_ba_after_split(?, ?);"; // NOI18N
    public static final String DOMAIN_NAME = "DLM25W";

    //~ Instance fields --------------------------------------------------------

    private final int fgBakId;
    private final int[] statIds;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WkkSearch object.
     *
     * @param  fgBakId  owner DOCUMENT ME!
     * @param  statIds  DOCUMENT ME!
     */
    public RecoverFgBaAfterSplit(final int fgBakId, final int[] statIds) {
        this.fgBakId = fgBakId;
        this.statIds = statIds;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Collection performServerSearch() {
        final MetaService ms = (MetaService)getActiveLocalServers().get(DOMAIN_NAME);

        if (ms != null) {
            try {
                final Object[] intArray = new Object[statIds.length];

                for (int i = 0; i < statIds.length; ++i) {
                    intArray[i] = statIds[i];
                }

                final Connection connection = DomainServerImpl.getServerInstance().getConnectionPool().getConnection();
                final PreparedStatement merge = connection.prepareStatement(QUERY);
                merge.setArray(1, connection.createArrayOf("integer", intArray));
                merge.setInt(2, fgBakId);
                final ResultSet rs = merge.executeQuery();

                final ArrayList<ArrayList> lists = collectResults(rs);
                return lists;
            } catch (SQLException ex) {
                LOG.error(ex.getMessage(), ex);
            }
        } else {
            LOG.error("active local server not found"); // NOI18N
        }

        return null;
    }
}
