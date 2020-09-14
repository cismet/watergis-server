/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.watergisserver.cidslayer;

import Sirius.server.middleware.types.MetaClass;
import Sirius.server.newuser.User;

import java.util.HashMap;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class QpGafPpCidsLayer extends WatergisDefaultCidsLayer {

    //~ Static fields/initializers ---------------------------------------------

    private static final HashMap<String, String> CATALOGUE_NAME_MAP = new HashMap<String, String>();

    static {
        CATALOGUE_NAME_MAP.put("rk", "rk");
        CATALOGUE_NAME_MAP.put("bk", "bk");
        CATALOGUE_NAME_MAP.put("kz", "kz");
    }

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new VwDvgStaluCidsLayer object.
     *
     * @param  mc    DOCUMENT ME!
     * @param  user  DOCUMENT ME!
     */
    public QpGafPpCidsLayer(final MetaClass mc, final User user) {
        super(
            mc,
            false,
            false,
            CATALOGUE_NAME_MAP,
            user);
    }

    //~ Methods ----------------------------------------------------------------

// @Override
// protected String addLeftJoin(StringBuilder joins, String table, String onClauseLeftSide, String onClauseRightSide) {
//        return super.addLeftJoin(joins, table, onClauseLeftSide, onClauseRightSide); //To change body of generated methods, choose Tools | Templates.
//    }

    @Override
    protected String getFieldRestriction(final String column) {
        if (column.equals("dlm25w.qp.bemerkung")) {
            if (isFullGUAccessAllowed()) {
                return null;
            } else {
                return "upl_name = '" + user.getName() + "'";
            }
        }

        return null;
    }

    @Override
    public String getRestriction() {
        if ((user != null) && user.getUserGroup().getName().equals("Administratoren")) {
            // the admin has no restrictions
            return null;
        } else {
            String rest =
                "(((select f.freigabe from dlm25w.qp p join dlm25w.k_freigabe f on (p.freigabe = f.id) where p.qp_nr = dlm25w.qp_gaf_pp.qp_nr) = 'uploader' and (select p.upl_name from dlm25w.qp p where p.qp_nr = dlm25w.qp_gaf_pp.qp_nr) = '"
                        + user.getName()
                        + "') or (select f.freigabe from dlm25w.qp p join dlm25w.k_freigabe f on (p.freigabe = f.id) where p.qp_nr = dlm25w.qp_gaf_pp.qp_nr) is null or (select f.freigabe from dlm25w.qp p join dlm25w.k_freigabe f on (p.freigabe = f.id) where p.qp_nr = dlm25w.qp_gaf_pp.qp_nr) = 'frei')";

            if ((user != null)
                        && (user.getUserGroup().getName().contains("lu")
                            || user.getUserGroup().getName().contains("wbv")
                            || user.getUserGroup().getName().contains("uwb")
                            || user.getUserGroup().getName().contains("wsa")
                            || user.getUserGroup().getName().contains("stalu"))) {
                rest = "("
                            + rest
                            + " or ((select f.freigabe from dlm25w.qp p join dlm25w.k_freigabe f on (p.freigabe = f.id) where p.qp_nr = dlm25w.qp_gaf_pp.qp_nr) = 'wawi'))";
            }

            return rest;
        }
    }
}
