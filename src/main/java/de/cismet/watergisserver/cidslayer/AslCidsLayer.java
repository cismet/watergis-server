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
package de.cismet.watergisserver.cidslayer;

import Sirius.server.localserver.attribute.MemberAttributeInfo;
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
public class AslCidsLayer extends WatergisDefaultCidsLayer {

    //~ Static fields/initializers ---------------------------------------------

    private static final HashMap<String, String> CATALOGUE_NAME_MAP = new HashMap<String, String>();

    static {
        CATALOGUE_NAME_MAP.put("m_traeger", "traeger");
        CATALOGUE_NAME_MAP.put("szenario", "szenario");
    }

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new AslCidsLayer object.
     *
     * @param  mc    DOCUMENT ME!
     * @param  user  DOCUMENT ME!
     */
    public AslCidsLayer(final MetaClass mc, final User user) {
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
    public void handleQp_id(final MemberAttributeInfo attr,
            final MetaClass foreignClass,
            final List<String> sb,
            final List<String> columnNamesList,
            final List<String> columnPropertyNamesList,
            final List<String> sqlColumnNamesList,
            final List<String> primitiveColumnTypesList,
            final StringBuilder joins) {
        final HashMap allClasses = foreignClass.getEmptyInstance().getAllClasses();

        sb.add("dlm25w.asl.qp_id");
        columnNamesList.add("qp_id");
        sqlColumnNamesList.add("dlm25w.asl.qp_id");
        columnPropertyNamesList.add("qp_id");
        primitiveColumnTypesList.add("java.lang.Integer");

        sb.add("dlm25w.qp_modell.ba_gn");
        columnNamesList.add("ba_gn");
        sqlColumnNamesList.add("dlm25w.qp_modell.ba_gn");
        columnPropertyNamesList.add("qp_modell.ba_gn");
        primitiveColumnTypesList.add("java.lang.String");

        sb.add("dlm25w.k_m_traeger.traeger as m_traeger");
        columnNamesList.add("m_traeger");
        sqlColumnNamesList.add("dlm25w.k_m_traeger.traeger");
        columnPropertyNamesList.add("qp_modell.m_traeger.traeger");
        primitiveColumnTypesList.add("java.lang.String");

        sb.add("dlm25w.qp_modell.abschnitt");
        columnNamesList.add("abschnitt");
        sqlColumnNamesList.add("dlm25w.qp_modell.abschnitt");
        columnPropertyNamesList.add("qp_modell.abschnitt");
        primitiveColumnTypesList.add("java.lang.String");

        sb.add("dlm25w.qp_modell.jahr");
        columnNamesList.add("jahr");
        sqlColumnNamesList.add("dlm25w.qp_modell.jahr");
        columnPropertyNamesList.add("qp_modell.jahr");
        primitiveColumnTypesList.add("java.lang.String");

        sb.add("dlm25w.qp_modell.m_software");
        columnNamesList.add("m_software");
        sqlColumnNamesList.add("dlm25w.qp_modell.m_software");
        columnPropertyNamesList.add("qp_modell.m_software");
        primitiveColumnTypesList.add("java.lang.String");

        sb.add("dlm25w.k_m_dim.dim as m_dim");
        columnNamesList.add("m_dim");
        sqlColumnNamesList.add("dlm25w.k_m_dim.dim");
        columnPropertyNamesList.add("qp_modell.m_dim.dim");
        primitiveColumnTypesList.add("java.lang.String");

        joins.append(" left join dlm25w.qp_modell on (qp_id = dlm25w.qp_modell.id)");
        joins.append(" left join dlm25w.k_m_traeger on (dlm25w.qp_modell.m_traeger = dlm25w.k_m_traeger.id)");
        joins.append(" left join dlm25w.k_m_dim on (dlm25w.qp_modell.m_dim = dlm25w.k_m_dim.id)");
    }

    @Override
    public String getRestriction() {
        return "not coalesce(obsolet, false)";
    }
}
