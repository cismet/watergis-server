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

import Sirius.server.localserver.attribute.MemberAttributeInfo;
import Sirius.server.middleware.types.MetaClass;
import Sirius.server.newuser.User;

import java.util.HashMap;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class FgBaDueCidsLayer extends Default1505ConsideredCidsLayer {

    //~ Static fields/initializers ---------------------------------------------

    private static final HashMap<String, String> CATALOGUE_NAME_MAP = new HashMap<String, String>();

    static {
        CATALOGUE_NAME_MAP.put("profil", "profil");
        CATALOGUE_NAME_MAP.put("material", "material");
        CATALOGUE_NAME_MAP.put("traeger", "traeger");
        CATALOGUE_NAME_MAP.put("zust_kl", "zust_kl");
        CATALOGUE_NAME_MAP.put("l_st", "l_st");
    }

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new VwDvgStaluCidsLayer object.
     *
     * @param  mc    DOCUMENT ME!
     * @param  user  DOCUMENT ME!
     */
    public FgBaDueCidsLayer(final MetaClass mc, final User user) {
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
        if (column.equals("dlm25w.fg_ba_due.material")
                    || column.equals("dlm25w.fg_ba_due.obj_nr_gu")
                    || column.equals("dlm25w.fg_ba_due.traeger")
                    || column.equals("dlm25w.fg_ba_due.traeger_gu")
                    || column.equals("dlm25w.fg_ba_due.ausbaujahr")) {
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
        if (column.equals("dlm25w.fg_ba_due.zust_kl")
                    || column.equals("dlm25w.fg_ba_due.bemerkung")
                    || column.equals("dlm25w.fg_ba_due.br_dm_li")
                    || column.equals("dlm25w.fg_ba_due.ho_li")
                    || column.equals("dlm25w.fg_ba_due.br_tr_o_li")
                    || column.equals("dlm25w.fg_ba_due.ho_e")
                    || column.equals("dlm25w.fg_ba_due.ho_a")
                    || column.equals("dlm25w.fg_ba_due.gefaelle")
                    || column.equals("dlm25w.fg_ba_due.ho_d_e")
                    || column.equals("dlm25w.fg_ba_due.ho_d_a")
                    || column.equals("dlm25w.fg_ba_due.ho_d_iab")
                    || column.equals("dlm25w.fg_ba_due.ho_d_iauf")
                    || column.equals("dlm25w.fg_ba_due.ho_d_m")) {
            if ((user == null)
                        || user.getUserGroup().getName().equalsIgnoreCase("administratoren")) {
                return null;
            } else {
                return "dlm25wPk_ww_gr1.owner = '" + user.getUserGroup().getName() + "'";
            }
        }

        return null;
    }
}
