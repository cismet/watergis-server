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
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class BakWithIncompleteGmdCoverage extends AbstractAnalyzeSearch {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WkkSearch object.
     *
     * @param  owner  DOCUMENT ME!
     * @param  ids    DOCUMENT ME!
     */
    public BakWithIncompleteGmdCoverage(final String owner, final int[] ids) {
        super(owner, ids);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected String createQuery(final String owner, final int[] ids) {
        if ((owner == null) || owner.startsWith("lung")
                    || owner.equalsIgnoreCase("administratoren")) {
            return
                "select id, ww_gr, geom, ba_cd, ba_st_von, ba_st_bis, gmd_nr_re, gmd_nr_li, laenge, fis_g_date, fis_g_user from dlm25w.select_fg_ba_with_incomplete_gmd_coverage(null, "
                        + createSqlString(ids)
                        + ")";
        } else {
            return
                "select id, ww_gr, geom, ba_cd, ba_st_von, ba_st_bis, gmd_nr_re, gmd_nr_li, laenge, fis_g_date, fis_g_user from dlm25w.select_fg_ba_with_incomplete_gmd_coverage('"
                        + owner
                        + "', "
                        + createSqlString(ids)
                        + ")";
        }
    }
}
