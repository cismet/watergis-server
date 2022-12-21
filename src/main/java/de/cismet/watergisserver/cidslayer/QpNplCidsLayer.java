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
public class QpNplCidsLayer extends WatergisDefaultCidsLayer {

    //~ Static fields/initializers ---------------------------------------------

    private static final HashMap<String, String> CATALOGUE_NAME_MAP = new HashMap<String, String>();

    static {
        CATALOGUE_NAME_MAP.put("ww_gr", "ww_gr");
        CATALOGUE_NAME_MAP.put("l_bezug", "l_bezug");
        CATALOGUE_NAME_MAP.put("h_bezug", "h_bezug");
        CATALOGUE_NAME_MAP.put("freigabe", "freigabe");
        CATALOGUE_NAME_MAP.put("l_st", "l_st");
        CATALOGUE_NAME_MAP.put("qp_hist", "hist");
    }

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new VwDvgStaluCidsLayer object.
     *
     * @param  mc    DOCUMENT ME!
     * @param  user  DOCUMENT ME!
     */
    public QpNplCidsLayer(final MetaClass mc, final User user) {
        super(mc, false, false, CATALOGUE_NAME_MAP, user);
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
    public void handleQp_upl(final MemberAttributeInfo attr,
            final MetaClass foreignClass,
            final List<String> sb,
            final List<String> columnNamesList,
            final List<String> columnPropertyNamesList,
            final List<String> sqlColumnNamesList,
            final List<String> primitiveColumnTypesList,
            final StringBuilder joins) {
        final HashMap allClasses = foreignClass.getEmptyInstance().getAllClasses();
        ObjectAttribute nameAttr = (ObjectAttribute)foreignClass.getEmptyInstance().getAttribute("ww_gr");
        final MetaClass wwGrClass = getBeanClass(allClasses, nameAttr.getMai().getForeignKeyClassId());
        nameAttr = (ObjectAttribute)wwGrClass.getEmptyInstance().getAttribute("ww_gr");

        sb.add(1, "dlm25wPk_ww_gr1.ww_gr");
        columnNamesList.add(1, "ww_gr");
        sqlColumnNamesList.add(1, "dlm25wPk_ww_gr1.ww_gr");
        columnPropertyNamesList.add(1, attr.getName() + ".ww_gr.ww_gr");
        primitiveColumnTypesList.add(1, nameAttr.getMai().getJavaclassname());

        nameAttr = (ObjectAttribute)foreignClass.getEmptyInstance().getAttribute("l_st");
        final MetaClass lStClass = getBeanClass(allClasses, nameAttr.getMai().getForeignKeyClassId());
        nameAttr = (ObjectAttribute)lStClass.getEmptyInstance().getAttribute("l_st");
        sb.add("dlm25wPk_lst.l_st");
        columnNamesList.add("l_st");
        sqlColumnNamesList.add("dlm25wPk_lst.l_st");
        columnPropertyNamesList.add(attr.getName() + ".l_st.l_st");
        primitiveColumnTypesList.add(nameAttr.getMai().getJavaclassname());

        nameAttr = (ObjectAttribute)foreignClass.getEmptyInstance().getAttribute("l_bezug");
        final MetaClass lBezugClass = getBeanClass(allClasses, nameAttr.getMai().getForeignKeyClassId());
        nameAttr = (ObjectAttribute)lBezugClass.getEmptyInstance().getAttribute("l_bezug");
        sb.add("dlm25wPk_lbezug.l_bezug");
        columnNamesList.add("l_bezug");
        sqlColumnNamesList.add("dlm25wPk_lbezug.l_bezug");
        columnPropertyNamesList.add(attr.getName() + ".l_bezug.l_bezug");
        primitiveColumnTypesList.add(nameAttr.getMai().getJavaclassname());

        nameAttr = (ObjectAttribute)foreignClass.getEmptyInstance().getAttribute("h_bezug");
        final MetaClass hBezugClass = getBeanClass(allClasses, nameAttr.getMai().getForeignKeyClassId());
        nameAttr = (ObjectAttribute)hBezugClass.getEmptyInstance().getAttribute("h_bezug");
        sb.add("dlm25wPk_hbezug.h_bezug");
        columnNamesList.add("h_bezug");
        sqlColumnNamesList.add("dlm25wPk_hbezug.h_bezug");
        columnPropertyNamesList.add(attr.getName() + ".h_bezug.h_bezug");
        primitiveColumnTypesList.add(nameAttr.getMai().getJavaclassname());

        nameAttr = (ObjectAttribute)foreignClass.getEmptyInstance().getAttribute("l_calc");
        sb.add("dlm25w.qp_upl.l_calc");
        columnNamesList.add("l_calc");
        sqlColumnNamesList.add("dlm25w.qp_upl.l_calc");
        columnPropertyNamesList.add(attr.getName() + ".l_calc");
        primitiveColumnTypesList.add(nameAttr.getMai().getJavaclassname());

        nameAttr = (ObjectAttribute)foreignClass.getEmptyInstance().getAttribute("upl_nr");
        sb.add("dlm25w.qp_upl.upl_nr");
        columnNamesList.add("upl_nr");
        sqlColumnNamesList.add("dlm25w.qp_upl.upl_nr");
        columnPropertyNamesList.add(attr.getName() + ".upl_nr");
        primitiveColumnTypesList.add(nameAttr.getMai().getJavaclassname());

        nameAttr = (ObjectAttribute)foreignClass.getEmptyInstance().getAttribute("upl_name");
        sb.add("dlm25w.qp_upl.upl_name");
        columnNamesList.add("upl_name");
        sqlColumnNamesList.add("dlm25w.qp_upl.upl_name");
        columnPropertyNamesList.add(attr.getName() + ".upl_name");
        primitiveColumnTypesList.add(nameAttr.getMai().getJavaclassname());

        nameAttr = (ObjectAttribute)foreignClass.getEmptyInstance().getAttribute("upl_datum");
        sb.add("dlm25w.qp_upl.upl_datum");
        columnNamesList.add("upl_datum");
        sqlColumnNamesList.add("dlm25w.qp_upl.upl_datum");
        columnPropertyNamesList.add(attr.getName() + ".upl_datum");
        primitiveColumnTypesList.add(nameAttr.getMai().getJavaclassname());

        nameAttr = (ObjectAttribute)foreignClass.getEmptyInstance().getAttribute("upl_zeit");
        sb.add("dlm25w.qp_upl.upl_zeit");
        columnNamesList.add("upl_zeit");
        sqlColumnNamesList.add("dlm25w.qp_upl.upl_zeit");
        columnPropertyNamesList.add(attr.getName() + ".upl_zeit");
        primitiveColumnTypesList.add(nameAttr.getMai().getJavaclassname());

        nameAttr = (ObjectAttribute)foreignClass.getEmptyInstance().getAttribute("aufn_name");
        sb.add("dlm25w.qp_upl.aufn_name");
        columnNamesList.add("aufn_name");
        sqlColumnNamesList.add("dlm25w.qp_upl.aufn_name");
        columnPropertyNamesList.add(attr.getName() + ".aufn_name");
        primitiveColumnTypesList.add(nameAttr.getMai().getJavaclassname());

        nameAttr = (ObjectAttribute)foreignClass.getEmptyInstance().getAttribute("aufn_datum");
        sb.add("dlm25w.qp_upl.aufn_datum");
        columnNamesList.add("aufn_datum");
        sqlColumnNamesList.add("dlm25w.qp_upl.aufn_datum");
        columnPropertyNamesList.add(attr.getName() + ".aufn_datum");
        primitiveColumnTypesList.add(nameAttr.getMai().getJavaclassname());

        nameAttr = (ObjectAttribute)foreignClass.getEmptyInstance().getAttribute("aufn_zeit");
        sb.add("dlm25w.qp_upl.aufn_zeit");
        columnNamesList.add("aufn_zeit");
        sqlColumnNamesList.add("dlm25w.qp_upl.aufn_zeit");
        columnPropertyNamesList.add(attr.getName() + ".aufn_zeit");
        primitiveColumnTypesList.add(nameAttr.getMai().getJavaclassname());

        nameAttr = (ObjectAttribute)foreignClass.getEmptyInstance().getAttribute("freigabe");
        final MetaClass freigabeClass = getBeanClass(allClasses, nameAttr.getMai().getForeignKeyClassId());
        nameAttr = (ObjectAttribute)freigabeClass.getEmptyInstance().getAttribute("freigabe");
        sb.add("dlm25wPk_freigabe.freigabe");
        columnNamesList.add("freigabe");
        sqlColumnNamesList.add("dlm25wPk_freigabe.freigabe");
        columnPropertyNamesList.add(attr.getName() + ".freigabe.freigabe");
        primitiveColumnTypesList.add(nameAttr.getMai().getJavaclassname());

        nameAttr = (ObjectAttribute)foreignClass.getEmptyInstance().getAttribute("titel");
        sb.add("dlm25w.qp_upl.titel");
        columnNamesList.add("titel");
        sqlColumnNamesList.add("dlm25w.qp_upl.titel");
        columnPropertyNamesList.add(attr.getName() + ".titel");
        primitiveColumnTypesList.add(nameAttr.getMai().getJavaclassname());

        nameAttr = (ObjectAttribute)foreignClass.getEmptyInstance().getAttribute("beschreib");
        sb.add("dlm25w.qp_upl.beschreib");
        columnNamesList.add("beschreib");
        sqlColumnNamesList.add("dlm25w.qp_upl.beschreib");
        columnPropertyNamesList.add(attr.getName() + ".beschreib");
        primitiveColumnTypesList.add(nameAttr.getMai().getJavaclassname());

        joins.append(" left ").append(" join dlm25w.qp_upl on (dlm25w.qp_upl.id = dlm25w.qp_npl.qp_upl)");
        joins.append(" left ")
                .append(" join dlm25w.k_ww_gr dlm25wPk_ww_gr1 on (dlm25w.qp_upl.ww_gr = dlm25wPk_ww_gr1.id)");
        joins.append(" left ").append(" join dlm25w.k_l_st dlm25wPk_lst on (dlm25w.qp_upl.l_st = dlm25wPk_lst.id)");
        joins.append(" left ")
                .append(" join dlm25w.k_l_bezug dlm25wPk_lbezug on (dlm25w.qp_upl.l_bezug = dlm25wPk_lbezug.id)");
        joins.append(" left ")
                .append(" join dlm25w.k_h_bezug dlm25wPk_hbezug on (dlm25w.qp_upl.h_bezug = dlm25wPk_hbezug.id)");
        joins.append(" left ")
                .append(" join dlm25w.k_freigabe dlm25wPk_freigabe on (dlm25w.qp_upl.freigabe = dlm25wPk_freigabe.id)");
    }

    @Override
    public String getRestriction() {
        if ((user != null)
                    && (user.getUserGroup().getName().equals("anonymous")
                        || user.getUserGroup().getName().equals("gaeste"))) {
            final String rest = "(dlm25wPk_freigabe.freigabe is null or dlm25wPk_freigabe.freigabe = 'frei')";

            return rest;
        } else {
            return null;
        }
    }
}
