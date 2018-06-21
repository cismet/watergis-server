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
public class QpGafLCidsLayer extends WatergisDefaultCidsLayer {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new VwDvgStaluCidsLayer object.
     *
     * @param  mc    DOCUMENT ME!
     * @param  user  DOCUMENT ME!
     */
    public QpGafLCidsLayer(final MetaClass mc, final User user) {
        super(mc, user);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected String getFieldRestriction(final String column) {
        if (column.equals("dlm25w.qp.bemerkung")) {
            if ((user == null) || user.getUserGroup().getName().startsWith("lung")
                        || user.getUserGroup().getName().equalsIgnoreCase("administratoren")) {
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
