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
public class CalculateFgLa extends AbstractCidsServerSearch {

    //~ Static fields/initializers ---------------------------------------------

    /** LOGGER. */
    private static final transient Logger LOG = Logger.getLogger(CalculateFgLa.class);

    public static final String DOMAIN_NAME = "DLM25W";
//    private static final String QUERY = "select * from dlm25w.calc_la_value('%1$s', %2$s)";
    private static final String QUERY = "select la_cd, la_st from dlm25w.calc_la_value(?, ?)";

    //~ Instance fields --------------------------------------------------------

    private final String baCd;
    private final double baSt;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WkkSearch object.
     *
     * @param  baCd  DOCUMENT ME!
     * @param  baSt  DOCUMENT ME!
     */
    public CalculateFgLa(final String baCd, final double baSt) {
        this.baCd = baCd;
        this.baSt = baSt;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Collection performServerSearch() {
        final MetaService ms = (MetaService)getActiveLocalServers().get(DOMAIN_NAME);

        if (ms != null) {
            try {
                final PreparableStatement ps = new PreparableStatement(
                        QUERY,
                        new int[] { Types.VARCHAR, Types.DOUBLE });
                ps.setObjects(baCd, baSt);
//                final ArrayList<ArrayList> lists = ms.performCustomSearch(String.format(QUERY, baCd, baSt));
                final ArrayList<ArrayList> lists = ms.performCustomSearch(ps);
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
