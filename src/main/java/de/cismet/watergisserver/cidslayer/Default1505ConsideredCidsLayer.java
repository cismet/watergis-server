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
import java.util.Map;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class Default1505ConsideredCidsLayer extends WatergisDefaultCidsLayer {

    //~ Static fields/initializers ---------------------------------------------

    private static final String[] GREEN_WBV = { "01", "02", "04", "05", "08", "10", "11", "20", "28", "09" };
    private static final String[] EXCEPTION_THEMES = { "dlm25w.fg_bak_gn1", "dlm25w.fg_bak_wk", "dlm25w.fg_ba_1_ord" };

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new Default1505ConsideredCidsLayer object.
     *
     * @param  mc    DOCUMENT ME!
     * @param  user  DOCUMENT ME!
     */
    public Default1505ConsideredCidsLayer(final MetaClass mc, final User user) {
        super(mc, user);
    }

    /**
     * Creates a new FgBakCidsLayer object.
     *
     * @param  mc                  DOCUMENT ME!
     * @param  user                DOCUMENT ME!
     * @param  CATALOGUE_NAME_MAP  DOCUMENT ME!
     */
    public Default1505ConsideredCidsLayer(final MetaClass mc,
            final User user,
            final HashMap<String, String> CATALOGUE_NAME_MAP) {
        super(mc, CATALOGUE_NAME_MAP, user);
    }

    /**
     * Creates a new Default1505ConsideredCidsLayer object.
     *
     * @param  mc              DOCUMENT ME!
     * @param  user            DOCUMENT ME!
     * @param  showFgLa        DOCUMENT ME!
     * @param  additionalGeom  DOCUMENT ME!
     */
    public Default1505ConsideredCidsLayer(final MetaClass mc,
            final User user,
            final boolean showFgLa,
            final boolean additionalGeom) {
        this(mc, user, showFgLa, additionalGeom, null);
    }

    /**
     * Creates a new Default1505ConsideredCidsLayer object.
     *
     * @param  mc                DOCUMENT ME!
     * @param  user              DOCUMENT ME!
     * @param  showFgLa          DOCUMENT ME!
     * @param  additionalGeom    DOCUMENT ME!
     * @param  catalogueNameMap  DOCUMENT ME!
     */
    public Default1505ConsideredCidsLayer(final MetaClass mc,
            final User user,
            final boolean showFgLa,
            final boolean additionalGeom,
            final Map<String, String> catalogueNameMap) {
        super(mc, showFgLa, additionalGeom, catalogueNameMap, user);
    }

    /**
     * Creates a new Default1505ConsideredCidsLayer object.
     *
     * @param  mc                DOCUMENT ME!
     * @param  user              DOCUMENT ME!
     * @param  showFgLa          DOCUMENT ME!
     * @param  additionalGeom    DOCUMENT ME!
     * @param  catalogueNameMap  DOCUMENT ME!
     * @param  inheritedWwGr     DOCUMENT ME!
     * @param  additionalJoins   DOCUMENT ME!
     */
    public Default1505ConsideredCidsLayer(final MetaClass mc,
            final User user,
            final boolean showFgLa,
            final boolean additionalGeom,
            final Map<String, String> catalogueNameMap,
            final boolean inheritedWwGr,
            final String additionalJoins) {
        super(mc, showFgLa, additionalGeom, catalogueNameMap, inheritedWwGr, additionalJoins, user);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public String getRestriction() {
        if ((user == null) || user.getUserGroup().getName().equalsIgnoreCase("administratoren")) {
            return null;
        } else {
            boolean isGreenWbv = false;

            for (final String greenWbv : GREEN_WBV) {
                if (user.getUserGroup().getName().startsWith("wbv_" + greenWbv)) {
                    isGreenWbv = true;
                }
            }

            boolean isExceptionTheme = false;

            for (final String et : EXCEPTION_THEMES) {
                if (mc.getTableName().equalsIgnoreCase(et)) {
                    isExceptionTheme = true;
                }
            }

            if (isExceptionTheme) {
                return null;
            }

            if (user.getUserGroup().getName().startsWith("wbv_") && !isGreenWbv) {
                return " (dlm25wPk_ww_gr1.owner = '" + user.getUserGroup().getName() + "')";
            } else {
                return "(dlm25wPk_ww_gr1.wdm <> 1505 or dlm25wPk_ww_gr1.owner = '" + user
                            .getUserGroup().getName() + "')";
            }
        }
    }
}
