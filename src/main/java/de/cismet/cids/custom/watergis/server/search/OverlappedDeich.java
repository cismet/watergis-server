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
package de.cismet.cids.custom.watergis.server.search;

/**
 * DOCUMENT ME!
 *
 * @author      therter
 * @version     $Revision$, $Date$
 * @deprecated  DOCUMENT ME!
 */
public class OverlappedDeich extends AbstractAnalyzeSearch {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WkkSearch object.
     *
     * @param  owner  DOCUMENT ME!
     * @param  ids    DOCUMENT ME!
     */
    public OverlappedDeich(final String owner, final int[] ids) {
        super(owner, ids);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected String createQuery(final String owner, final int[] ids) {
        if ((owner == null) || owner.startsWith("lung")
                    || owner.equalsIgnoreCase("administratoren")) {
            return
                "select id, ww_gr, geom, ba_cd, ba_st_von, ba_st_bis, km_von, km_bis, l_st, l_rl, nr, name, deich, org, l_fk, schgr, material_f, material_w, material_k, material_i, material_b, berme_w, berme_b, obj_nr, traeger, wbbl, ausbaujahr, zust_kl, bemerkung, br_f, br_k, ho_k_f, ho_k_pn, ho_bhw_pn, ho_mw_pn, bv_w, bv_b, laenge, fis_g_date, fis_g_user from dlm25w.select_overlapped_fg_ba_deich(null, "
                        + createSqlString(ids)
                        + ")";
        } else {
            return
                "select id, ww_gr, geom, ba_cd, ba_st_von, ba_st_bis, km_von, km_bis, l_st, l_rl, nr, name, deich, org, l_fk, schgr, material_f, material_w, material_k, material_i, material_b, berme_w, berme_b, obj_nr, traeger, wbbl, ausbaujahr, zust_kl, bemerkung, br_f, br_k, ho_k_f, ho_k_pn, ho_bhw_pn, ho_mw_pn, bv_w, bv_b, laenge, fis_g_date, fis_g_user from dlm25w.select_overlapped_fg_ba_deich('"
                        + owner
                        + "', "
                        + createSqlString(ids)
                        + ")";
        }
    }
}
