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

import de.cismet.cids.server.cidslayer.StationInfo;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class HaltungCidsLayer extends Default1505ConsideredCidsLayer {

    //~ Static fields/initializers ---------------------------------------------

    private static final HashMap<String, String> CATALOGUE_NAME_MAP = new HashMap<String, String>();

    static {
        CATALOGUE_NAME_MAP.put("ww_gr", "ww_gr");
    }

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new FgBaCidsLayer object.
     *
     * @param  mc    DOCUMENT ME!
     * @param  user  DOCUMENT ME!
     */
    public HaltungCidsLayer(final MetaClass mc, final User user) {
        super(mc, user, CATALOGUE_NAME_MAP);
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
    public void handleHaltung(final MemberAttributeInfo attr,
            final MetaClass foreignClass,
            final List<String> sb,
            final List<String> columnNamesList,
            final List<String> columnPropertyNamesList,
            final List<String> sqlColumnNamesList,
            final List<String> primitiveColumnTypesList,
            final StringBuilder joins) {
        final HashMap allClasses = foreignClass.getEmptyInstance().getAllClasses();
        final MetaClass geomMc = getGeomClass(allClasses);

        // ba_cd
        sqlGeoField = "geo_field";
        geoField = "geom";
        sb.add(1, "ST_AsEWKb(geom.geo_field) as " + geoField);
        columnNamesList.add(1, "geom");
        sqlColumnNamesList.add(1, "geom.geo_field");
        columnPropertyNamesList.add(1, attr.getName() + ".geom.geo_field");
        primitiveColumnTypesList.add(1, "Geometry");

        sb.add("dlm25wPk_ww_gr1.ww_gr");
        columnNamesList.add("ww_gr");
        sqlColumnNamesList.add("dlm25wPk_ww_gr1.ww_gr");
        columnPropertyNamesList.add("haltung.ba_st.von.route.ww_gr.ww_gr");
        primitiveColumnTypesList.add("java.lang.Integer");

        sb.add("dlm25w.fg_ba.ba_cd");
        columnNamesList.add("ba_cd");
        sqlColumnNamesList.add("dlm25w.fg_ba.ba_cd");
        columnPropertyNamesList.add(attr.getName() + ".von.route.ba_cd");
        primitiveColumnTypesList.add("String");

        sb.add(" von.wert as ba_st_von");
        columnNamesList.add("ba_st_von");
        sqlColumnNamesList.add("von.wert");
        columnPropertyNamesList.add(attr.getName() + ".von.wert");
        primitiveColumnTypesList.add("java.lang.Double");

        sb.add(" bis.wert as ba_st_bis");
        columnNamesList.add("ba_st_bis");
        sqlColumnNamesList.add("bis.wert");
        columnPropertyNamesList.add(attr.getName() + ".bis.wert");
        primitiveColumnTypesList.add("java.lang.Double");

        sb.add("dlm25w.fg_ba.gu_cd");
        columnNamesList.add("gu_cd");
        sqlColumnNamesList.add("dlm25w.fg_ba.gu_cd");
        columnPropertyNamesList.add("haltung.ba_st.von.route.gu_cd");
        primitiveColumnTypesList.add("String");

        sb.add("dlm25w.k_haltung.anfangsobjekt");
        columnNamesList.add("anfangsobjekt");
        sqlColumnNamesList.add("dlm25w.k_haltung.anfangsobjekt");
        columnPropertyNamesList.add("haltung.anfangsobjekt");
        primitiveColumnTypesList.add("java.lang.Integer");

        sb.add("dlm25w.k_haltung.endobjekt");
        columnNamesList.add("endobjekt");
        sqlColumnNamesList.add("dlm25w.k_haltung.endobjekt");
        columnPropertyNamesList.add("haltung.endobjekt");
        primitiveColumnTypesList.add("java.lang.Integer");

        sb.add("dlm25w.k_haltung.objekt_reihenfolge");
        columnNamesList.add("objekt_reihenfolge");
        sqlColumnNamesList.add("dlm25w.k_haltung.objekt_reihenfolge");
        columnPropertyNamesList.add("haltung.objekt_reihenfolge");
        primitiveColumnTypesList.add("String");

        joins.append(" join dlm25w.k_haltung on (haltung = dlm25w.k_haltung.id)");
        joins.append(" join dlm25w.fg_ba_linie on (dlm25w.k_haltung.ba_st = dlm25w.fg_ba_linie.id)");
        joins.append(" join geom on (geom = geom.id)");
        referencedClass.put(geoField, geomMc.getID());
        joins.append(" join dlm25w.fg_ba_punkt von on (von.id = dlm25w.fg_ba_linie.von)");
        joins.append(" join dlm25w.fg_ba_punkt bis on (bis.id = dlm25w.fg_ba_linie.bis)");
        joins.append(" join dlm25w.fg_ba on (von.route = dlm25w.fg_ba.id)");
        joins.append(" left join dlm25w.k_ww_gr dlm25wPk_ww_gr1 on (dlm25w.fg_ba.ww_gr = dlm25wPk_ww_gr1.id)");
    }
}
