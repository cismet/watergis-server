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
public class FgLakPrPfCidsLayer extends WatergisDefaultCidsLayer {

    //~ Static fields/initializers ---------------------------------------------

    private static final HashMap<String, String> CATALOGUE_NAME_MAP = new HashMap<String, String>();

    static {
        CATALOGUE_NAME_MAP.put("la_gn", "la_gn");
        CATALOGUE_NAME_MAP.put("la_cd_k", "la_cd_k");
        CATALOGUE_NAME_MAP.put("la_gn_t", "la_gn_t");
        CATALOGUE_NAME_MAP.put("la_wrrl", "la_wrrl");
        CATALOGUE_NAME_MAP.put("la_lage", "la_lage");
        CATALOGUE_NAME_MAP.put("ezg_fl", "ezg_fl");
        CATALOGUE_NAME_MAP.put("la_ordn", "la_ordn");
        CATALOGUE_NAME_MAP.put("la_cd", "la_cd");
    }

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new FgBakCidsLayer object.
     *
     * @param  mc    DOCUMENT ME!
     * @param  user  DOCUMENT ME!
     */
    public FgLakPrPfCidsLayer(final MetaClass mc, final User user) {
        super(mc, CATALOGUE_NAME_MAP, user);
    }
}
