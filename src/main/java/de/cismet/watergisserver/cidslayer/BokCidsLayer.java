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
public class BokCidsLayer extends WatergisDefaultCidsLayer {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new VwDvgStaluCidsLayer object.
     *
     * @param  mc    DOCUMENT ME!
     * @param  user  DOCUMENT ME!
     */
    public BokCidsLayer(final MetaClass mc, final User user) {
        super(mc, user);
    }
}
