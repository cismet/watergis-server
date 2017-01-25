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
public class FgBakGbkCidsLayer extends Default1505ConsideredCidsLayer {

    //~ Static fields/initializers ---------------------------------------------

    private static final HashMap<String, String> CATALOGUE_NAME_MAP = new HashMap<String, String>();

    static {
        CATALOGUE_NAME_MAP.put("gbk_lawa", "gbk_lawa");
    }

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new FgBaCidsLayer object.
     *
     * @param  mc    DOCUMENT ME!
     * @param  user  DOCUMENT ME!
     */
    public FgBakGbkCidsLayer(final MetaClass mc, final User user) {
        super(
            mc,
            user,
            false,
            false,
            CATALOGUE_NAME_MAP,
            false,
            " left join dlm25w.k_ww_gr dlm25wPk_ww_gr1 on (dlm25w.fg_bak.ww_gr = dlm25wPk_ww_gr1.id)");
    }
}
