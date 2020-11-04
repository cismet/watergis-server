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
public class FgBaDuvDGesCidsLayer extends Default1505ConsideredCidsLayer {

    //~ Static fields/initializers ---------------------------------------------

    private static final HashMap<String, String> CATALOGUE_NAME_MAP = new HashMap<String, String>();

    static {
        CATALOGUE_NAME_MAP.put("profil", "profil");
        CATALOGUE_NAME_MAP.put("material", "material");
        CATALOGUE_NAME_MAP.put("traeger", "traeger");
        CATALOGUE_NAME_MAP.put("zust_kl", "zust_kl");
        CATALOGUE_NAME_MAP.put("l_st", "l_st");
        CATALOGUE_NAME_MAP.put("ww_gr", "ww_gr");
    }

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new VwDvgStaluCidsLayer object.
     *
     * @param  mc    DOCUMENT ME!
     * @param  user  DOCUMENT ME!
     */
    public FgBaDuvDGesCidsLayer(final MetaClass mc, final User user) {
        super(
            mc,
            user,
            false,
            false,
            CATALOGUE_NAME_MAP);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected boolean hasAttributeReadPermission(final String column, final User user) {
        if (column.equals("duv.fg_ba_duv_d_ges.material")
                    || column.equals("duv.fg_ba_duv_d_ges.obj_nr_gu")
                    || column.equals("duv.fg_ba_duv_d_ges.traeger")
                    || column.equals("duv.fg_ba_duv_d_ges.traeger_gu")
                    || column.equals("duv.fg_ba_duv_d_ges.ausbaujahr")) {
            return ((user != null)
                            && (isFullGUAccessAllowed()
                                || user.getUserGroup().getName().equals("Administratoren")
                                || user.getUserGroup().getName().contains("lu")
                                || user.getUserGroup().getName().contains("wbv")
                                || user.getUserGroup().getName().contains("uwb")
                                || user.getUserGroup().getName().contains("wsa")
                                || user.getUserGroup().getName().contains("stalu")));
        } else {
            return true;
        }
    }

    @Override
    protected String getFieldRestriction(final String column) {
        if (column.equals("duv.fg_ba_duv_d_ges.zust_kl")
                    || column.equals("duv.fg_ba_duv_d_ges.bemerkung")
                    || column.equals("duv.fg_ba_duv_d_ges.br_dm_li")
                    || column.equals("duv.fg_ba_duv_d_ges.ho_li")
                    || column.equals("duv.fg_ba_duv_d_ges.br_tr_o_li")
                    || column.equals("duv.fg_ba_duv_d_ges.ho_e")
                    || column.equals("duv.fg_ba_duv_d_ges.ho_a")
                    || column.equals("duv.fg_ba_duv_d_ges.gefaelle")
                    || column.equals("duv.fg_ba_duv_d_ges.ho_d_e")
                    || column.equals("duv.fg_ba_duv_d_ges.ho_d_a")
                    || column.equals("duv.fg_ba_duv_d_ges.ho_d_m")) {
            if (isFullGUAccessAllowed()) {
                return null;
            } else {
                return "dlm25wPk_ww_gr1.owner = '" + user.getUserGroup().getName() + "'";
            }
        }

        return null;
    }
}
