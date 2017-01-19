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
public class AllLineObjects extends AbstractCidsServerSearch {

    //~ Static fields/initializers ---------------------------------------------

    /** LOGGER. */
    private static final transient Logger LOG = Logger.getLogger(AllLineObjects.class);

    public static final String DOMAIN_NAME = "DLM25W";
    private static final String QUERY = "select ba.id, st_length(geo_field), ba.ba_cd, von.wert, bis.wert, von.route\n"
                + "from dlm25w.%1$s m \n"
                + "join dlm25w.fg_ba_linie l on (m.ba_st = l.id)\n"
                + "join dlm25w.fg_ba_punkt von on (l.von = von.id)\n"
                + "join dlm25w.fg_ba_punkt bis on (l.bis = bis.id)\n"
                + "join dlm25w.fg_ba ba on (von.route = ba.id)\n"
                + "join geom g on (g.id = l.geom)\n"
                + "where (%2$s is null or von.route = any(%2$s))\n"
                + "order by ba_cd, least(von.wert, bis.wert)";

    //~ Enums ------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public enum Table {

        //~ Enum constants -----------------------------------------------------

        fg_ba_gb, fg_ba_prof, fg_ba_sbef, fg_ba_ubef, fg_ba_bbef, fg_ba_sb, fg_ba_rl, fg_ba_d, fg_ba_due, fg_ba_anll,
        fg_ba_deich, fg_ba_ughz, fg_ba_leis, fg_ba_tech
    }

    //~ Instance fields --------------------------------------------------------

    protected final Table table;
    protected final int[] gew;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WkkSearch object.
     *
     * @param  table  DOCUMENT ME!
     * @param  gew    DOCUMENT ME!
     */
    public AllLineObjects(final Table table, final int[] gew) {
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
