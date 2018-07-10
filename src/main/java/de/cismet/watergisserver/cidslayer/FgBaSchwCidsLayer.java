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
public class FgBaSchwCidsLayer extends Default1505ConsideredCidsLayer {

    //~ Static fields/initializers ---------------------------------------------

    private static final HashMap<String, String> CATALOGUE_NAME_MAP = new HashMap<String, String>();

    static {
        CATALOGUE_NAME_MAP.put("zust_kl", "zust_kl");
        CATALOGUE_NAME_MAP.put("traeger", "traeger");
        CATALOGUE_NAME_MAP.put("schw", "schw");
        CATALOGUE_NAME_MAP.put("l_st", "l_st");
    }

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new VwDvgStaluCidsLayer object.
     *
     * @param  mc    DOCUMENT ME!
     * @param  user  DOCUMENT ME!
     */
    public FgBaSchwCidsLayer(final MetaClass mc, final User user) {
        super(
            mc,
            user,
            false,
            false,
            CATALOGUE_NAME_MAP,
            true,
            " left join dlm25w.k_ww_gr dlm25wPk_ww_gr1 on (dlm25w.fg_ba.ww_gr = dlm25wPk_ww_gr1.id)");
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected boolean hasAttributeReadPermission(final String column, final User user) {
        if (column.equals("dlm25w.fg_ba_schw.obj_nr_gu")
                    || column.equals("dlm25w.fg_ba_schw.traeger")
                    || column.equals("dlm25w.fg_ba_schw.traeger_gu")
                    || column.equals("dlm25w.fg_ba_schw.ausbaujahr")) {
            return ((user != null)
                            && (user.getUserGroup().getName().equals("Administratoren")
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
        if (column.equals("dlm25w.fg_ba_schw.zust_kl")
                    || column.equals("dlm25w.fg_ba_schw.esw")
                    || column.equals("dlm25w.fg_ba_schw.bemerkung")
                    || column.equals("dlm25w.fg_ba_schw.br")
                    || column.equals("dlm25w.fg_ba_schw.sz")
                    || column.equals("dlm25w.fg_ba_schw.az")
                    || column.equals("dlm25w.fg_ba_schw.ezg_fl")
                    || column.equals("dlm25w.fg_ba_schw.v_fl")
                    || column.equals("dlm25w.fg_ba_schw.pu_anz1")
                    || column.equals("dlm25w.fg_ba_schw.pu_typ1")
                    || column.equals("dlm25w.fg_ba_schw.pu_motl1")
                    || column.equals("dlm25w.fg_ba_schw.pu_foel1")
                    || column.equals("dlm25w.fg_ba_schw.pu_anz2")
                    || column.equals("dlm25w.fg_ba_schw.pu_typ2")
                    || column.equals("dlm25w.fg_ba_schw.pu_motl2")
                    || column.equals("dlm25w.fg_ba_schw.pu_foel2")) {
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
