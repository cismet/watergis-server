/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.watergisserver.cidslayer;

import Sirius.server.localserver.attribute.MemberAttributeInfo;
import Sirius.server.localserver.attribute.ObjectAttribute;
import Sirius.server.middleware.types.MetaClass;

import java.util.List;

import de.cismet.cids.server.cidslayer.StationInfo;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class MnOwPegelCidsLayer extends WatergisDefaultCidsLayer {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new VwDvgStaluCidsLayer object.
     *
     * @param  mc  DOCUMENT ME!
     */
    public MnOwPegelCidsLayer(final MetaClass mc) {
        super(mc, true, true);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  attr                      DOCUMENT ME!
     * @param  foreignClass              DOCUMENT ME!
     * @param  sb                        DOCUMENT ME!
     * @param  columnNamesList           DOCUMENT ME!
     * @param  columnPropertyNamesList   DOCUMENT ME!
     * @param  sqlColumnNamesList        DOCUMENT ME!
     * @param  primitiveColumnTypesList  DOCUMENT ME!
     * @param  joins                     DOCUMENT ME!
     */
    public void handleLa_st(final MemberAttributeInfo attr,
            final MetaClass foreignClass,
            final List<String> sb,
            final List<String> columnNamesList,
            final List<String> columnPropertyNamesList,
            final List<String> sqlColumnNamesList,
            final List<String> primitiveColumnTypesList,
            final StringBuilder joins) {
        final String columnName = "station";
        sb.add("dlm25w.fg_la.la_cd,");
        columnNamesList.add("gwk_lawa");
        sqlColumnNamesList.add("dlm25w.fg_la.la_cd"); // urspruenglich bis.wert
        columnPropertyNamesList.add(attr.getName() + ".route.la_cd");
        sb.add("lastat.wert");
        columnNamesList.add(columnName);
        sqlColumnNamesList.add("lastat.wert");
        columnPropertyNamesList.add(attr.getName() + ".wert");
        joins.append(" left join dlm25w.fg_la_punkt lastat on (lastat.id = ").append(attr.getFieldName()).append(")");
        joins.append(" left join dlm25w.fg_la on (lastat.route = dlm25w.fg_la.id)");
        primitiveColumnTypesList.add("String");
        primitiveColumnTypesList.add("java.lang.Double");
        final StationInfo s = new StationInfo(false, true, "dlm25w.fg_ba", 5, "gwk_lawa");
        stationTypes.put(columnName, s);
    }
}
