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
public class FgBaDuvProfGesCidsLayer extends Default1505ConsideredCidsLayer {

    //~ Static fields/initializers ---------------------------------------------

    private static final HashMap<String, String> CATALOGUE_NAME_MAP = new HashMap<String, String>();

    static {
        CATALOGUE_NAME_MAP.put("traeger", "traeger");
        CATALOGUE_NAME_MAP.put("l_st", "l_st");
        CATALOGUE_NAME_MAP.put("profil", "profil");
        CATALOGUE_NAME_MAP.put("zust_kl", "zust_kl");
        CATALOGUE_NAME_MAP.put("ww_gr", "ww_gr");
    }

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new VwDvgStaluCidsLayer object.
     *
     * @param  mc    DOCUMENT ME!
     * @param  user  DOCUMENT ME!
     */
    public FgBaDuvProfGesCidsLayer(final MetaClass mc, final User user) {
        super(
            mc,
            user,
            false,
            false,
            CATALOGUE_NAME_MAP);
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

    @Override
    protected String getFieldRestriction(final String column) {
        if (column.equals("duv.fg_ba_duv_prof_ges.zust_kl")
                    || column.equals("duv.fg_ba_duv_prof_ges.bemerkung")
                    || column.equals("duv.fg_ba_duv_prof_ges.br")
                    || column.equals("duv.fg_ba_duv_prof_ges.ho_e")
                    || column.equals("duv.fg_ba_duv_prof_ges.ho_a")
                    || column.equals("duv.fg_ba_duv_prof_ges.gefaelle")
                    || column.equals("duv.fg_ba_duv_prof_ges.bv_re")
                    || column.equals("duv.fg_ba_duv_prof_ges.bh_re")
                    || column.equals("duv.fg_ba_duv_prof_ges.bl_re")
                    || column.equals("duv.fg_ba_duv_prof_ges.bv_li")
                    || column.equals("duv.fg_ba_duv_prof_ges.bh_li")
                    || column.equals("duv.fg_ba_duv_prof_ges.bl_li")
                    || column.equals("duv.fg_ba_duv_prof_ges.mw")) {
            if (isFullGUAccessAllowed()) {
                return null;
            } else {
                return "dlm25wPk_ww_gr1.owner = '" + user.getUserGroup().getName() + "'";
            }
        }

        return null;
    }
}
