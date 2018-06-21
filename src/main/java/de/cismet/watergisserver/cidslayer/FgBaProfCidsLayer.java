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
public class FgBaProfCidsLayer extends Default1505ConsideredCidsLayer {

    //~ Static fields/initializers ---------------------------------------------

    private static final HashMap<String, String> CATALOGUE_NAME_MAP = new HashMap<String, String>();

    static {
        CATALOGUE_NAME_MAP.put("traeger", "traeger");
        CATALOGUE_NAME_MAP.put("l_st", "l_st");
        CATALOGUE_NAME_MAP.put("profil", "profil");
        CATALOGUE_NAME_MAP.put("zust_kl", "zust_kl");
    }

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new VwDvgStaluCidsLayer object.
     *
     * @param  mc    DOCUMENT ME!
     * @param  user  DOCUMENT ME!
     */
    public FgBaProfCidsLayer(final MetaClass mc, final User user) {
        super(
            mc,
            user,
            false,
            false,
            CATALOGUE_NAME_MAP,
            true,
            " left join dlm25w.k_ww_gr dlm25wPk_ww_gr1 on (dlm25w.fg_ba.ww_gr = dlm25wPk_ww_gr1.id)");
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
        if (column.equals("dlm25w.fg_ba_prof.zust_kl")
                    || column.equals("dlm25w.fg_ba_prof.bemerkung")
                    || column.equals("dlm25w.fg_ba_prof.br")
                    || column.equals("dlm25w.fg_ba_prof.ho_e")
                    || column.equals("dlm25w.fg_ba_prof.ho_a")
                    || column.equals("dlm25w.fg_ba_prof.gefaelle")
                    || column.equals("dlm25w.fg_ba_prof.bv_re")
                    || column.equals("dlm25w.fg_ba_prof.bh_re")
                    || column.equals("dlm25w.fg_ba_prof.bl_re")
                    || column.equals("dlm25w.fg_ba_prof.bv_li")
                    || column.equals("dlm25w.fg_ba_prof.bh_li")
                    || column.equals("dlm25w.fg_ba_prof.bl_li")
                    || column.equals("dlm25w.fg_ba_prof.mw")) {
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
