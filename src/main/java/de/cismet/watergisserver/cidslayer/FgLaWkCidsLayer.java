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
public class FgLaWkCidsLayer extends WatergisDefaultCidsLayer { // Default1505ConsideredCidsLayer {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new VwDvgStaluCidsLayer object.
     *
     * @param  mc    DOCUMENT ME!
     * @param  user  DOCUMENT ME!
     */
    public FgLaWkCidsLayer(final MetaClass mc, final User user) {
//        super(
//            mc,
//            user,
//            false,
//            false,
//            null,
//            false,
//            " left join dlm25w.k_ww_gr dlm25wPk_ww_gr1 on (dlm25w.fg_la_wk.ww_gr = dlm25wPk_ww_gr1.id)");
        super(mc, user);
    }
}
