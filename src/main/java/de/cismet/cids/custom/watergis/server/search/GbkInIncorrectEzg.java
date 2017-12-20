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
public class GbkInIncorrectEzg extends AbstractAnalyzeSearch {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WkkSearch object.
     *
     * @param  owner  DOCUMENT ME!
     * @param  ids    DOCUMENT ME!
     */
    public GbkInIncorrectEzg(final String owner, final int[] ids) {
        super(owner, ids);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected String createQuery(final String owner, final int[] ids) {
        if ((owner == null) || owner.startsWith("lung")
                    || owner.equalsIgnoreCase("administratoren")) {
            return
                "select distinct id, geom, ba_cd, bak_st_von, bak_st_bis, ezg, gbk_lawa, laenge, fis_g_date, fis_g_user from dlm25w.select_fg_bak_gbk_in_incorrect_ezg(null, "
                        + createSqlString(ids)
                        + ")";
        } else {
            return
                "select distinct id, geom, ba_cd, bak_st_von, bak_st_bis, ezg, gbk_lawa, laenge, fis_g_date, fis_g_user from dlm25w.select_fg_bak_gbk_in_incorrect_ezg('"
                        + owner
                        + "', "
                        + createSqlString(ids)
                        + ")";
        }
    }
}
