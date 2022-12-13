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
public class SgSeeWkCidsLayer extends WatergisDefaultCidsLayer {

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
    public SgSeeWkCidsLayer(final MetaClass mc, final User user) {
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

        sb.add(2, "dlm25w.k_wk_sg.wk_nr");
        columnNamesList.add(2, "wk_nr");
        sqlColumnNamesList.add(2, "dlm25w.k_wk_sg.wk_nr");
        columnPropertyNamesList.add(2, "wk_nr");
        primitiveColumnTypesList.add(2, nameAttr.getMai().getJavaclassname());
        catalogueTypes.put("wk_nr", attr.getForeignKeyClassId());

        nameAttr = (ObjectAttribute)foreignClass.getEmptyInstance().getAttribute("wk_fedfue");
        final MetaClass kBlClass = getBeanClass(allClasses, nameAttr.getMai().getForeignKeyClassId());
        nameAttr = (ObjectAttribute)kBlClass.getEmptyInstance().getAttribute("bl_nr");

        sb.add("dlm25w.k_bl.bl_nr");
        columnNamesList.add("wk_fedfue");
        sqlColumnNamesList.add("dlm25w.k_bl.bl_nr");
        columnPropertyNamesList.add(attr.getName() + ".bl_nr");
        primitiveColumnTypesList.add(nameAttr.getMai().getJavaclassname());

        joins.append(" left ").append(" join dlm25w.k_wk_sg on (dlm25w.sg_see_wk.wk_nr = dlm25w.k_wk_sg.id)");
        joins.append(" left ").append(" join dlm25w.k_bl on (dlm25w.k_wk_sg.wk_fedfue = dlm25w.k_bl.id)");
    }
}
