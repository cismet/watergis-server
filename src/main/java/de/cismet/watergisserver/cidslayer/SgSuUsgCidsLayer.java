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
public class SgSuUsgCidsLayer extends WatergisDefaultCidsLayer {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new VwDvgStaluCidsLayer object.
     *
     * @param  mc    DOCUMENT ME!
     * @param  user  DOCUMENT ME!
     */
    public SgSuUsgCidsLayer(final MetaClass mc, final User user) {
        super(mc, true, user);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public String getRestriction() {
        if ((user == null)
                    || user.getUserGroup().getName().equalsIgnoreCase("administratoren")
                    || user.getUserGroup().getName().equalsIgnoreCase("seenprogramm")) {
            return null;
        } else {
            return "usg_aktiv = 1";
        }
    }

    @Override
    protected boolean hasAttributeReadPermission(final String column, final User user) {
        if (column.equals("dlm25w.sg_su_usg.usg_aktiv")) {
            return ((user == null)
                            || user.getUserGroup().getName().equalsIgnoreCase("administratoren")
                            || user.getUserGroup().getName().equalsIgnoreCase("seenprogramm"));
        } else {
            return true;
        }
    }
}
