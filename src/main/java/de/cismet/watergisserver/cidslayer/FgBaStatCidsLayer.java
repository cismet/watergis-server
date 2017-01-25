/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
public class FgBaStatCidsLayer extends Default1505ConsideredCidsLayer {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new FgBakCidsLayer object.
     *
     * @param  mc    DOCUMENT ME!
     * @param  user  DOCUMENT ME!
     */
    public FgBaStatCidsLayer(final MetaClass mc, final User user) {
//        super(mc, user);
        super(
            mc,
            user,
            false,
            false,
            null,
            false,
            " left join dlm25w.k_ww_gr dlm25wPk_ww_gr1 on (dlm25w.fg_ba_stat.ww_gr = dlm25wPk_ww_gr1.ww_gr)");
    }

//    @Override
//    public String getRestriction() {
//        if ((user == null) || user.getUserGroup().getName().equalsIgnoreCase("administratoren")) {
//            return null;
//        } else {
////            if (additionalJoin) {
//            return "(dlm25wPk_ww_gr1.wdm <> 1505 or dlm25wPk_ww_gr1.owner = '" + user
//                        .getUserGroup().getName() + "')";
////            } else {
////                return "(dlm25w.k_ww_gr.wdm <> 1505 or dlm25w.k_ww_gr.owner = '" + user
////                            .getUserGroup().getName() + "')";
////            }
//        }

}
