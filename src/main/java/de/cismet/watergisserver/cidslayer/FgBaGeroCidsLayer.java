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
public class FgBaGeroCidsLayer extends Default1505ConsideredCidsLayer {

    //~ Static fields/initializers ---------------------------------------------

    private static final HashMap<String, String> CATALOGUE_NAME_MAP = new HashMap<String, String>();

    static {
        CATALOGUE_NAME_MAP.put("ww_gr", "ww_gr");
        CATALOGUE_NAME_MAP.put("traeger", "traeger");
        CATALOGUE_NAME_MAP.put("zust_kl", "zust_kl");
        CATALOGUE_NAME_MAP.put("l_st", "l_st");
        CATALOGUE_NAME_MAP.put("profil", "profil");
    }

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new FgBaGeroCidsLayer object.
     *
     * @param  mc    DOCUMENT ME!
     * @param  user  DOCUMENT ME!
     */
    public FgBaGeroCidsLayer(final MetaClass mc, final User user) {
        super(mc, user, false, true, CATALOGUE_NAME_MAP);
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
        if (column.equals("dlm25w.fg_ba_gero.zust_kl")
                    || column.equals("dlm25w.fg_ba_gero.bemerkung")
                    || column.equals("dlm25w.fg_ba_gero.br_so")
                    || column.equals("dlm25w.fg_ba_gero.ho_e")
                    || column.equals("dlm25w.fg_ba_gero.ho_a")
                    || column.equals("dlm25w.fg_ba_gero.gefaelle")
                    || column.equals("dlm25w.fg_ba_gero.bv_re")
                    || column.equals("dlm25w.fg_ba_gero.bh_re")
                    || column.equals("dlm25w.fg_ba_gero.bl_re")
                    || column.equals("dlm25w.fg_ba_gero.bv_li")
                    || column.equals("dlm25w.fg_ba_gero.bh_li")
                    || column.equals("dlm25w.fg_ba_gero.bl_li")
                    || column.equals("dlm25w.fg_ba_gero.mw")) {
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
