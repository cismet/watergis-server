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
public class RouteProblemsCount extends AbstractCidsServerSearch {

    //~ Static fields/initializers ---------------------------------------------

    /** LOGGER. */
    private static final transient Logger LOG = Logger.getLogger(RouteProblemsCount.class);

    public static final String DOMAIN_NAME = "DLM25W";
//    private static final String QUERY = "select coalesce(\n"
//                + "(select sum(cs_getNumberOfRows(invalidBakLinesByClass(cs_class.id, $user))) \n"
//                + "from cs_attr join cs_class on (cs_attr.class_id = cs_class.id) where foreign_key_references_to = (select id from cs_class where name = 'fg_bak_linie') and  (%1$s is null or ba.id = any(%1$s))\n"
//                + "), 0\n"
//                + ")\n"
//                + "+ coalesce(\n"
//                + "(select sum(cs_getNumberOfRows(invalidBaLinesByClass(cs_class.id, $user))) \n"
//                + "from cs_attr join cs_class on (cs_attr.class_id = cs_class.id) where foreign_key_references_to = (select id from cs_class where name = 'fg_ba_linie') and  (%1$s is null or ba.id = any(%1$s))\n"
//                + "), 0\n"
//                + ")\n"
//                + "+ coalesce(\n"
//                + "(select sum(cs_getNumberOfRows(invalidLakLinesByClass(cs_class.id, $user))) \n"
//                + "from cs_attr join cs_class on (cs_attr.class_id = cs_class.id) where foreign_key_references_to = (select id from cs_class where name = 'fg_lak_linie') and  (%1$s is null or ba.id = any(%1$s))\n"
//                + "), 0\n"
//                + ")\n"
//                + "+ coalesce(\n"
//                + "(select sum(cs_getNumberOfRows(invalidBakStationsByClass(cs_class.id, $user))) \n"
//                + "from cs_attr join cs_class on (cs_attr.class_id = cs_class.id) where foreign_key_references_to = (select id from cs_class where name = 'fg_bak_punkt') and cs_class.id not in ((select id from cs_class where name = 'fg_bak_linie')) and  (%1$s is null or ba.id = any(%1$s))\n"
//                + "), 0\n"
//                + ")\n"
//                + "+ coalesce(\n"
//                + "(select sum(cs_getNumberOfRows(invalidBaStationsByClass(cs_class.id, $user))) \n"
//                + "from cs_attr join cs_class on (cs_attr.class_id = cs_class.id) where foreign_key_references_to = (select id from cs_class where name = 'fg_ba_punkt') and cs_class.id not in ((select id from cs_class where name = 'fg_ba_linie')) and  (%1$s is null or ba.id = any(%1$s))\n"
//                + "), 0\n"
//                + ")\n"
//                + "+ coalesce(\n"
//                + "(select sum(cs_getNumberOfRows(invalidLakStationsByClass(cs_class.id, $user))) \n"
//                + "from cs_attr join cs_class on (cs_attr.class_id = cs_class.id) where foreign_key_references_to = (select id from cs_class where name = 'fg_lak_punkt') and cs_class.id not in ((select id from cs_class where name = 'fg_lak_linie')) and  (%1$s is null or ba.id = any(%1$s))\n"
//                + "), 0\n"
//                + ")\n";
    private static final String QUERY = "select dlm25w.correction_count(%1$s, %2$s)";
    private static final String QUERY_WITH_CLASS = "select dlm25w.correction_count(%1$s, %2$s, %3$s, %4$s)";

    //~ Instance fields --------------------------------------------------------

    private final String owner;
    private int[] ids;
    private int[] classIds = null;
    private boolean fgBakIds = true;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WkkSearch object.
     *
     * @param  owner  DOCUMENT ME!
     * @param  ids    DOCUMENT ME!
     */
    public RouteProblemsCount(final String owner, final int[] ids) {
        this.owner = owner;
        this.ids = ids;
    }

    /**
     * Creates a new RouteProblemsCount object.
     *
     * @param  owner     DOCUMENT ME!
     * @param  ids       DOCUMENT ME!
     * @param  classIds  DOCUMENT ME!
     * @param  fgBakIds  DOCUMENT ME!
     */
    public RouteProblemsCount(final String owner, final int[] ids, final int[] classIds, final boolean fgBakIds) {
        this.owner = owner;
        this.ids = ids;
        this.classIds = classIds;
        this.fgBakIds = fgBakIds;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Collection performServerSearch() {
        final MetaService ms = (MetaService)getActiveLocalServers().get(DOMAIN_NAME);

        if (ms != null) {
            try {
                if (classIds != null) {
                    final ArrayList<ArrayList> lists = ms.performCustomSearch(String.format(
                                QUERY_WITH_CLASS,
                                owner,
                                SQLFormatter.createSqlArrayString(ids),
                                SQLFormatter.createSqlArrayString(classIds),
                                String.valueOf(fgBakIds)));
                    return lists;
                } else {
                    final ArrayList<ArrayList> lists = ms.performCustomSearch(String.format(
                                QUERY,
                                owner,
                                SQLFormatter.createSqlArrayString(ids)));
                    return lists;
                }
            } catch (RemoteException ex) {
                LOG.error(ex.getMessage(), ex);
            }
        } else {
            LOG.error("active local server not found"); // NOI18N
        }

        return null;
    }
}
