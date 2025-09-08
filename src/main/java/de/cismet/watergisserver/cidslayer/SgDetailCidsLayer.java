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
public class SgDetailCidsLayer extends WatergisDefaultCidsLayer {

    //~ Static fields/initializers ---------------------------------------------

    private static final HashMap<String, String> CATALOGUE_NAME_MAP = new HashMap<String, String>();

    static {
        CATALOGUE_NAME_MAP.put("ww_gr", "ww_gr");
    }

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new VwDvgStaluCidsLayer object.
     *
     * @param  mc    DOCUMENT ME!
     * @param  user  DOCUMENT ME!
     */
    public SgDetailCidsLayer(final MetaClass mc, final User user) {
        super(mc, CATALOGUE_NAME_MAP, user);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public String getRestriction() {
        if ((user == null)
                    || user.getUserGroup().getName().equalsIgnoreCase("administratoren")
                    || user.getUserGroup().getName().equalsIgnoreCase("seenprogramm")) {
            return null;
        } else {
            return "vl = 0 and kg = 0";
        }
    }

    @Override
    protected boolean hasAttributeReadPermission(final String column, final User user) {
        if (column.equals("dlm25w.sg_detail.vl") || column.equals("dlm25w.sg_detail.kg")) {
            return ((user == null)
                            || user.getUserGroup().getName().equalsIgnoreCase("administratoren")
                            || user.getUserGroup().getName().equalsIgnoreCase("seenprogramm"));
        } else {
            return true;
        }
    }

//    @Override
//    protected String getFieldRestriction(final String column) {
//        if (column.equals("dlm25w.sg_detail.vl") || column.equals("dlm25w.sg_detail.kg")) {
//            if ((user == null)
//                        || user.getUserGroup().getName().equalsIgnoreCase("administratoren")
//                        || user.getUserGroup().getName().equalsIgnoreCase("seenprogramm")) {
//                return null;
//            } else {
//                return "false";
//            }
//        }
//
//        return null;
//    }
}
