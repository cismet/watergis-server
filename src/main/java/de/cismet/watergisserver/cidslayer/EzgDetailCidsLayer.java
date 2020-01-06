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
public class EzgDetailCidsLayer extends WatergisDefaultCidsLayer {

    //~ Static fields/initializers ---------------------------------------------

    private static final HashMap<String, String> CATALOGUE_NAME_MAP = new HashMap<String, String>();

    static {
        CATALOGUE_NAME_MAP.put("gbk_lawa", "gbk_lawa");
        CATALOGUE_NAME_MAP.put("gbk_lawa_k", "gbk_lawa_k");
        CATALOGUE_NAME_MAP.put("gbk_von", "gbk_von");
        CATALOGUE_NAME_MAP.put("gbk_bis", "gbk_bis");
        CATALOGUE_NAME_MAP.put("gbk_pl", "gbk_pl");
        CATALOGUE_NAME_MAP.put("gbk_ordn", "gbk_ordn");
        CATALOGUE_NAME_MAP.put("gwk_lawa", "la_cd");
        CATALOGUE_NAME_MAP.put("gwk_gn", "la_gn");
    }

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new VwDvgStaluCidsLayer object.
     *
     * @param  mc    DOCUMENT ME!
     * @param  user  DOCUMENT ME!
     */
    public EzgDetailCidsLayer(final MetaClass mc, final User user) {
        super(mc, CATALOGUE_NAME_MAP, user);
    }
}
