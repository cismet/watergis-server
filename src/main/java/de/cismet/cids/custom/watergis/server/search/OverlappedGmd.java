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

import Sirius.server.middleware.interfaces.domainserver.MetaService;

import org.apache.log4j.Logger;

import java.rmi.RemoteException;

import java.util.ArrayList;
import java.util.Collection;

import de.cismet.cids.server.search.AbstractCidsServerSearch;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class OverlappedGmd extends AbstractAnalyzeSearch {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WkkSearch object.
     *
     * @param  owner   DOCUMENT ME!
     * @param  ids     DOCUMENT ME!
     * @param  export  DOCUMENT ME!
     */
    public OverlappedGmd(final String owner, final int[] ids, final boolean export) {
        super(owner, ids, export);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected String createQuery(final String owner, final int[] ids, final boolean export) {
        if ((owner == null) || owner.startsWith("lung")
                    || owner.equalsIgnoreCase("administratoren")) {
            return
                "select id, geom, ww_gr, ba_cd, ba_st_von, ba_st_bis, laenge, fis_g_date, fis_g_user from dlm25w.select_overlapped_fg_ba_gmd(null, "
                        + createSqlString(ids)
                        + ", "
                        + export
                        + ")";
        } else {
            return
                "select id, geom, ww_gr, ba_cd, ba_st_von, ba_st_bis, laenge, fis_g_date, fis_g_user from dlm25w.select_overlapped_fg_ba_gmd('"
                        + owner
                        + "', "
                        + createSqlString(ids)
                        + ", "
                        + export
                        + ")";
        }
    }
}
