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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class ActionHelper {

    //~ Static fields/initializers ---------------------------------------------

    private static final String QUOTE_IDENTIFIER = "select quote_ident(?)";
    public static final String[] INVALID_DB_USER = { "postgres", "rep_admin", "user_ro" };
    public static final String[] INVALID_SCHEMAS = {
            "dlm25w",
            "public",
            "duv",
            "ogc",
            "raw",
            "pg_catalog",
            "information_schema"
        };

    //~ Methods ----------------------------------------------------------------

    /**
     * Quotes the given identifier.
     *
     * @param   con         a db connection
     * @param   identifier  an identifier
     *
     * @return  the quoted identifier
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public static String quoteIdentifier(final Connection con, final String identifier) throws Exception {
        String result = null;
        PreparedStatement psCreate = null;
        ResultSet rs = null;

        try {
            psCreate = con.prepareStatement(QUOTE_IDENTIFIER);
            psCreate.setString(1, identifier);
            rs = psCreate.executeQuery();

            if (rs.next()) {
                result = rs.getString(1);
            }

            return result;
        } finally {
            if (psCreate != null) {
                psCreate.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    /**
     * Checks, if the given schema name can be used for external use.
     *
     * @param   schemaName  the name to check
     *
     * @return  true, iff the given schema name can be used for external use
     */
    public static boolean isInvalidSchemaName(final String schemaName) {
        return arrayContainsString(INVALID_SCHEMAS, schemaName) || schemaName.toLowerCase().startsWith("pg_");
    }

    /**
     * Checks, if the given user name can be used for external use.
     *
     * @param   user  the name to check
     *
     * @return  true, iff the given user name can be used for external use
     */
    public static boolean isInvalidUserName(final String user) {
        return arrayContainsString(INVALID_DB_USER, user) || user.toLowerCase().startsWith("pg_");
    }

    /**
     * Checks, if the given array contains the given value.
     *
     * @param   array   DOCUMENT ME!
     * @param   string  DOCUMENT ME!
     *
     * @return  true, iff the given array contains the given value
     */
    private static boolean arrayContainsString(final String[] array, final String string) {
        for (final String entry : array) {
            if (entry.equalsIgnoreCase(string)) {
                return true;
            }
        }

        return false;
    }
}
