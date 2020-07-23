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

import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;

import de.cismet.cids.server.search.AbstractCidsServerSearch;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public abstract class WritableSearch extends AbstractCidsServerSearch {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   rs  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  SQLException           DOCUMENT ME!
     * @throws  IllegalStateException  DOCUMENT ME!
     */
    public ArrayList<ArrayList> collectResults(final ResultSet rs) throws SQLException {
        final ArrayList<ArrayList> result = new ArrayList<ArrayList>();

        while (rs.next()) {
            final ArrayList row = new ArrayList();
            for (int i = 0; i < rs.getMetaData().getColumnCount(); ++i) {
                final Object objectFromResultSet = rs.getObject(i + 1);

                if (objectFromResultSet instanceof Clob) {
                    // we convert the clob to a string, otherwise the value is not serialisable out of the
                    // box due to the direct connection to the database
                    // TODO: handle overflows, i.e. clob too big
                    final Clob clob = (Clob)objectFromResultSet;
                    if (clob.length() <= Integer.valueOf(Integer.MAX_VALUE).longValue()) {
                        row.add(clob.getSubString(1, Long.valueOf(clob.length()).intValue()));
                    } else {
                        throw new IllegalStateException(
                            "cannot handle clobs larger than Integer.MAX_VALUE)");
                    }
                } else {
                    row.add(objectFromResultSet);
                }
            }
            result.add(row);
        }

        return result;
    }
}
