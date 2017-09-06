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
public class FgBaFotoPrPfCidsLayer extends WatergisDefaultCidsLayer {

    //~ Static fields/initializers ---------------------------------------------

    private static final HashMap<String, String> CATALOGUE_NAME_MAP = new HashMap<String, String>();

    static {
        CATALOGUE_NAME_MAP.put("ww_gr", "ww_gr");
        CATALOGUE_NAME_MAP.put("l_st", "l_st");
        CATALOGUE_NAME_MAP.put("l_rl", "l_rl");
        CATALOGUE_NAME_MAP.put("k_freigabe", "freigabe");
    }

    //~ Instance fields --------------------------------------------------------

    private final User user;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new FgBaCidsLayer object.
     *
     * @param  mc    DOCUMENT ME!
     * @param  user  DOCUMENT ME!
     */
    public FgBaFotoPrPfCidsLayer(final MetaClass mc, final User user) {
        super(mc, CATALOGUE_NAME_MAP);
        this.user = user;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public String getRestriction() {
        if ((user != null) && user.getUserGroup().getName().equals("Administratoren")) {
            // the admin has no restrictions
            return null;
        } else {
            String rest = "((dlm25wPk_freigabe1.freigabe = 'uploader' and dlm25wPk_ww_gr1.owner = '"
                        + user.getUserGroup().getName()
                        + "') or dlm25wPk_freigabe1.freigabe is null or dlm25wPk_freigabe1.freigabe = 'frei')";

            if ((user != null)
                        && (user.getUserGroup().getName().contains("lu")
                            || user.getUserGroup().getName().contains("wbv")
                            || user.getUserGroup().getName().contains("uwb")
                            || user.getUserGroup().getName().contains("wsa")
                            || user.getUserGroup().getName().contains("stalu"))) {
                rest = "(" + rest + " or (freigabe = 'wawi'))";
            }

            return rest;
        }
    }
}
