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

import java.util.HashMap;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class AslCidsLayer extends WatergisDefaultCidsLayer {

    //~ Static fields/initializers ---------------------------------------------

    private static final HashMap<String, String> CATALOGUE_NAME_MAP = new HashMap<String, String>();

    static {
        CATALOGUE_NAME_MAP.put("m_traeger", "traeger");
        CATALOGUE_NAME_MAP.put("szenario", "szenario");
    }

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new AslCidsLayer object.
     *
     * @param  mc    DOCUMENT ME!
     * @param  user  DOCUMENT ME!
     */
    public AslCidsLayer(final MetaClass mc, final User user) {
        super(mc, CATALOGUE_NAME_MAP, user);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public String getRestriction() {
        return "not coalesce(obsolet, false)";
    }
}
