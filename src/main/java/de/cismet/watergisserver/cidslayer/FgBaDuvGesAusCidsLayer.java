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
public class FgBaDuvGesAusCidsLayer extends Default1505ConsideredCidsLayer {

    //~ Static fields/initializers ---------------------------------------------

    private static final HashMap<String, String> CATALOGUE_NAME_MAP = new HashMap<String, String>();

    static {
        CATALOGUE_NAME_MAP.put("ww_gr", "ww_gr");
        CATALOGUE_NAME_MAP.put("seite", "seite");
    }

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new FgBaCidsLayer object.
     *
     * @param  mc    DOCUMENT ME!
     * @param  user  DOCUMENT ME!
     */
    public FgBaDuvGesAusCidsLayer(final MetaClass mc, final User user) {
        super(mc, user, CATALOGUE_NAME_MAP);
        init();
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected void init(final MetaClass mc) {
        // init the class in the init() method, when the user is set
    }

    /**
     * DOCUMENT ME!
     */
    private void init() {
        super.init(mc);
    }
}