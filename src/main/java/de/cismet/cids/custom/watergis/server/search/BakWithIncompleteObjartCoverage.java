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
public class BakWithIncompleteObjartCoverage extends AbstractAnalyzeSearch {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WkkSearch object.
     *
     * @param  owner   DOCUMENT ME!
     * @param  ids     DOCUMENT ME!
     * @param  export  DOCUMENT ME!
     */
    public BakWithIncompleteObjartCoverage(final String owner, final int[] ids, final boolean export) {
        super(owner, ids, export);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected String createQuery(final String owner, final int[] ids, final boolean export) {
        if ((owner == null) || owner.startsWith("lung")
                    || owner.equalsIgnoreCase("administratoren")) {
            return
                "select id, geom, ba_cd, bak_st_von, bak_st_bis, objart, laenge, fis_g_date, fis_g_user from dlm25w.select_fg_bak_with_incomplete_objart_coverage(null, "
                        + createSqlString(ids)
                        + ")";
        } else {
            return
                "select id, geom, ba_cd, bak_st_von, bak_st_bis, objart, laenge, fis_g_date, fis_g_user from dlm25w.select_fg_bak_with_incomplete_objart_coverage('"
                        + owner
                        + "', "
                        + createSqlString(ids)
                        + ")";
        }
    }
}
