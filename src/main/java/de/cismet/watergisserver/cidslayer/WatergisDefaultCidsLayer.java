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

import java.io.Serializable;

import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.cismet.cids.server.cidslayer.CidsLayerInfo;
import de.cismet.cids.server.cidslayer.StationInfo;

//import de.cismet.cismap.commons.gui.attributetable.DefaultAttributeTableRuleSet;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class WatergisDefaultCidsLayer implements CidsLayerInfo, Serializable {

    //~ Static fields/initializers ---------------------------------------------

    private static final String DOMAIN = "DLM25W";

    //~ Instance fields --------------------------------------------------------

    protected Map<String, String> catalogueNameMap = null;

    protected final MetaClass mc;
    protected final Map<String, Boolean> primitiveTypes = new HashMap<String, Boolean>();
    protected final Map<String, Integer> catalogueTypes = new HashMap<String, Integer>();
    protected final Map<String, StationInfo> stationTypes = new HashMap<String, StationInfo>();
    protected final Map<String, Integer> referencedClass = new HashMap<String, Integer>();
    protected boolean useDistinct = false;
    protected User user = null;
    protected String sqlGeoField;
    protected String geoField;
    private String selectionString;
    private String[] columnNames;
    private String[] sqlColumnNames;
    private String[] columnPropertyNames;
    private String[] primitiveColumnTypes;
    private boolean showFgLa = false;
    private boolean firstFgLa = true;
    private boolean additionalGeom = false;
    private boolean inheritedWwGr = false;
    private String additionalJoins = null;
    private boolean showSgSuAttributes = false;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new FgBakAeCidsLayer object.
     *
     * @param  mc    DOCUMENT ME!
     * @param  user  DOCUMENT ME!
     */
    public WatergisDefaultCidsLayer(final MetaClass mc, final User user) {
        this(mc, false, false, null, user);
    }

    /**
     * Creates a new FgBakAeCidsLayer object.
     *
     * @param  mc                  DOCUMENT ME!
     * @param  showSgSuAttributes  DOCUMENT ME!
     * @param  user                DOCUMENT ME!
     */
    public WatergisDefaultCidsLayer(final MetaClass mc, final boolean showSgSuAttributes, final User user) {
        this.mc = mc;
        this.showFgLa = false;
        this.additionalGeom = false;
        this.catalogueNameMap = null;
        this.inheritedWwGr = false;
        this.additionalJoins = null;
        this.showSgSuAttributes = showSgSuAttributes;
        this.user = user;

        if (showFgLa) {
            useDistinct = true;
        }

        init(mc);
    }

    /**
     * Creates a new FgBakAeCidsLayer object.
     *
     * @param  mc                DOCUMENT ME!
     * @param  catalogueNameMap  DOCUMENT ME!
     * @param  user              DOCUMENT ME!
     */
    public WatergisDefaultCidsLayer(final MetaClass mc, final Map<String, String> catalogueNameMap, final User user) {
        this(mc, false, false, catalogueNameMap, user);
    }

    /**
     * Creates a new FgBakAeCidsLayer object.
     *
     * @param  mc               DOCUMENT ME!
     * @param  inheritedWwGr    DOCUMENT ME!
     * @param  additionalJoins  DOCUMENT ME!
     * @param  user             DOCUMENT ME!
     */
    public WatergisDefaultCidsLayer(final MetaClass mc,
            final boolean inheritedWwGr,
            final String additionalJoins,
            final User user) {
        this(mc, false, false, null, inheritedWwGr, additionalJoins, user);
    }

    /**
     * Creates a new FgBakAeCidsLayer object.
     *
     * @param  mc              DOCUMENT ME!
     * @param  showFgLa        DOCUMENT ME!
     * @param  additionalGeom  DOCUMENT ME!
     * @param  user            DOCUMENT ME!
     */
    public WatergisDefaultCidsLayer(final MetaClass mc,
            final boolean showFgLa,
            final boolean additionalGeom,
            final User user) {
        this(mc, showFgLa, additionalGeom, null, user);
    }

    /**
     * Creates a new FgBakAeCidsLayer object.
     *
     * @param  mc                DOCUMENT ME!
     * @param  showFgLa          DOCUMENT ME!
     * @param  additionalGeom    DOCUMENT ME!
     * @param  catalogueNameMap  DOCUMENT ME!
     * @param  user              DOCUMENT ME!
     */
    public WatergisDefaultCidsLayer(final MetaClass mc,
            final boolean showFgLa,
            final boolean additionalGeom,
            final Map<String, String> catalogueNameMap,
            final User user) {
        this(mc, showFgLa, additionalGeom, catalogueNameMap, false, null, user);
    }

    /**
     * Creates a new FgBakAeCidsLayer object.
     *
     * @param  mc                DOCUMENT ME!
     * @param  showFgLa          DOCUMENT ME!
     * @param  additionalGeom    DOCUMENT ME!
     * @param  catalogueNameMap  DOCUMENT ME!
     * @param  inheritedWwGr     DOCUMENT ME!
     * @param  user              DOCUMENT ME!
     */
    public WatergisDefaultCidsLayer(final MetaClass mc,
            final boolean showFgLa,
            final boolean additionalGeom,
            final Map<String, String> catalogueNameMap,
            final boolean inheritedWwGr,
            final User user) {
        this(mc, showFgLa, additionalGeom, catalogueNameMap, inheritedWwGr, null, user);
    }

    /**
     * Creates a new FgBakAeCidsLayer object.
     *
     * @param  mc                DOCUMENT ME!
     * @param  showFgLa          DOCUMENT ME!
     * @param  additionalGeom    DOCUMENT ME!
     * @param  catalogueNameMap  DOCUMENT ME!
     * @param  inheritedWwGr     DOCUMENT ME!
     * @param  additionalJoins   DOCUMENT ME!
     * @param  user              DOCUMENT ME!
     */
    public WatergisDefaultCidsLayer(final MetaClass mc,
            final boolean showFgLa,
            final boolean additionalGeom,
            final Map<String, String> catalogueNameMap,
            final boolean inheritedWwGr,
            final String additionalJoins,
            final User user) {
        this.mc = mc;
        this.showFgLa = showFgLa;
        this.additionalGeom = additionalGeom;
        this.catalogueNameMap = catalogueNameMap;
        this.inheritedWwGr = inheritedWwGr;
        this.additionalJoins = additionalJoins;
        this.user = user;

        if (showFgLa) {
            useDistinct = true;
        }

        init(mc);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  mc  DOCUMENT ME!
     */
    protected void init(final MetaClass mc) {
        final HashMap attrMap = mc.getMemberAttributeInfos();
        final List<String> sb = new ArrayList<String>();
        final StringBuilder joins = new StringBuilder();
//        boolean firstAttr = true;
        final HashMap allClasses = mc.getEmptyInstance().getAllClasses();
        final MetaClass geomMc = getGeomClass(allClasses);
        final List<String> columnNamesList = new ArrayList<String>();
        final List<String> sqlColumnNamesList = new ArrayList<String>();
        final List<String> columnPropertyNamesList = new ArrayList<String>();
        final List<String> primitiveColumnTypesList = new ArrayList<String>();
        int i = 0;
        int lineId = 0;
        int fgLaPosition = -1;
        final String joinExtension = (additionalGeom ? " left" : "");

        for (final Object key : attrMap.keySet()) {
            final MemberAttributeInfo attr = (MemberAttributeInfo)attrMap.get(key);
            if ((user != null) && !hasAttributeReadPermission(mc.getTableName() + "." + attr.getFieldName(), user)) {
                continue;
            }
            if (!attr.getName().equalsIgnoreCase("id") && !attr.isVisible()) {
                continue;
            }

//            if (!firstAttr) {
//                final String tmp = sb.remove(sb.size() - 1);
//                sb.add(tmp + ",");
//            } else {
//                firstAttr = false;
//            }

            if (attr.isForeignKey()
                        && getBeanClassName(allClasses, attr.getForeignKeyClassId()).toLowerCase().endsWith("geom")) {
                sqlGeoField = "geo_field";
                geoField = attr.getName();
                sb.add("ST_AsEWKb(geom.geo_field) as " + geoField);
                columnNamesList.add(attr.getName());
                sqlColumnNamesList.add("geom.geo_field");
                columnPropertyNamesList.add(attr.getName() + ".geo_field");
                joins.append(" join geom on (").append(attr.getFieldName()).append(" = geom.id)");
                primitiveColumnTypesList.add("Geometry");
                referencedClass.put(attr.getName(), attr.getForeignKeyClassId());
            } else if ((attr.getJavaclassname() != null)
                        && attr.getJavaclassname().equalsIgnoreCase("com.vividsolutions.jts.geom.Geometry")) {
                sqlGeoField = mc.getTableName() + "." + attr.getName();
                geoField = attr.getName();
                sb.add("ST_AsEWKb(" + mc.getTableName() + "." + attr.getName() + ") as " + geoField);
                columnNamesList.add(attr.getName());
                sqlColumnNamesList.add(attr.getName());
                columnPropertyNamesList.add(attr.getName());
                primitiveColumnTypesList.add("Geometry");
//                referencedClass.put(attr.getName(), attr.getForeignKeyClassId());
            } else if (attr.isForeignKey()
                        && getBeanClassName(allClasses, attr.getForeignKeyClassId()).toLowerCase().equals(
                            "fg_bak_punkt")) {
                sqlGeoField = "geo_field";
                final String columnName = "bak_st";
                if (!additionalGeom) {
                    geoField = "geom";
                    sb.add(1, "ST_AsEWKb(geom.geo_field) as " + geoField);
                    columnNamesList.add(1, "geom");
                    sqlColumnNamesList.add(1, "geom.geo_field");
                    columnPropertyNamesList.add(1, attr.getName() + ".real_point.geo_field");
                    primitiveColumnTypesList.add(1, "Geometry");
                }
                sb.add("dlm25w.fg_bak.ba_cd");
                columnNamesList.add("fg_bak.ba_cd");
                sqlColumnNamesList.add("fg_bak.ba_cd");           // urspruenglich bis.wert
                columnPropertyNamesList.add(attr.getName() + ".route.ba_cd");
                sb.add(" stat.wert");
                columnNamesList.add(columnName);
                sqlColumnNamesList.add("stat.wert");
                columnPropertyNamesList.add(attr.getName() + ".wert");
                joins.append(joinExtension)
                        .append(" join dlm25w.fg_bak_punkt stat on (stat.id = ")
                        .append(attr.getFieldName())
                        .append(")");
                if (!additionalGeom) {
                    joins.append(" join geom on (real_point = geom.id)");
                    referencedClass.put(geoField, geomMc.getID());
                }
                joins.append(joinExtension).append(" join dlm25w.fg_bak on (stat.route = dlm25w.fg_bak.id)");
                primitiveColumnTypesList.add("String");
                primitiveColumnTypesList.add("java.lang.Double");
                final StationInfo s = new StationInfo(false, true, "dlm25w.fg_bak", ++lineId, "fg_bak.ba_cd");
                stationTypes.put(columnName, s);
            } else if (attr.isForeignKey()
                        && getBeanClassName(allClasses, attr.getForeignKeyClassId()).toLowerCase().equals(
                            "fg_ba_punkt")) {
                sqlGeoField = "geo_field";
                final String columnName = "ba_st";
                if (!additionalGeom) {
                    geoField = "geom";
                    sb.add(1, "ST_AsEWKb(geom.geo_field) as " + geoField);
                    columnNamesList.add(1, "geom");
                    sqlColumnNamesList.add(1, "geom.geo_field");
                    columnPropertyNamesList.add(1, attr.getName() + ".real_point.geo_field");
                    primitiveColumnTypesList.add(1, "Geometry");
                }
                sb.add("dlm25w.fg_ba.ba_cd");
                columnNamesList.add("ba_cd");
                sqlColumnNamesList.add("dlm25w.fg_ba.ba_cd");     // urspruenglich bis.wert
                columnPropertyNamesList.add(attr.getName() + ".route.ba_cd");
                sb.add(" stat.wert");
                columnNamesList.add(columnName);
                sqlColumnNamesList.add("stat.wert");
                columnPropertyNamesList.add(attr.getName() + ".wert");
                joins.append(joinExtension)
                        .append(" join dlm25w.fg_ba_punkt stat on (stat.id = ")
                        .append(attr.getFieldName())
                        .append(")");
                if (!additionalGeom) {
                    joins.append(" join geom on (real_point = geom.id)");
                    referencedClass.put(geoField, geomMc.getID());
                }
                joins.append(joinExtension).append(" join dlm25w.fg_ba on (stat.route = dlm25w.fg_ba.id)");
                primitiveColumnTypesList.add("String");
                primitiveColumnTypesList.add("java.lang.Double");
                final StationInfo s = new StationInfo(false, true, "dlm25w.fg_ba", ++lineId, "ba_cd");
                stationTypes.put(columnName, s);
            } else if (attr.isForeignKey()
                        && getBeanClassName(allClasses, attr.getForeignKeyClassId()).toLowerCase().equals(
                            "fg_la_punkt")) {
                if (this.showFgLa && firstFgLa) {
                    firstFgLa = false;
                    fgLaPosition = sb.size();
                    continue;
                }
                if (firstFgLa) {
                    sqlGeoField = "geo_field";
                    final String columnName = "la_st";
                    if (!additionalGeom) {
                        geoField = "geom";
                        sb.add(1, "ST_AsEWKb(geom.geo_field) as " + geoField);
                        columnNamesList.add(1, "geom");
                        sqlColumnNamesList.add(1, "geom.geo_field");
                        columnPropertyNamesList.add(1, attr.getName() + ".real_point.geo_field");
                        primitiveColumnTypesList.add(1, "Geometry");
                    }
                    sb.add("dlm25w.fg_la.la_cd");
                    columnNamesList.add("la_cd");
                    sqlColumnNamesList.add("dlm25w.fg_la.la_cd"); // urspruenglich bis.wert
                    columnPropertyNamesList.add(attr.getName() + ".route.la_cd");
                    sb.add(" statl.wert");
                    columnNamesList.add(columnName);
                    sqlColumnNamesList.add("statl.wert");
                    columnPropertyNamesList.add(attr.getName() + ".wert");
                    joins.append(joinExtension)
                            .append(" join dlm25w.fg_la_punkt statl on (statl.id = ")
                            .append(attr.getFieldName())
                            .append(")");
                    if (!additionalGeom) {
                        joins.append(" join geom on (real_point = geom.id)");
                        referencedClass.put(geoField, geomMc.getID());
                    }
                    joins.append(joinExtension).append(" join dlm25w.fg_la on (statl.route = dlm25w.fg_la.id)");
                    primitiveColumnTypesList.add("BigDecimal");
                    primitiveColumnTypesList.add("java.lang.Double");
                    final StationInfo s = new StationInfo(false, true, "dlm25w.fg_la", ++lineId, "la_cd");
                    stationTypes.put(columnName, s);
                } else {
                    sqlGeoField = "geo_field";
                    final String columnName = "station";
                    if (!additionalGeom) {
                        geoField = "geom";
                        sb.add(1, "ST_AsEWKb(geom.geo_field) as " + geoField);
                        columnNamesList.add(1, "geom");
                        sqlColumnNamesList.add(1, "geom.geo_field");
                        columnPropertyNamesList.add(1, attr.getName() + ".real_point.geo_field");
                        primitiveColumnTypesList.add(1, "Geometry");
                    }
                    sb.add("dlm25w.fg_la.la_cd as gwk_lawa");
                    columnNamesList.add("gwk_lawa");
                    sqlColumnNamesList.add("dlm25w.fg_la.la_cd"); // urspruenglich bis.wert
                    columnPropertyNamesList.add(attr.getName() + ".route.la_cd");
                    sb.add(" statl.wert as station");
                    columnNamesList.add(columnName);
                    sqlColumnNamesList.add("statl.wert");
                    columnPropertyNamesList.add(attr.getName() + ".wert");
                    joins.append(joinExtension)
                            .append(" join dlm25w.fg_la_punkt statl on (statl.id = ")
                            .append(attr.getFieldName())
                            .append(")");
                    if (!additionalGeom) {
                        joins.append(" join geom on (real_point = geom.id)");
                        referencedClass.put(geoField, geomMc.getID());
                    }
                    joins.append(joinExtension).append(" join dlm25w.fg_la on (statl.route = dlm25w.fg_la.id)");
                    primitiveColumnTypesList.add("BigDecimal");
                    primitiveColumnTypesList.add("java.lang.Double");
                    final StationInfo s = new StationInfo(false, true, "dlm25w.fg_la", ++lineId, "la_cd");
                    stationTypes.put(columnName, s);
                }
            } else if (attr.isForeignKey()
                        && getBeanClassName(allClasses, attr.getForeignKeyClassId()).toLowerCase().equals(
                            "fg_bak_linie")) {
                sqlGeoField = "geo_field";
                if (!additionalGeom) {
                    geoField = "geom";
                    sb.add(1, "ST_AsEWKb(geom.geo_field) as " + geoField);
                    columnNamesList.add(1, "geom");
                    sqlColumnNamesList.add(1, "geom.geo_field");
                    columnPropertyNamesList.add(1, attr.getName() + ".geom.geo_field");
                    primitiveColumnTypesList.add(1, "Geometry");
                }
                sb.add("dlm25w.fg_bak.ba_cd");
                columnNamesList.add("ba_cd");
                sqlColumnNamesList.add("dlm25w.fg_bak.ba_cd");
                columnPropertyNamesList.add(attr.getName() + ".von.route.ba_cd");
                sb.add(" von.wert as bak_st_von");
                columnNamesList.add("bak_st_von");
                sqlColumnNamesList.add("von.wert");
                columnPropertyNamesList.add(attr.getName() + ".von.wert");
                sb.add(" bis.wert as bak_st_bis");
                columnNamesList.add("bak_st_bis");
                sqlColumnNamesList.add("bis.wert");
                columnPropertyNamesList.add(attr.getName() + ".bis.wert");
                joins.append(joinExtension)
                        .append(" join dlm25w.fg_bak_linie on (")
                        .append(attr.getFieldName())
                        .append(" = dlm25w.fg_bak_linie.id)");
                if (!additionalGeom) {
                    joins.append(" join geom on (geom = geom.id)");
                    referencedClass.put(geoField, geomMc.getID());
                }
                joins.append(joinExtension)
                        .append(" join dlm25w.fg_bak_punkt von on (von.id = dlm25w.fg_bak_linie.von)");
                joins.append(joinExtension)
                        .append(" join dlm25w.fg_bak_punkt bis on (bis.id = dlm25w.fg_bak_linie.bis)");
                joins.append(joinExtension).append(" join dlm25w.fg_bak on (bis.route = dlm25w.fg_bak.id)");
                primitiveColumnTypesList.add("String");
                primitiveColumnTypesList.add("java.lang.Double");
                primitiveColumnTypesList.add("java.lang.Double");
                StationInfo s = new StationInfo(true, true, "dlm25w.fg_bak", ++lineId, "ba_cd");
                stationTypes.put("bak_st_von", s);
                s = new StationInfo(true, false, "dlm25w.fg_bak", lineId, "ba_cd");
                stationTypes.put("bak_st_bis", s);
            } else if (attr.isForeignKey()
                        && getBeanClassName(allClasses, attr.getForeignKeyClassId()).toLowerCase().equals(
                            "fg_ba_linie")) {
                sqlGeoField = "geo_field";
                if (!additionalGeom) {
                    geoField = "geom";
                    sb.add(1, "ST_AsEWKb(geom.geo_field) as " + geoField);
                    columnNamesList.add(1, "geom");
                    sqlColumnNamesList.add(1, "geom.geo_field");
                    columnPropertyNamesList.add(1, attr.getName() + ".geom.geo_field");
                    primitiveColumnTypesList.add(1, "Geometry");
                }
                sb.add("dlm25w.fg_ba.ba_cd");
                columnNamesList.add("ba_cd");
                sqlColumnNamesList.add("dlm25w.fg_ba.ba_cd");
                columnPropertyNamesList.add(attr.getName() + ".von.route.ba_cd");
                sb.add(" von.wert as ba_st_von");
                columnNamesList.add("ba_st_von");
                sqlColumnNamesList.add("von.wert");
                columnPropertyNamesList.add(attr.getName() + ".von.wert");
                sb.add(" bis.wert as ba_st_bis");
                columnNamesList.add("ba_st_bis");
                sqlColumnNamesList.add("bis.wert");
                columnPropertyNamesList.add(attr.getName() + ".bis.wert");
                joins.append(joinExtension)
                        .append(" join dlm25w.fg_ba_linie on (")
                        .append(attr.getFieldName())
                        .append(" = dlm25w.fg_ba_linie.id)");
                if (!additionalGeom) {
                    joins.append(" join geom on (geom = geom.id)");
                    referencedClass.put(geoField, geomMc.getID());
                }
                joins.append(joinExtension).append(" join dlm25w.fg_ba_punkt von on (von.id = dlm25w.fg_ba_linie.von)");
                joins.append(joinExtension).append(" join dlm25w.fg_ba_punkt bis on (bis.id = dlm25w.fg_ba_linie.bis)");
                joins.append(joinExtension).append(" join dlm25w.fg_ba on (von.route = dlm25w.fg_ba.id)");
                primitiveColumnTypesList.add("String");
                primitiveColumnTypesList.add("java.lang.Double");
                primitiveColumnTypesList.add("java.lang.Double");
                StationInfo s = new StationInfo(true, true, "dlm25w.fg_ba", ++lineId, "ba_cd");
                stationTypes.put("ba_st_von", s);
                s = new StationInfo(true, false, "dlm25w.fg_ba", lineId, "ba_cd");
                stationTypes.put("ba_st_bis", s);
            } else if (attr.isForeignKey()
                        && getBeanClassName(allClasses, attr.getForeignKeyClassId()).toLowerCase().equals(
                            "sg_su_linie")) {
                sqlGeoField = "geo_field";
                if (!additionalGeom) {
                    geoField = "geom";
                    sb.add(1, "ST_AsEWKb(geom.geo_field) as " + geoField);
                    columnNamesList.add(1, "geom");
                    sqlColumnNamesList.add(1, "geom.geo_field");
                    columnPropertyNamesList.add(1, attr.getName() + ".geom.geo_field");
                    primitiveColumnTypesList.add(1, "Geometry");
                }
                sb.add("dlm25w.sg_su.su_cd");
                columnNamesList.add("su_cd");
                sqlColumnNamesList.add("dlm25w.sg_su.su_cd");
                columnPropertyNamesList.add(attr.getName() + "route.su_cd");
                sb.add(" von.wert as su_st_von");
                columnNamesList.add("su_st_von");
                sqlColumnNamesList.add("von.wert");
                columnPropertyNamesList.add(attr.getName() + ".von.wert");
                sb.add(" bis.wert as su_st_bis");
                columnNamesList.add("su_st_bis");
                sqlColumnNamesList.add("bis.wert");
                columnPropertyNamesList.add(attr.getName() + ".bis.wert");

                primitiveColumnTypesList.add("String");
                primitiveColumnTypesList.add("java.lang.Double");
                primitiveColumnTypesList.add("java.lang.Double");

                if (showSgSuAttributes) {
                    sb.add("dlm25w.sg_su.see_gn");
                    columnNamesList.add("see_gn");
                    sqlColumnNamesList.add("dlm25w.sg_su.see_gn");
                    columnPropertyNamesList.add(attr.getName() + ".see_gn");
                    primitiveColumnTypesList.add("String");

                    sb.add("dlm25w.sg_su.see_lawa");
                    columnNamesList.add("see_lawa");
                    sqlColumnNamesList.add("dlm25w.sg_su.see_lawa");
                    columnPropertyNamesList.add(attr.getName() + ".see_lawa");
                    primitiveColumnTypesList.add("String");

                    sb.add("dlm25w.sg_su.see_sp");
                    columnNamesList.add("see_sp");
                    sqlColumnNamesList.add("dlm25w.sg_su.see_sp");
                    columnPropertyNamesList.add(attr.getName() + ".see_sp");
                    primitiveColumnTypesList.add("String");
                }

                joins.append(joinExtension)
                        .append(" join dlm25w.sg_su_linie on (")
                        .append(attr.getFieldName())
                        .append(" = dlm25w.sg_su_linie.id)");
                if (!additionalGeom) {
                    joins.append(" join geom on (geom = geom.id)");
                    referencedClass.put(geoField, geomMc.getID());
                }
                joins.append(joinExtension).append(" join dlm25w.sg_su_punkt von on (von.id = dlm25w.sg_su_linie.von)");
                joins.append(joinExtension).append(" join dlm25w.sg_su_punkt bis on (bis.id = dlm25w.sg_su_linie.bis)");
                joins.append(joinExtension).append(" join dlm25w.sg_su on (bis.route = dlm25w.sg_su.id)");
                StationInfo s = new StationInfo(true, true, "dlm25w.sg_su", ++lineId, "su_cd");
                stationTypes.put("su_st_von", s);
                s = new StationInfo(true, false, "dlm25w.sg_su", lineId, "su_cd");
                stationTypes.put("su_st_bis", s);
            } else if (attr.isForeignKey()
                        && getBeanClassName(allClasses, attr.getForeignKeyClassId()).toLowerCase().equals(
                            "fg_la_linie")) {
                sqlGeoField = "geo_field";
                if (!additionalGeom) {
                    geoField = "geom";
                    sb.add(1, "ST_AsEWKb(geom.geo_field) as " + geoField);
                    columnNamesList.add(1, "geom");
                    sqlColumnNamesList.add(1, "geom.geo_field");
                    columnPropertyNamesList.add(1, attr.getName() + ".geom.geo_field");
                    primitiveColumnTypesList.add(1, "Geometry");
                }
//                sb.add("dlm25w.fg_la.la_cd");
                sb.add("dlm25w.k_gwk_lawa.la_cd");
                columnNamesList.add("la_cd");
//                sqlColumnNamesList.add("dlm25w.fg_la.la_cd");
//                columnPropertyNamesList.add(attr.getName() + ".von.route.la_cd");
                sqlColumnNamesList.add("dlm25w.k_gwk_lawa.la_cd");
                columnPropertyNamesList.add(attr.getName() + ".von.route.la_cd.la_cd");
                sb.add(" von.wert as la_st_von");
                columnNamesList.add("la_st_von");
                sqlColumnNamesList.add("von.wert");
                columnPropertyNamesList.add(attr.getName() + ".von.wert");
                sb.add(" bis.wert as la_st_bis");
                columnNamesList.add("la_st_bis");
                sqlColumnNamesList.add("bis.wert");
                columnPropertyNamesList.add(attr.getName() + ".bis.wert");
                joins.append(joinExtension)
                        .append(" join dlm25w.fg_la_linie on (")
                        .append(attr.getFieldName())
                        .append(" = dlm25w.fg_la_linie.id)");
                if (!additionalGeom) {
                    joins.append(" join geom on (geom = geom.id)");
                    referencedClass.put(geoField, geomMc.getID());
                }
                joins.append(joinExtension).append(" join dlm25w.fg_la_punkt von on (von.id = dlm25w.fg_la_linie.von)");
                joins.append(joinExtension).append(" join dlm25w.fg_la_punkt bis on (bis.id = dlm25w.fg_la_linie.bis)");
                joins.append(joinExtension).append(" join dlm25w.fg_la on (bis.route = dlm25w.fg_la.id)");
                joins.append(joinExtension)
                        .append(" join dlm25w.k_gwk_lawa on (dlm25w.k_gwk_lawa.id")
                        .append(" = dlm25w.fg_la.la_cd)");
                primitiveColumnTypesList.add("BigDecimal");
                primitiveColumnTypesList.add("java.lang.Double");
                primitiveColumnTypesList.add("java.lang.Double");
                StationInfo s = new StationInfo(true, true, "dlm25w.fg_la", ++lineId, "la_cd");
                stationTypes.put("la_st_von", s);
                s = new StationInfo(true, false, "dlm25w.fg_la", lineId, "la_cd");
                stationTypes.put("la_st_bis", s);
            } else if (attr.isForeignKey()
                        && getBeanClassName(allClasses, attr.getForeignKeyClassId()).toLowerCase().equals(
                            "fg_lak_linie") && !showFgLa) {
                sqlGeoField = "geo_field";
                if (!additionalGeom) {
                    geoField = "geom";
                    sb.add(1, "ST_AsEWKb(geom.geo_field) as " + geoField);
                    columnNamesList.add(1, "geom");
                    sqlColumnNamesList.add(1, "geom.geo_field");
                    columnPropertyNamesList.add(1, attr.getName() + ".geom.geo_field");
                    primitiveColumnTypesList.add(1, "Geometry");
                }
//                sb.add("dlm25w.fg_lak.la_cd");
                sb.add("dlm25w.k_gwk_lawa.la_cd");
                columnNamesList.add("la_cd");
//                sqlColumnNamesList.add("dlm25w.fg_lak.la_cd");
//                columnPropertyNamesList.add(attr.getName() + ".von.route.la_cd");
                sqlColumnNamesList.add("dlm25w.k_gwk_lawa.la_cd");
                columnPropertyNamesList.add(attr.getName() + ".von.route.la_cd.la_cd");
                sb.add(" von.wert as lak_st_von");
                columnNamesList.add("lak_st_von");
                sqlColumnNamesList.add("von.wert");
                columnPropertyNamesList.add(attr.getName() + ".von.wert");
                sb.add(" bis.wert as lak_st_bis");
                columnNamesList.add("lak_st_bis");
                sqlColumnNamesList.add("bis.wert");
                columnPropertyNamesList.add(attr.getName() + ".bis.wert");
                joins.append(joinExtension)
                        .append(" join dlm25w.fg_lak_linie on (")
                        .append(attr.getFieldName())
                        .append(" = dlm25w.fg_lak_linie.id)");
                if (!additionalGeom) {
                    joins.append(" join geom on (geom = geom.id)");
                    referencedClass.put(geoField, geomMc.getID());
                }
                joins.append(joinExtension)
                        .append(" join dlm25w.fg_lak_punkt von on (von.id = dlm25w.fg_lak_linie.von)");
                joins.append(joinExtension)
                        .append(" join dlm25w.fg_lak_punkt bis on (bis.id = dlm25w.fg_lak_linie.bis)");
                joins.append(joinExtension).append(" join dlm25w.fg_lak on (bis.route = dlm25w.fg_lak.id)");
                joins.append(joinExtension)
                        .append(" join dlm25w.k_gwk_lawa on (dlm25w.k_gwk_lawa.id")
                        .append(" = dlm25w.fg_lak.la_cd)");
                primitiveColumnTypesList.add("BigDecimal");
                primitiveColumnTypesList.add("java.lang.Double");
                primitiveColumnTypesList.add("java.lang.Double");
                StationInfo s = new StationInfo(true, true, "dlm25w.fg_lak", ++lineId, "la_cd");
                stationTypes.put("lak_st_von", s);
                s = new StationInfo(true, false, "dlm25w.fg_lak", lineId, "la_cd");
                stationTypes.put("lak_st_bis", s);
            } else if (attr.isForeignKey()
                        && getBeanClassName(allClasses, attr.getForeignKeyClassId()).toLowerCase().equals(
                            "fg_ba_duv_linie")) {
                sqlGeoField = "geo_field";
                if (!additionalGeom) {
                    geoField = "geom";
                    sb.add(1, "ST_AsEWKb(geom.geo_field) as " + geoField);
                    columnNamesList.add(1, "geom");
                    sqlColumnNamesList.add(1, "geom.geo_field");
                    columnPropertyNamesList.add(1, attr.getName() + ".geom.geo_field");
                    primitiveColumnTypesList.add(1, "Geometry");
                }
                sb.add("duv.fg_ba_duv.ba_cd");
                columnNamesList.add("ba_cd");
                sqlColumnNamesList.add("duv.fg_ba_duv.ba_cd");
                columnPropertyNamesList.add(attr.getName() + ".von.route.ba_cd");
                sb.add(" von.wert as ba_st_von");
                columnNamesList.add("ba_st_von");
                sqlColumnNamesList.add("von.wert");
                columnPropertyNamesList.add(attr.getName() + ".von.wert");
                sb.add(" bis.wert as ba_st_bis");
                columnNamesList.add("ba_st_bis");
                sqlColumnNamesList.add("bis.wert");
                columnPropertyNamesList.add(attr.getName() + ".bis.wert");
                joins.append(joinExtension)
                        .append(" join duv.fg_ba_duv_linie on (")
                        .append(attr.getFieldName())
                        .append(" = duv.fg_ba_duv_linie.id)");
                if (!additionalGeom) {
                    joins.append(" join geom on (geom = geom.id)");
                    referencedClass.put(geoField, geomMc.getID());
                }
                joins.append(joinExtension)
                        .append(" join duv.fg_ba_duv_punkt von on (von.id = duv.fg_ba_duv_linie.von)");
                joins.append(joinExtension)
                        .append(" join duv.fg_ba_duv_punkt bis on (bis.id = duv.fg_ba_duv_linie.bis)");
                joins.append(joinExtension).append(" join duv.fg_ba_duv on (von.route = duv.fg_ba_duv.id)");
                primitiveColumnTypesList.add("String");
                primitiveColumnTypesList.add("java.lang.Double");
                primitiveColumnTypesList.add("java.lang.Double");
                StationInfo s = new StationInfo(true, true, "duv.fg_ba_duv", ++lineId, "ba_cd");
                stationTypes.put("ba_st_von", s);
                s = new StationInfo(true, false, "duv.fg_ba_duv", lineId, "ba_cd");
                stationTypes.put("ba_st_bis", s);
            } else if (attr.isForeignKey()
                        && getBeanClassName(allClasses, attr.getForeignKeyClassId()).toLowerCase().equals(
                            "sg_umring_linie")) {
                sqlGeoField = "geo_field";
                if (!additionalGeom) {
                    geoField = "geom";
                    sb.add(1, "ST_AsEWKb(geom.geo_field) as " + geoField);
                    columnNamesList.add(1, "geom");
                    sqlColumnNamesList.add(1, "geom.geo_field");
                    columnPropertyNamesList.add(1, attr.getName() + ".geom.geo_field");
                    primitiveColumnTypesList.add(1, "Geometry");
                }
                sb.add("duv.sg_umring.see_id");
                columnNamesList.add("see_id");
                sqlColumnNamesList.add("duv.sg_umring.see_id");
                columnPropertyNamesList.add(attr.getName() + ".von.route.see_id");
                sb.add(" von.wert as see_st_von");
                columnNamesList.add("see_st_von");
                sqlColumnNamesList.add("von.wert");
                columnPropertyNamesList.add(attr.getName() + ".von.wert");
                sb.add(" bis.wert as see_st_bis");
                columnNamesList.add("see_st_bis");
                sqlColumnNamesList.add("bis.wert");
                columnPropertyNamesList.add(attr.getName() + ".bis.wert");
                joins.append(joinExtension)
                        .append(" join duv.sg_umring_linie on (")
                        .append(attr.getFieldName())
                        .append(" = duv.sg_umring_linie.id)");
                if (!additionalGeom) {
                    joins.append(" join geom on (geom = geom.id)");
                    referencedClass.put(geoField, geomMc.getID());
                }
                joins.append(joinExtension)
                        .append(" join duv.sg_umring_punkt von on (von.id = duv.sg_umring_linie.von)");
                joins.append(joinExtension)
                        .append(" join duv.sg_umring_punkt bis on (bis.id = duv.sg_umring_linie.bis)");
                joins.append(joinExtension).append(" join duv.sg_umring on (von.route = duv.sg_umring.id)");
                primitiveColumnTypesList.add("java.lang.Integer");
                primitiveColumnTypesList.add("java.lang.Double");
                primitiveColumnTypesList.add("java.lang.Double");
                StationInfo s = new StationInfo(true, true, "duv.sg_umring", ++lineId, "see_id");
                stationTypes.put("ba_st_von", s);
                s = new StationInfo(true, false, "duv.sg_umring", lineId, "see_id");
                stationTypes.put("ba_st_bis", s);
            } else if (attr.isForeignKey()
                        && getBeanClassName(allClasses, attr.getForeignKeyClassId()).toLowerCase().equals(
                            "fg_ba_duv_ges_linie")) {
                sqlGeoField = "geo_field";
                if (!additionalGeom) {
                    geoField = "geom";
                    sb.add(1, "ST_AsEWKb(geom.geo_field) as " + geoField);
                    columnNamesList.add(1, "geom");
                    sqlColumnNamesList.add(1, "geom.geo_field");
                    columnPropertyNamesList.add(1, attr.getName() + ".geom.geo_field");
                    primitiveColumnTypesList.add(1, "Geometry");
                }
                sb.add("duv.fg_ba_duv_ges.ba_cd");
                columnNamesList.add("ba_cd");
                sqlColumnNamesList.add("duv.fg_ba_duv_ges.ba_cd");
                columnPropertyNamesList.add(attr.getName() + ".von.route.ba_cd");
                sb.add(" von.wert as ba_st_von");
                columnNamesList.add("ba_st_von");
                sqlColumnNamesList.add("von.wert");
                columnPropertyNamesList.add(attr.getName() + ".von.wert");
                sb.add(" bis.wert as ba_st_bis");
                columnNamesList.add("ba_st_bis");
                sqlColumnNamesList.add("bis.wert");
                columnPropertyNamesList.add(attr.getName() + ".bis.wert");
                joins.append(joinExtension)
                        .append(" join duv.fg_ba_duv_ges_linie on (")
                        .append(attr.getFieldName())
                        .append(" = duv.fg_ba_duv_ges_linie.id)");
                if (!additionalGeom) {
                    joins.append(" join geom on (geom = geom.id)");
                    referencedClass.put(geoField, geomMc.getID());
                }
                joins.append(joinExtension)
                        .append(" join duv.fg_ba_duv_ges_punkt von on (von.id = duv.fg_ba_duv_ges_linie.von)");
                joins.append(joinExtension)
                        .append(" join duv.fg_ba_duv_ges_punkt bis on (bis.id = duv.fg_ba_duv_ges_linie.bis)");
                joins.append(joinExtension).append(" join duv.fg_ba_duv_ges on (von.route = duv.fg_ba_duv_ges.id)");
                primitiveColumnTypesList.add("String");
                primitiveColumnTypesList.add("java.lang.Double");
                primitiveColumnTypesList.add("java.lang.Double");
                StationInfo s = new StationInfo(true, true, "duv.fg_ba_duv_ges", ++lineId, "ba_cd");
                stationTypes.put("ba_st_von", s);
                s = new StationInfo(true, false, "duv.fg_ba_duv_ges", lineId, "ba_cd");
                stationTypes.put("ba_st_bis", s);
            } else if (attr.isForeignKey()) {
                if (inheritedWwGr
                            && getBeanClassName(allClasses, attr.getForeignKeyClassId()).toLowerCase().endsWith(
                                "k_ww_gr")) {
                    final MetaClass foreignClass = getBeanClass(allClasses, attr.getForeignKeyClassId());
                    final String namePropertyName = "ww_gr";
                    final ObjectAttribute nameAttr = (ObjectAttribute)foreignClass.getEmptyInstance()
                                .getAttribute(namePropertyName);
                    final String alias = "dlm25wPk_ww_gr1";
                    sb.add(alias + "." + nameAttr.getMai().getFieldName());
                    columnNamesList.add(attr.getName());
                    sqlColumnNamesList.add(alias + "." + nameAttr.getMai().getFieldName());
                    columnPropertyNamesList.add(attr.getName());
                    catalogueTypes.put(columnNamesList.get(columnNamesList.size() - 1), attr.getForeignKeyClassId());
                    primitiveColumnTypesList.add(nameAttr.getMai().getJavaclassname());
                } else if (!(attr.isForeignKey()
                                && getBeanClassName(allClasses, attr.getForeignKeyClassId()).toLowerCase().endsWith(
                                    "geom"))) {
                    final MetaClass foreignClass = getBeanClass(allClasses, attr.getForeignKeyClassId());
                    try {
                        final String methodName = "handle" + attr.getName().substring(0, 1).toUpperCase()
                                    + attr.getName().substring(1);
                        final Method m = this.getClass()
                                    .getMethod(
                                        methodName,
                                        MemberAttributeInfo.class,
                                        MetaClass.class,
                                        List.class,
                                        List.class,
                                        List.class,
                                        List.class,
                                        List.class,
                                        StringBuilder.class);
                        m.invoke(
                            this,
                            attr,
                            foreignClass,
                            sb,
                            columnNamesList,
                            columnPropertyNamesList,
                            sqlColumnNamesList,
                            primitiveColumnTypesList,
                            joins);
                    } catch (NoSuchMethodException ex) {
                        handleCatalogue(
                            attr,
                            foreignClass,
                            sb,
                            columnNamesList,
                            columnPropertyNamesList,
                            sqlColumnNamesList,
                            primitiveColumnTypesList,
                            joins);
                    } catch (Exception e) {
                        // todo LOG
                    }
                } else {
                    final String tmp = sb.remove(sb.size() - 1);
                    sb.add(tmp.substring(0, tmp.length() - 1));
//                    sb.deleteCharAt(sb.length() - 1);
                }
            } else {
                final String fieldRestriction = getFieldRestriction(mc.getTableName() + "." + attr.getFieldName());

                if (fieldRestriction != null) {
                    sb.add("case when " + fieldRestriction + " then " + mc.getTableName() + "." + attr.getFieldName()
                                + " else null end as" + attr.getName());
                } else {
                    sb.add(mc.getTableName() + "." + attr.getFieldName());
                }
                columnNamesList.add(attr.getName());
                sqlColumnNamesList.add(mc.getTableName() + "." + attr.getFieldName());
                columnPropertyNamesList.add(attr.getName());
                primitiveTypes.put(columnNamesList.get(columnNamesList.size() - 1), Boolean.TRUE);
                primitiveColumnTypesList.add(attr.getJavaclassname());
            }

            ++i;
        }

        if (showFgLa) {
            if (fgLaPosition == -1) {
                fgLaPosition = sb.size();
            }
            sb.add(fgLaPosition, " la_lp.la_cd as la_cd ");
            sb.add(fgLaPosition + 1, " la_lp.statval as la_st ");
            columnNamesList.add(fgLaPosition, "la_cd");
            columnNamesList.add(fgLaPosition + 1, "la_st");
            sqlColumnNamesList.add(fgLaPosition, "la_cd");
            sqlColumnNamesList.add(fgLaPosition + 1, "la_st");
            columnPropertyNamesList.add(fgLaPosition, "la_cd");
            columnPropertyNamesList.add(fgLaPosition + 1, "la_st");

            final String baCd = "dlm25w.fg_ba.ba_cd";
            final String value = "stat.wert";
            joins.append(" left join dlm25w.fg_la_locate_point la_lp on (la_lp.wert = ")
                    .append(value)
                    .append(" and la_lp.ba_cd = ")
                    .append(baCd)
                    .append(")");
            primitiveColumnTypesList.add(fgLaPosition, "BigDecimal");
            primitiveColumnTypesList.add(fgLaPosition + 1, "java.lang.Double");
        }

        for (int n = 0; n < (sb.size() - 1); ++n) {
            final String tmp = sb.remove(n);
            sb.add(n, tmp + ",");
        }

        sb.add(" from " + mc.getTableName());
        sb.add(joins.toString());
        if (additionalJoins != null) {
            sb.add(additionalJoins);
        }

        selectionString = "Select ";

        if (useDistinct) {
            selectionString += "distinct ";
        }
        for (final String tmp : sb) {
            if (selectionString == null) {
                selectionString = tmp;
            } else {
                selectionString += tmp;
            }
        }

//        selectionString = sb.toString();
        columnNames = columnNamesList.toArray(new String[columnNamesList.size()]);
        sqlColumnNames = sqlColumnNamesList.toArray(new String[sqlColumnNamesList.size()]);
        columnPropertyNames = columnPropertyNamesList.toArray(new String[columnPropertyNamesList.size()]);
        primitiveColumnTypes = primitiveColumnTypesList.toArray(new String[primitiveColumnTypesList.size()]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   column  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected String getFieldRestriction(final String column) {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   column  DOCUMENT ME!
     * @param   user    DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected boolean hasAttributeReadPermission(final String column, final User user) {
        return true;
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
    protected void handleCatalogue(final MemberAttributeInfo attr,
            final MetaClass foreignClass,
            final List<String> sb,
            final List<String> columnNamesList,
            final List<String> columnPropertyNamesList,
            final List<String> sqlColumnNamesList,
            final List<String> primitiveColumnTypesList,
            final StringBuilder joins) {
        String namePropertyName = "name";

        if ((catalogueNameMap != null) && (catalogueNameMap.get(attr.getName()) != null)) {
            namePropertyName = catalogueNameMap.get(attr.getName());
        }
        final ObjectAttribute nameAttr = (ObjectAttribute)foreignClass.getEmptyInstance()
                    .getAttribute(namePropertyName);
        final String fieldRestriction = getFieldRestriction(mc.getTableName() + "." + attr.getFieldName());

        if (nameAttr != null) {
            final String alias = addLeftJoin(
                    joins,
                    foreignClass.getTableName(),
                    mc.getTableName()
                            + "."
                            + attr.getFieldName(),
                    "id");
            if (fieldRestriction != null) {
                sb.add("case when " + fieldRestriction + " then " + alias + "." + nameAttr.getMai().getFieldName()
                            + " else null end as " + attr.getName());
            } else {
                sb.add(alias + "." + nameAttr.getMai().getFieldName() + " as " + attr.getName());
            }
            columnNamesList.add(attr.getName());
            sqlColumnNamesList.add(alias + "." + nameAttr.getMai().getFieldName());
            columnPropertyNamesList.add(attr.getName());
            catalogueTypes.put(columnNamesList.get(columnNamesList.size() - 1), attr.getForeignKeyClassId());
            primitiveColumnTypesList.add(nameAttr.getMai().getJavaclassname());
        } else {
            if (fieldRestriction != null) {
                sb.add("case when " + fieldRestriction + " then " + mc.getTableName() + "." + attr.getFieldName()
                            + " else null end as " + attr.getName());
            } else {
                sb.add(mc.getTableName() + "." + attr.getFieldName() + " as " + attr.getName());
            }
            columnNamesList.add(attr.getName());
            sqlColumnNamesList.add(mc.getTableName() + "." + attr.getFieldName());
            columnPropertyNamesList.add(attr.getName() + ".name");
            catalogueTypes.put(columnNamesList.get(columnNamesList.size() - 1), attr.getForeignKeyClassId());
            primitiveColumnTypesList.add("java.lang.String");
            // primitiveColumnTypesList.add(attr.getJavaclassname());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   joins              DOCUMENT ME!
     * @param   table              DOCUMENT ME!
     * @param   onClauseLeftSide   DOCUMENT ME!
     * @param   onClauseRightSide  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected String addLeftJoin(final StringBuilder joins,
            final String table,
            final String onClauseLeftSide,
            final String onClauseRightSide) {
        String alias = "";
        int counter = 0;

        do {
            alias = table.replace('.', 'P') + (++counter);
        } while (joins.indexOf(alias) != -1);

        joins.append(" left join ")
                .append(table)
                .append(" ")
                .append(alias)
                .append(" on (")
                .append(onClauseLeftSide)
                .append(" = ")
                .append(alias)
                .append(".")
                .append(onClauseRightSide)
                .append(")");

        return alias;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   allClasses  DOCUMENT ME!
     * @param   classId     DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected String getBeanClassName(final HashMap<String, MetaClass> allClasses, final int classId) {
        return allClasses.get(DOMAIN + classId).toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param   allClasses  DOCUMENT ME!
     * @param   classId     DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected MetaClass getBeanClass(final HashMap<String, MetaClass> allClasses, final int classId) {
        return allClasses.get(DOMAIN + classId);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   allClasses  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected MetaClass getGeomClass(final HashMap<String, MetaClass> allClasses) {
        for (final MetaClass cl : allClasses.values()) {
            if (cl.getName().toLowerCase().equals("geom")) {
                return cl;
            }
        }

        return null;
    }

    @Override
    public String getIdField() {
        return "id";
    }

    @Override
    public String getSqlGeoField() {
        return sqlGeoField;
    }

    @Override
    public String getGeoField() {
        return geoField;
    }

    @Override
    public String getSelectString() {
        return selectionString;
    }

    @Override
    public String[] getColumnNames() {
        return columnNames;
    }

    @Override
    public String[] getPrimitiveColumnTypes() {
        return primitiveColumnTypes;
    }

    @Override
    public boolean isPrimitive(final String column) {
        final Boolean primitive = primitiveTypes.get(column);

        return ((primitive != null) ? primitive.booleanValue() : false);
    }

    @Override
    public boolean isCatalogue(final String column) {
        final Integer catalogue = catalogueTypes.get(column);

        return ((catalogue != null) ? true : false);
    }

    @Override
    public Integer getCatalogueClass(final String column) {
        final Integer catalogueClass = catalogueTypes.get(column);

        return catalogueClass;
    }

    @Override
    public boolean isStation(final String column) {
        final StationInfo stationInfo = stationTypes.get(column);

        return ((stationInfo != null) ? true : false);
    }

    @Override
    public StationInfo getStationInfo(final String column) {
        final StationInfo stationInfo = stationTypes.get(column);

        return stationInfo;
    }

    @Override
    public String[] getColumnPropertyNames() {
        return columnPropertyNames;
    }

//    @Override
//    public DefaultAttributeTableRuleSet getAttributeTableRuleSet() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }

    @Override
    public int getReferencedCidsClass(final String column) {
        return referencedClass.get(column);
    }

    @Override
    public boolean isReferenceToCidsClass(final String column) {
        final Integer classId = referencedClass.get(column);

        return ((classId != null) ? true : false);
    }

    @Override
    public String[] getSqlColumnNames() {
        return sqlColumnNames;
    }

    @Override
    public String getRestriction() {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected boolean isFullGUAccessAllowed() {
        return (user == null) || user.getUserGroup().getName().equalsIgnoreCase("administratoren")
                    || user.getUserGroup().getName().equalsIgnoreCase("gaeste_read_gu");
    }
}
