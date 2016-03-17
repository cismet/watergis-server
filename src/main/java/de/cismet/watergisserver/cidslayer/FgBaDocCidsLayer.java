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
 * This layer was removed.
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class FgBaDocCidsLayer extends WatergisDefaultCidsLayer {

    //~ Static fields/initializers ---------------------------------------------

    private static final HashMap<String, String> CATALOGUE_NAME_MAP = new HashMap<String, String>();

    static {
        CATALOGUE_NAME_MAP.put("ww_gr", "ww_gr");
    }

    //~ Instance fields --------------------------------------------------------

    private User user;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new VwDvgStaluCidsLayer object.
     *
     * @param  mc    DOCUMENT ME!
     * @param  user  DOCUMENT ME!
     */
    public FgBaDocCidsLayer(final MetaClass mc, final User user) {
        super(mc, CATALOGUE_NAME_MAP);
        this.user = user;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public String getRestriction() {
        if ((user == null) || user.getUserGroup().getName().startsWith("lung")
                    || user.getUserGroup().getName().equalsIgnoreCase("administratoren")) {
            return null;
        } else {
            return "dlm25wPk_ww_gr1.owner = '" + user.getUserGroup().getName() + "'";
        }
    }
}
