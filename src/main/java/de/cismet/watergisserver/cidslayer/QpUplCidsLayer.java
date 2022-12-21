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
public class QpUplCidsLayer extends WatergisDefaultCidsLayer {

    //~ Static fields/initializers ---------------------------------------------

    private static final HashMap<String, String> CATALOGUE_NAME_MAP = new HashMap<String, String>();

    static {
        CATALOGUE_NAME_MAP.put("ww_gr", "ww_gr");
        CATALOGUE_NAME_MAP.put("l_bezug", "l_bezug");
        CATALOGUE_NAME_MAP.put("h_bezug", "h_bezug");
        CATALOGUE_NAME_MAP.put("freigabe", "freigabe");
        CATALOGUE_NAME_MAP.put("l_st", "l_st");
    }

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new VwDvgStaluCidsLayer object.
     *
     * @param  mc    DOCUMENT ME!
     * @param  user  DOCUMENT ME!
     */
    public QpUplCidsLayer(final MetaClass mc, final User user) {
        super(mc, false, false, CATALOGUE_NAME_MAP, user);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public String getRestriction() {
        if ((user != null)
                    && (user.getUserGroup().getName().equals("anonymous")
                        || user.getUserGroup().getName().equals("gaeste"))) {
            final String rest = "(dlm25wPk_freigabe1.freigabe is null or dlm25wPk_freigabe1.freigabe = 'frei')";

            return rest;
        } else {
            return null;
        }
    }
}
