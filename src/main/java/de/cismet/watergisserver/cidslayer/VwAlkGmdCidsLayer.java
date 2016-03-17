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

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class VwAlkGmdCidsLayer extends WatergisDefaultCidsLayer {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new FgBakCidsLayer object.
     *
     * @param  mc  DOCUMENT ME!
     */
    public VwAlkGmdCidsLayer(final MetaClass mc) {
        super(mc);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Is not in use, at the moment.
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
    public void handleGmd(final MemberAttributeInfo attr,
            final MetaClass foreignClass,
            final StringBuilder sb,
            final List<String> columnNamesList,
            final List<String> columnPropertyNamesList,
            final List<String> sqlColumnNamesList,
            final List<String> primitiveColumnTypesList,
            final StringBuilder joins) {
        // gmd_nr
        ObjectAttribute nameAttr = (ObjectAttribute)foreignClass.getEmptyInstance().getAttribute("gmd");

        final String alias = addLeftJoin(
                joins,
                foreignClass.getTableName(),
                mc.getTableName()
                        + ".gmd",
                "id");
        sb.append(alias).append(".").append(nameAttr.getMai().getFieldName());
        columnNamesList.add("gmd_nr");
        sqlColumnNamesList.add(alias + "." + nameAttr.getMai().getFieldName());
        columnPropertyNamesList.add(attr.getName());
        catalogueTypes.put(columnNamesList.get(columnNamesList.size() - 1), attr.getForeignKeyClassId());
        primitiveColumnTypesList.add(nameAttr.getMai().getJavaclassname());

        // gmd_name
        nameAttr = (ObjectAttribute)foreignClass.getEmptyInstance().getAttribute("name");

        sb.append(",").append(alias).append(".").append(nameAttr.getMai().getFieldName());
        columnNamesList.add("gmd_name");
        sqlColumnNamesList.add(alias + "." + nameAttr.getMai().getFieldName());
        columnPropertyNamesList.add(attr.getName() + "." + nameAttr.getMai().getFieldName());
//        catalogueTypes.put(columnNamesList.get(columnNamesList.size() - 1), attr.getForeignKeyClassId());
        primitiveColumnTypesList.add(nameAttr.getMai().getJavaclassname());

        // wbv

        for (int i = 1; i < 31; ++i) {
            if ((i != 3) && (i != 21) && (i != 26) && (i != 29)) {
                nameAttr = (ObjectAttribute)foreignClass.getEmptyInstance().getAttribute("wbv_" + toTwoDigit(i));

                sb.append(",").append(alias).append(".").append(nameAttr.getMai().getFieldName());
                columnNamesList.add("wbv_" + toTwoDigit(i));
                sqlColumnNamesList.add(alias + "." + nameAttr.getMai().getFieldName());
                columnPropertyNamesList.add(attr.getName() + "." + nameAttr.getMai().getFieldName());
//                catalogueTypes.put(columnNamesList.get(columnNamesList.size() - 1), attr.getForeignKeyClassId());
                primitiveColumnTypesList.add(nameAttr.getMai().getJavaclassname());
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   i  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private String toTwoDigit(final int i) {
        String digitString = String.valueOf(i);

        if (digitString.length() == 1) {
            digitString = "0" + digitString;
        }

        return digitString;
    }
}
