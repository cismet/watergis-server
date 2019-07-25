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
public class AllPunktObjects extends AbstractCidsServerSearch {

    //~ Static fields/initializers ---------------------------------------------

    /** LOGGER. */
    private static final transient Logger LOG = Logger.getLogger(AllPunktObjects.class);

    public static final String DOMAIN_NAME = "DLM25W";
    private static final String QUERY = "select von.route, ba.ba_cd, von.wert\n"
                + "from dlm25w.%1$s m \n"
                + "join dlm25w.fg_ba_punkt von on (m.ba_st = von.id)\n"
                + "join dlm25w.fg_ba ba on (von.route = ba.id)\n"
                + "where (%2$s is null or von.route = any(%2$s))\n"
                + "order by ba.ba_cd, von.wert";

    //~ Enums ------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public enum Table {

        //~ Enum constants -----------------------------------------------------

        wr_wbu_ben, wr_wbu_aus, mn_ow_pegel, fg_ba_scha, fg_ba_wehr, fg_ba_schw, fg_ba_anlp, fg_ba_kr, fg_ba_ea,
        fg_ba_foto
    }

    //~ Instance fields --------------------------------------------------------

    private final Table table;
    private final int[] gew;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WkkSearch object.
     *
     * @param  table  DOCUMENT ME!
     * @param  gew    DOCUMENT ME!
     */
    public AllPunktObjects(final Table table, final int[] gew) {
        this.table = table;
        this.gew = gew;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Collection performServerSearch() {
        final MetaService ms = (MetaService)getActiveLocalServers().get(DOMAIN_NAME);

        if (ms != null) {
            try {
                final ArrayList<ArrayList> lists = ms.performCustomSearch(String.format(
                            QUERY,
                            table.toString(),
                            SQLFormatter.createSqlArrayString(gew)));
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
