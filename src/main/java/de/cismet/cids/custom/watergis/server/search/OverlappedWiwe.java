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
public class OverlappedWiwe extends AbstractAnalyzeSearch {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WkkSearch object.
     *
     * @param  owner  DOCUMENT ME!
     * @param  ids    DOCUMENT ME!
     */
    public OverlappedWiwe(final String owner, final int[] ids) {
        super(owner, ids);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected String createQuery(final String owner, final int[] ids) {
        if ((owner == null) || owner.startsWith("lung")
                    || owner.equalsIgnoreCase("administratoren")) {
            return
                "select id, ww_gr, geom, l_st, wiwe, material, obj_nr, traeger, ausbaujahr, zust_kl, bemerkung, br, laenge, fis_g_date, fis_g_user from dlm25w.select_overlapped_gu_wiwe(null, "
                        + createSqlString(ids)
                        + ")";
        } else {
            return
                "select id, ww_gr, geom, l_st, wiwe, material, obj_nr, traeger, ausbaujahr, zust_kl, bemerkung, br, laenge, fis_g_date, fis_g_user from dlm25w.select_overlapped_gu_wiwe('"
                        + owner
                        + "', "
                        + createSqlString(ids)
                        + ")";
        }
    }
}
