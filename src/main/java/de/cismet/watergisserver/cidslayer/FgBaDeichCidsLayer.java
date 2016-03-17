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

import java.util.HashMap;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class FgBaDeichCidsLayer extends WatergisDefaultCidsLayer {

    //~ Static fields/initializers ---------------------------------------------

    private static final HashMap<String, String> CATALOGUE_NAME_MAP = new HashMap<String, String>();

    static {
        CATALOGUE_NAME_MAP.put("l_rl", "l_rl");
        CATALOGUE_NAME_MAP.put("l_st", "l_st");
        CATALOGUE_NAME_MAP.put("ord", "ord");
        CATALOGUE_NAME_MAP.put("deich", "deich");
        CATALOGUE_NAME_MAP.put("traeger", "traeger");
        CATALOGUE_NAME_MAP.put("ww_gr", "ww_gr");
        CATALOGUE_NAME_MAP.put("l_fk", "l_fk");
        CATALOGUE_NAME_MAP.put("schgr", "schgr");
        CATALOGUE_NAME_MAP.put("material_f", "material");
        CATALOGUE_NAME_MAP.put("material_w", "material");
        CATALOGUE_NAME_MAP.put("material_k", "material");
        CATALOGUE_NAME_MAP.put("material_i", "material");
        CATALOGUE_NAME_MAP.put("material_b", "material");
    }

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new VwDvgStaluCidsLayer object.
     *
     * @param  mc  DOCUMENT ME!
     */
    public FgBaDeichCidsLayer(final MetaClass mc) {
        super(mc, false, true, CATALOGUE_NAME_MAP);
    }
}
