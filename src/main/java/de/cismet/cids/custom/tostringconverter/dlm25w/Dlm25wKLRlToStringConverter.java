/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.custom.tostringconverter.dlm25w;

import de.cismet.cids.tools.CustomToStringConverter;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class Dlm25wKLRlToStringConverter extends CustomToStringConverter {

    //~ Methods ----------------------------------------------------------------

// private static final Logger LOG = Logger.getLogger(BewirtschaftungsendeToStringConverter.class);
// private static Map<String, String> map = new Hashtable<String, String>();

    @Override
    public String createString() {
        String name = String.valueOf(cidsBean.getProperty("l_rl"));

        if (name == null) {
            name = "unbekannt";
        }

        return name;
    }
}
