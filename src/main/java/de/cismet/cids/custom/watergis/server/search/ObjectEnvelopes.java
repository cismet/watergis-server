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
public class ObjectEnvelopes extends AbstractCidsServerSearch {

    //~ Static fields/initializers ---------------------------------------------

    /** LOGGER. */
    private static final transient Logger LOG = Logger.getLogger(ObjectEnvelopes.class);
    public static final String DOMAIN_NAME = "DLM25W";
    private static final String QUERY_KREIS = "Select \n"
                + "st_asBinary(st_envelope(geom.geo_field)) as geom,\n"
                + "dlm25w.vw_dvg_kreis.kreis_name, dlm25w.vw_dvg_kreis.id \n"
                + "from dlm25w.vw_dvg_kreis \n"
                + "join geom on (geom = geom.id) \n";
    private static final String QUERY_AMT = "Select \n"
                + "st_asBinary(st_envelope(geom.geo_field)) as geom,\n"
                + "dlm25w.vw_dvg_amt.amt_name, dlm25w.vw_dvg_amt.id \n"
                + "from dlm25w.vw_dvg_amt \n"
                + "join geom on (geom = geom.id) \n";
    private static final String QUERY_WBV = "Select \n"
                + "st_asBinary(st_envelope(geom.geo_field)) as geom,\n"
                + "dlm25w.ezg_mv_wbv.name, dlm25w.ezg_mv_wbv.id \n"
                + "from dlm25w.ezg_mv_wbv \n"
                + "join geom on (geom = geom.id) \n";

    //~ Enums ------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public static enum ObjectType {

        //~ Enum constants -----------------------------------------------------

        AMT, KREIS, WBV
    }

    //~ Instance fields --------------------------------------------------------

    private final ObjectType type;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WkkSearch object.
     *
     * @param  type  whereCondition owner DOCUMENT ME!
     */
    public ObjectEnvelopes(final ObjectType type) {
        this.type = type;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Collection performServerSearch() {
        final MetaService ms = (MetaService)getActiveLocalServers().get(DOMAIN_NAME);

        if (ms != null) {
            try {
                String query;

                if (type.equals(ObjectType.KREIS)) {
                    query = QUERY_KREIS;
                } else if (type.equals(ObjectType.AMT)) {
                    query = QUERY_AMT;
                } else {
                    query = QUERY_WBV;
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
