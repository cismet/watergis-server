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
public class QpModellCidsLayer extends WatergisDefaultCidsLayer {

    //~ Static fields/initializers ---------------------------------------------

    private static final HashMap<String, String> CATALOGUE_NAME_MAP = new HashMap<String, String>();

    static {
        CATALOGUE_NAME_MAP.put("m_traeger", "praefix");
        CATALOGUE_NAME_MAP.put("la_gn", "la_gn");
        CATALOGUE_NAME_MAP.put("la_cd_k", "la_cd_k");
        CATALOGUE_NAME_MAP.put("m_dim", "dim");
        CATALOGUE_NAME_MAP.put("m_time", "time");
        CATALOGUE_NAME_MAP.put("m_traeger", "traeger");
        CATALOGUE_NAME_MAP.put("prio", "prio");
        CATALOGUE_NAME_MAP.put("m_obsolet", "geschehen");
        CATALOGUE_NAME_MAP.put("m_exist", "geschehen");
        CATALOGUE_NAME_MAP.put("flood_area", "geschehen");
        CATALOGUE_NAME_MAP.put("m_hw_hq10", "geschehen");
        CATALOGUE_NAME_MAP.put("m_hw_hq100", "geschehen");
        CATALOGUE_NAME_MAP.put("m_hw_hq200", "geschehen");
        CATALOGUE_NAME_MAP.put("m_mnq", "geschehen");
        CATALOGUE_NAME_MAP.put("m_mq", "geschehen");
        CATALOGUE_NAME_MAP.put("m_q330", "geschehen");
        CATALOGUE_NAME_MAP.put("m_mhq", "geschehen");
    }

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new VwDvgStaluCidsLayer object.
     *
     * @param  mc    DOCUMENT ME!
     * @param  user  DOCUMENT ME!
     */
    public QpModellCidsLayer(final MetaClass mc, final User user) {
        super(mc, false, false, CATALOGUE_NAME_MAP, user);
    }
//
//    @Override
//    protected String getFieldRestriction(final String column) {
//        if (column.equals("dlm25w.qp.bemerkung")) {
//            if (isFullGUAccessAllowed()) {
//                return null;
//            } else {
//                return "upl_name = '" + user.getName() + "'";
//            }
//        }
//
//        return null;
//    }
//
//    @Override
//    public String getRestriction() {
//        if ((user != null) && user.getUserGroup().getName().equals("Administratoren")) {
//            // the admin has no restrictions
//            return null;
//        } else {
//            String rest = "((dlm25wPk_freigabe1.freigabe = 'uploader' and upl_name = '" + user.getName()
//                        + "') or dlm25wPk_freigabe1.freigabe is null or dlm25wPk_freigabe1.freigabe = 'frei')";
//
//            if ((user != null)
//                        && (user.getUserGroup().getName().contains("lu")
//                            || user.getUserGroup().getName().contains("wbv")
//                            || user.getUserGroup().getName().contains("uwb")
//                            || user.getUserGroup().getName().contains("wsa")
//                            || user.getUserGroup().getName().contains("stalu"))) {
//                rest = "(" + rest + " or (dlm25wPk_freigabe1.freigabe = 'wawi'))";
//            }
//
//            return rest;
//        }
//    }
}
