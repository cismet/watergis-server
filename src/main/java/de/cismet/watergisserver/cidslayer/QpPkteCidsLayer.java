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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.cismet.cids.server.cidslayer.StationInfo;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class QpPkteCidsLayer extends WatergisDefaultCidsLayer {

    //~ Static fields/initializers ---------------------------------------------

    private static final HashMap<String, String> CATALOGUE_NAME_MAP = new HashMap<String, String>();

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new VwDvgStaluCidsLayer object.
     *
     * @param  mc    DOCUMENT ME!
     * @param  user  DOCUMENT ME!
     */
    public QpPkteCidsLayer(final MetaClass mc, final User user) {
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
    public void handleQp_npl(final MemberAttributeInfo attr,
            final MetaClass foreignClass,
            final List<String> sb,
            final List<String> columnNamesList,
            final List<String> columnPropertyNamesList,
            final List<String> sqlColumnNamesList,
            final List<String> primitiveColumnTypesList,
            final StringBuilder joins) {
        final HashMap allClasses = foreignClass.getEmptyInstance().getAllClasses();
        for (int i = 0; i < sb.size(); ++i) {
            if (sb.get(i).equals("ST_AsEWKb(geom) as geom")) {
                sb.remove(i);
                sb.add(i, "ST_AsEWKb(dlm25w.qp_pkte.geom) as geom");
            }
        }
        for (int i = 0; i < sqlColumnNamesList.size(); ++i) {
            if (sb.get(i).equals("geom")) {
                sb.remove(i);
                sb.add(i, "dlm25w.qp_pkte.geom");
            }
        }
        sqlGeoField = "dlm25w.qp_pkte.geom";
        sb.add("dlm25w.qp_npl.qp_nr");
        columnNamesList.add("qp_nr");
        sqlColumnNamesList.add("dlm25w.qp_npl.qp_nr");
        columnPropertyNamesList.add(attr.getName() + ".qp_nr");
        ObjectAttribute nameAttr = (ObjectAttribute)foreignClass.getEmptyInstance().getAttribute("qp_nr");
        primitiveColumnTypesList.add(nameAttr.getMai().getJavaclassname());

        final MetaClass kHist = getBeanClass(allClasses, attr.getForeignKeyClassId());
        nameAttr = (ObjectAttribute)kHist.getEmptyInstance().getAttribute("qp_hist");
        sb.add(1, "dlm25w_k_hist.hist");
        columnNamesList.add(1, "qp_hist");
        sqlColumnNamesList.add(1, "dlm25w_k_hist.hist");
        columnPropertyNamesList.add(1, attr.getName() + ".k_hist.jist");
        primitiveColumnTypesList.add(1, nameAttr.getMai().getJavaclassname());

//        sb.add("dlm25w_k_hist.hist");
//        columnNamesList.add("qp_hist");
//        sqlColumnNamesList.add("dlm25w.qp_npl.qp_hist");
//        columnPropertyNamesList.add(attr.getName() + ".qp_hist");
//        nameAttr = (ObjectAttribute)foreignClass.getEmptyInstance().getAttribute("qp_hist");
//        primitiveColumnTypesList.add(nameAttr.getMai().getJavaclassname());

        joins.append(" left ").append(" join dlm25w.qp_npl on (dlm25w.qp_npl.id = dlm25w.qp_pkte.qp_npl)");
        joins.append(" left ")
                .append(" join dlm25w.k_hist dlm25w_k_hist  on (dlm25w.qp_npl.qp_hist = dlm25w_k_hist.id)");
    }

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

        nameAttr = (ObjectAttribute)foreignClass.getEmptyInstance().getAttribute("upl_nr");
        sb.add("dlm25w.qp_upl.upl_nr");
        columnNamesList.add("upl_nr");
        sqlColumnNamesList.add("dlm25w.qp_upl.upl_nr");
        columnPropertyNamesList.add(attr.getName() + ".upl_nr");
        primitiveColumnTypesList.add(nameAttr.getMai().getJavaclassname());

        nameAttr = (ObjectAttribute)foreignClass.getEmptyInstance().getAttribute("l_calc");
        sb.add("dlm25w.qp_upl.l_calc");
        columnNamesList.add("l_calc");
        sqlColumnNamesList.add("dlm25w.qp_upl.l_calc");
        columnPropertyNamesList.add(attr.getName() + ".l_calc");
        primitiveColumnTypesList.add(nameAttr.getMai().getJavaclassname());

        joins.append(" left ").append(" join dlm25w.qp_upl on (dlm25w.qp_upl.id = dlm25w.qp_pkte.qp_upl)");
        joins.append(" left ")
                .append(" join dlm25w.k_ww_gr dlm25wPk_ww_gr1 on (dlm25w.qp_upl.ww_gr = dlm25wPk_ww_gr1.id)");
    }
}
