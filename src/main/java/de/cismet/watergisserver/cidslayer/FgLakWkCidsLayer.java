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
import Sirius.server.newuser.User;

import java.util.HashMap;
import java.util.List;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class FgLakWkCidsLayer extends WatergisDefaultCidsLayer {

    //~ Static fields/initializers ---------------------------------------------

    private static final HashMap<String, String> CATALOGUE_NAME_MAP = new HashMap<String, String>();

    static {
//        CATALOGUE_NAME_MAP.put("wk_nr", "wk_nr");
    }

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new VwDvgStaluCidsLayer object.
     *
     * @param  mc    DOCUMENT ME!
     * @param  user  DOCUMENT ME!
     */
    public FgLakWkCidsLayer(final MetaClass mc, final User user) {
        super(mc, CATALOGUE_NAME_MAP, user);
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
    public void handleWk_nr(final MemberAttributeInfo attr,
            final MetaClass foreignClass,
            final List<String> sb,
            final List<String> columnNamesList,
            final List<String> columnPropertyNamesList,
            final List<String> sqlColumnNamesList,
            final List<String> primitiveColumnTypesList,
            final StringBuilder joins) {
        final HashMap allClasses = foreignClass.getEmptyInstance().getAllClasses();
        ObjectAttribute nameAttr = (ObjectAttribute)foreignClass.getEmptyInstance().getAttribute("wk_nr");

        sb.add("dlm25w.k_wk_fg.wk_nr");
        columnNamesList.add("wk_nr");
        sqlColumnNamesList.add("dlm25w.k_wk_fg.wk_nr");
        columnPropertyNamesList.add("wk_nr");
        primitiveColumnTypesList.add(nameAttr.getMai().getJavaclassname());
        catalogueTypes.put(columnNamesList.get(columnNamesList.size() - 1), attr.getForeignKeyClassId());

        nameAttr = (ObjectAttribute)foreignClass.getEmptyInstance().getAttribute("wk_fedfue");
        final MetaClass kBlClass = getBeanClass(allClasses, nameAttr.getMai().getForeignKeyClassId());
        nameAttr = (ObjectAttribute)kBlClass.getEmptyInstance().getAttribute("bl_nr");

        sb.add("dlm25w.k_bl.bl_nr");
        columnNamesList.add("wk_fedfue");
        sqlColumnNamesList.add("dlm25w.k_bl.bl_nr");
        columnPropertyNamesList.add(attr.getName() + ".bl_nr");
        primitiveColumnTypesList.add(nameAttr.getMai().getJavaclassname());

        nameAttr = (ObjectAttribute)foreignClass.getEmptyInstance().getAttribute("wk_ordnung");

        sb.add("dlm25w.k_wk_fg.wk_ordnung");
        columnNamesList.add("wk_ordnung");
        sqlColumnNamesList.add("dlm25w.k_wk_fg.wk_ordnung");
        columnPropertyNamesList.add(attr.getName());
        primitiveColumnTypesList.add(nameAttr.getMai().getJavaclassname());

        joins.append(" left ").append(" join dlm25w.k_wk_fg on (dlm25w.fg_lak_wk.wk_nr = dlm25w.k_wk_fg.id)");
        joins.append(" left ").append(" join dlm25w.k_bl on (dlm25w.k_wk_fg.wk_fedfue = dlm25w.k_bl.id)");
    }
}
