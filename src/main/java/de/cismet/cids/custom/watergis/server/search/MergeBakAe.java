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
import Sirius.server.sql.PreparableStatement;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

import java.util.ArrayList;
import java.util.Collection;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class MergeBakAe extends MergeSearch {

    //~ Static fields/initializers ---------------------------------------------

    /** LOGGER. */
    private static final transient Logger LOG = Logger.getLogger(MergeBakAe.class);

    private static final String QUERY_BY_ID = "select dlm25w.merge_fg_bak_ae_by_id(?);"; // NOI18N
    public static final String DOMAIN_NAME = "DLM25W";

    //~ Instance fields --------------------------------------------------------

    private Integer bakId = null;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WkkSearch object.
     *
     * @param  owner  DOCUMENT ME!
     */
    public MergeBakAe(final String owner) {
        super(owner);
        QUERY = "select dlm25w.merge_fg_bak_ae(?);";                  // NOI18N
    }

    /**
     * Creates a new WkkSearch object.
     *
     * @param  bakId  owner DOCUMENT ME!
     */
    public MergeBakAe(final Integer bakId) {
        super(null);
        this.bakId = bakId;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Collection performServerSearch() {
        final MetaService ms = (MetaService)getActiveLocalServers().get(DOMAIN_NAME);

        if (ms != null) {
            try {
                if (bakId != null) {
                    final PreparableStatement ps = new PreparableStatement(
                            QUERY_BY_ID,
                            new int[] { Types.INTEGER });
                    ps.setObjects(bakId);
                    final ArrayList<ArrayList> lists = ms.performCustomSearch(ps);

                    return lists;
                } else {
                    return super.performServerSearch();
                }
            } catch (Exception ex) {
                LOG.error(ex.getMessage(), ex);
            }
        } else {
            LOG.error("active local server not found"); // NOI18N
        }

        return null;
    }
}
