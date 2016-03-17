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
public class FgBakGwkCidsLayer extends WatergisDefaultCidsLayer {

    //~ Static fields/initializers ---------------------------------------------

    private static final HashMap<String, String> CATALOGUE_NAME_MAP = new HashMap<String, String>();

    static {
        CATALOGUE_NAME_MAP.put("la_cd", "la_cd");
    }

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new FgBaCidsLayer object.
     *
     * @param  mc  DOCUMENT ME!
     */
    public FgBakGwkCidsLayer(final MetaClass mc) {
        super(mc, CATALOGUE_NAME_MAP);
    }
}
