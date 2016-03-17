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
package de.cismet.cids.custom.helper;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class SQLFormatter {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   ids  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static String createSqlArrayString(final int[] ids) {
        if (ids == null) {
            return "null";
        }

        final StringBuilder builder = new StringBuilder("ARRAY[");
        boolean first = true;

        for (final int id : ids) {
            if (first) {
                first = false;
                builder.append(id);
            } else {
                builder.append(",").append(id);
            }
        }

        builder.append("]");

        return builder.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param   values  ids DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static String createSqlArrayString(final String[] values) {
        if (values == null) {
            return "null";
        }

        final StringBuilder builder = new StringBuilder("ARRAY[");
        boolean first = true;

        for (final String val : values) {
            if (first) {
                first = false;
                builder.append("'").append(val).append("'");
            } else {
                builder.append(",'").append(val).append("'");
            }
        }

        builder.append("]");

        return builder.toString();
    }
}
