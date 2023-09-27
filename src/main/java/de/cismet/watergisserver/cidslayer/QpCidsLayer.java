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
public class QpCidsLayer extends WatergisDefaultCidsLayer {

    //~ Static fields/initializers ---------------------------------------------

    private static final HashMap<String, String> CATALOGUE_NAME_MAP = new HashMap<String, String>();

    static {
        CATALOGUE_NAME_MAP.put("ww_gr", "ww_gr");
        CATALOGUE_NAME_MAP.put("l_st", "l_st");
        CATALOGUE_NAME_MAP.put("l_rl", "l_rl");
        CATALOGUE_NAME_MAP.put("freigabe", "freigabe");
    }

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new VwDvgStaluCidsLayer object.
     *
     * @param  mc    DOCUMENT ME!
     * @param  user  DOCUMENT ME!
     */
    public QpCidsLayer(final MetaClass mc, final User user) {
        super(mc, true, true, CATALOGUE_NAME_MAP, user);
    }

    //~ Methods ----------------------------------------------------------------

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
            String rest = "((dlm25wPk_freigabe1.freigabe = 'uploader' and upl_name = '" + user.getName()
                        + "') or dlm25wPk_freigabe1.freigabe is null or dlm25wPk_freigabe1.freigabe = 'frei')";

            if ((user != null)
                        && (user.getUserGroup().getName().contains("lm")
                            || user.getUserGroup().getName().contains("wbv")
                            || user.getUserGroup().getName().contains("uwb")
                            || user.getUserGroup().getName().contains("wsa")
                            || user.getUserGroup().getName().contains("stalu"))) {
                rest = "(" + rest + " or (dlm25wPk_freigabe1.freigabe = 'wawi'))";
            }

            return rest;
        }
    }
}
