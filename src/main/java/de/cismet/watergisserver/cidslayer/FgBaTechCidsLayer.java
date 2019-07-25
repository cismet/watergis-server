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
public class FgBaTechCidsLayer extends WatergisDefaultCidsLayer {

    //~ Static fields/initializers ---------------------------------------------

    private static final HashMap<String, String> CATALOGUE_NAME_MAP = new HashMap<String, String>();

    static {
        CATALOGUE_NAME_MAP.put("ww_gr", "ww_gr");
        CATALOGUE_NAME_MAP.put("l_st", "l_st");
        CATALOGUE_NAME_MAP.put("tech", "tech");
        CATALOGUE_NAME_MAP.put("na_gu", "na_gu");
        CATALOGUE_NAME_MAP.put("mahd_gu", "mahd_gu");
        CATALOGUE_NAME_MAP.put("gu_gu", "gu_gu");
    }

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new VwDvgStaluCidsLayer object.
     *
     * @param  mc    DOCUMENT ME!
     * @param  user  DOCUMENT ME!
     */
    public FgBaTechCidsLayer(final MetaClass mc, final User user) {
        super(
            mc,
            false,
            false,
            CATALOGUE_NAME_MAP,
            true,
            " left join dlm25w.k_ww_gr dlm25wPk_ww_gr1 on (dlm25w.fg_ba.ww_gr = dlm25wPk_ww_gr1.id)",
            user);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public String getRestriction() {
        if ((user == null) || user.getUserGroup().getName().startsWith("lung")
                    || user.getUserGroup().getName().equalsIgnoreCase("administratoren")) {
            return null;
        } else {
            return "(dlm25wPk_ww_gr1.owner = '" + user.getUserGroup().getName() + "' or dlm25wPk_ww_gr1.ww_gr = 4000 )";
        }
    }
}
