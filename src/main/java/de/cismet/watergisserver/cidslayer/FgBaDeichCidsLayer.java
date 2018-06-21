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
public class FgBaDeichCidsLayer extends Default1505ConsideredCidsLayer {

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
        CATALOGUE_NAME_MAP.put("zust_kl", "zust_kl");
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
     * @param  mc    DOCUMENT ME!
     * @param  user  DOCUMENT ME!
     */
    public FgBaDeichCidsLayer(final MetaClass mc, final User user) {
        super(mc, user, false, true, CATALOGUE_NAME_MAP);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected boolean hasAttributeReadPermission(final String column, final User user) {
        if (column.equals("dlm25w.fg_ba_deich.ord")
                    || column.equals("dlm25w.fg_ba_deich.l_fk")
                    || column.equals("dlm25w.fg_ba_deich.schgr")
                    || column.equals("dlm25w.fg_ba_deich.material_f")
                    || column.equals("dlm25w.fg_ba_deich.material_w")
                    || column.equals("dlm25w.fg_ba_deich.material_k")
                    || column.equals("dlm25w.fg_ba_deich.material_i")
                    || column.equals("dlm25w.fg_ba_deich.material_b")
                    || column.equals("dlm25w.fg_ba_deich.berme_w")
                    || column.equals("dlm25w.fg_ba_deich.berme_b")
                    || column.equals("dlm25w.fg_ba_deich.obj_nr")
                    || column.equals("dlm25w.fg_ba_deich.obj_nr_gu")
                    || column.equals("dlm25w.fg_ba_deich.traeger")
                    || column.equals("dlm25w.fg_ba_deich.traeger_gu")
                    || column.equals("dlm25w.fg_ba_deich.ausbaujahr")) {
            return ((user != null)
                            && (user.getUserGroup().getName().contains("lu")
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
        if (column.equals("dlm25w.fg_ba_deich.zust_kl")
                    || column.equals("dlm25w.fg_ba_deich.bemerkung")
                    || column.equals("dlm25w.fg_ba_deich.br_f")
                    || column.equals("dlm25w.fg_ba_deich.br_k")
                    || column.equals("dlm25w.fg_ba_deich.ho_k_f")
                    || column.equals("dlm25w.fg_ba_deich.ho_k_pn")
                    || column.equals("dlm25w.fg_ba_deich.ho_bhw_pn")
                    || column.equals("dlm25w.fg_ba_deich.ho_mw_pn")
                    || column.equals("dlm25w.fg_ba_deich.bv_w")
                    || column.equals("dlm25w.fg_ba_deich.bv_b")) {
            if ((user == null) || user.getUserGroup().getName().startsWith("lung")
                        || user.getUserGroup().getName().equalsIgnoreCase("administratoren")) {
                return null;
            } else {
                return "dlm25wPk_ww_gr1.owner = '" + user.getUserGroup().getName() + "'";
            }
        }

        return null;
    }
}
