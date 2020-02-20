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
public class RemoveDuplicatedNodesFromFgBak extends MergeSearch {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WkkSearch object.
     *
     * @param  owner  DOCUMENT ME!
     */
    public RemoveDuplicatedNodesFromFgBak(final String owner) {
        super(owner);
        QUERY = "select dlm25w.remove_duplicated_coords_in_fg_bak(?);";                  // NOI18N
    }
}
