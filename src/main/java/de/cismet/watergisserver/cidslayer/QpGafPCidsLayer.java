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

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class QpGafPCidsLayer extends WatergisDefaultCidsLayer {

    //~ Instance fields --------------------------------------------------------

    private final User user;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new VwDvgStaluCidsLayer object.
     *
     * @param  mc    DOCUMENT ME!
     * @param  user  DOCUMENT ME!
     */
    public QpGafPCidsLayer(final MetaClass mc, final User user) {
        super(mc);
        this.user = user;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public String getRestriction() {
        if ((user != null) && user.getUserGroup().getName().equals("Administratoren")) {
            // the admin has no restrictions
            return null;
        } else {
            String rest = "((freigabe = 'uploader' and upl_name = '" + user.getName()
                        + "') or freigabe is null or freigabe = 'frei')";

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
