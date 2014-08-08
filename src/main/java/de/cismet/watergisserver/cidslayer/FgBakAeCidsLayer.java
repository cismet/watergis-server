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

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collection;
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
public class FgBakAeCidsLayer implements CidsLayerInfo, Serializable {

    //~ Static fields/initializers ---------------------------------------------

    private static final String DOMAIN = "DLM25W";

    //~ Instance fields --------------------------------------------------------

    private final MetaClass mc;
    private String sqlGeoField;
    private String geoField;
    private String selectionString;
    private String[] columnNames;
    private String[] sqlColumnNames;
    private String[] columnPropertyNames;
    private String[] primitiveColumnTypes;
    private Map<String, Boolean> primitiveTypes = new HashMap<String, Boolean>();
    private Map<String, Integer> catalogueTypes = new HashMap<String, Integer>();
    private Map<String, StationInfo> stationTypes = new HashMap<String, StationInfo>();
    private Map<String, Integer> referencedClass = new HashMap<String, Integer>();

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new FgBakAeCidsLayer object.
     *
     * @param  mc  DOCUMENT ME!
     */
    public FgBakAeCidsLayer(final MetaClass mc) {
        this.mc = mc;
        init(mc);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  mc  DOCUMENT ME!
     */
    private void init(final MetaClass mc) {
        final HashMap attrMap = mc.getMemberAttributeInfos();
        final StringBuilder sb = new StringBuilder("Select ");
        final StringBuilder joins = new StringBuilder();
        boolean firstAttr = true;
        final HashMap allClasses = mc.getEmptyInstance().getAllClasses();
        final List<String> columnNamesList = new ArrayList<String>();
        final List<String> sqlColumnNamesList = new ArrayList<String>();
        final List<String> columnPropertyNamesList = new ArrayList<String>();
        final List<String> primitiveColumnTypesList = new ArrayList<String>();
        int i = 0;
        int lineId = 0;

        for (final Object key : attrMap.keySet()) {
            final MemberAttributeInfo attr = (MemberAttributeInfo)attrMap.get(key);
            if (!firstAttr) {
                sb.append(",");
            } else {
                firstAttr = false;
            }

            if (attr.isForeignKey()
                        && getBeanClassName(allClasses, attr.getForeignKeyClassId()).toLowerCase().endsWith("geom")) {
                sqlGeoField = "geo_field";
                geoField = attr.getName();
                sb.append("asText(geom.geo_field) as ").append(geoField);
                columnNamesList.add(attr.getName());
                sqlColumnNamesList.add("geom.geo_field");
                columnPropertyNamesList.add(attr.getName() + ".geo_field");
                joins.append(" join geom on (").append(attr.getFieldName()).append(" = geom.id)");
                primitiveColumnTypesList.add(attr.getJavaclassname());
                referencedClass.put(attr.getName(), attr.getForeignKeyClassId());
            } else if (attr.isForeignKey()
                        && getBeanClassName(allClasses, attr.getForeignKeyClassId()).toLowerCase().equals(
                            "fg_bak_linie")) {
                sqlGeoField = "geo_field";
                geoField = "geom";
                sb.append("asText(geom.geo_field) as ").append(geoField);
                columnNamesList.add("geom");
                sqlColumnNamesList.add("geom.geo_field");
                columnPropertyNamesList.add(null);
//                columnPropertyNamesList.add(attr.getName() + ".geom.geo_field");
                sb.append(", von.wert");
                columnNamesList.add("von");
                sqlColumnNamesList.add("von.wert");
                columnPropertyNamesList.add(attr.getName());
                sb.append(", bis.wert");
                columnNamesList.add("bis");
                sqlColumnNamesList.add("bis.wert");
                columnPropertyNamesList.add(attr.getName());
                sb.append(", fg_bak.ba_cd");
                columnNamesList.add("fg_bak.ba_cd");
                sqlColumnNamesList.add("bis.wert");
                columnPropertyNamesList.add("fg_bak");
                joins.append(" join dlm25w.fg_bak_linie on (")
                        .append(attr.getFieldName())
                        .append(" = dlm25w.fg_bak_linie.id)");
                joins.append(" join geom on (geom = geom.id)");
                joins.append(" join dlm25w.fg_bak_punkt von on (von.id = dlm25w.fg_bak_linie.von)");
                joins.append(" join dlm25w.fg_bak_punkt bis on (bis.id = dlm25w.fg_bak_linie.bis)");
                joins.append(" join dlm25w.fg_bak on (bis.route = dlm25w.fg_bak.id)");
                primitiveColumnTypesList.add("String");
                primitiveColumnTypesList.add(attr.getJavaclassname());
                primitiveColumnTypesList.add(attr.getJavaclassname());
                primitiveColumnTypesList.add("String");
                StationInfo s = new StationInfo(true, true, null, ++lineId);
                stationTypes.put("von", s);
                s = new StationInfo(true, false, null, lineId);
                stationTypes.put("bis", s);
            } else if (attr.isForeignKey()
                        && getBeanClassName(allClasses, attr.getForeignKeyClassId()).toLowerCase().equals(
                            "fg_ba_linie")) {
                sqlGeoField = "geo_field";
                geoField = "geom";
                sb.append("asText(geom.geo_field) as ").append(geoField);
                columnNamesList.add("geom");
                sqlColumnNamesList.add("geom");
                columnPropertyNamesList.add(null);
//                columnPropertyNamesList.add(attr.getName() + ".geom.geo_field");
                sb.append(", von.wert");
                columnNamesList.add("von");
                sqlColumnNamesList.add("von.wert");
                columnPropertyNamesList.add(attr.getName());
                sb.append(", bis.wert");
                columnNamesList.add("bis");
                sqlColumnNamesList.add("bis.wert");
                columnPropertyNamesList.add(attr.getName());
                joins.append(" join dlm25w.fg_ba_linie on (")
                        .append(attr.getFieldName())
                        .append(" = dlm25w.fg_ba_linie.id)");
                joins.append(" join geom on (geom = geom.id)");
                joins.append(" join dlm25w.fg_ba_punkt von on (von.id = dlm25w.fg_ba_linie.von)");
                joins.append(" join dlm25w.fg_ba_punkt bis on (bis.id = dlm25w.fg_ba_linie.bis)");
                primitiveColumnTypesList.add("String");
                primitiveColumnTypesList.add(attr.getJavaclassname());
                primitiveColumnTypesList.add(attr.getJavaclassname());
                StationInfo s = new StationInfo(true, true, null, ++lineId);
                stationTypes.put("von", s);
                s = new StationInfo(true, false, null, lineId);
                stationTypes.put("bis", s);
            } else if (attr.isForeignKey()
                        && getBeanClassName(allClasses, attr.getForeignKeyClassId()).toLowerCase().equals(
                            "fg_lak_linie")) {
                sqlGeoField = "geo_field";
                geoField = "geom";
                sb.append("asText(geom.geo_field) as ").append(geoField);
                columnNamesList.add("geom");
                sqlColumnNamesList.add("geom");
                columnPropertyNamesList.add(null);
//                columnPropertyNamesList.add(attr.getName() + ".geom.geo_field");
                sb.append(", von.wert");
                columnNamesList.add("von");
                sqlColumnNamesList.add("von.wert");
                columnPropertyNamesList.add(attr.getName());
                sb.append(", bis.wert");
                columnNamesList.add("bis");
                sqlColumnNamesList.add("bis.wert");
                columnPropertyNamesList.add(attr.getName());
                joins.append(" join dlm25w.fg_lak_linie on (")
                        .append(attr.getFieldName())
                        .append(" = dlm25w.fg_lak_linie.id)");
                joins.append(" join geom on (geom = geom.id)");
                joins.append(" join dlm25w.fg_lak_punkt von on (von.id = dlm25w.fg_lak_linie.von)");
                joins.append(" join dlm25w.fg_lak_punkt bis on (bis.id = dlm25w.fg_lak_linie.bis)");
                primitiveColumnTypesList.add("String");
                primitiveColumnTypesList.add(attr.getJavaclassname());
                primitiveColumnTypesList.add(attr.getJavaclassname());
                StationInfo s = new StationInfo(true, true, null, ++lineId);
                stationTypes.put("von", s);
                s = new StationInfo(true, false, null, lineId);
                stationTypes.put("bis", s);
            } else if (attr.isForeignKey()) {
                final MetaClass foreignClass = getBeanClass(allClasses, attr.getForeignKeyClassId());
                handleCatalogue(
                    attr,
                    foreignClass,
                    sb,
                    columnNamesList,
                    columnPropertyNamesList,
                    sqlColumnNamesList,
                    primitiveColumnTypesList,
                    joins);
            } else {
                sb.append(mc.getTableName()).append(".").append(attr.getFieldName());
                columnNamesList.add(attr.getName());
                sqlColumnNamesList.add(mc.getTableName() + "." + attr.getFieldName());
                columnPropertyNamesList.add(attr.getName());
                primitiveTypes.put(columnNamesList.get(columnNamesList.size() - 1), Boolean.TRUE);
                primitiveColumnTypesList.add(attr.getJavaclassname());
            }

            ++i;
        }

        sb.append(" from ").append(mc.getTableName());
        sb.append(joins.toString());
        selectionString = sb.toString();
        columnNames = columnNamesList.toArray(new String[columnNamesList.size()]);
        sqlColumnNames = sqlColumnNamesList.toArray(new String[sqlColumnNamesList.size()]);
        columnPropertyNames = columnPropertyNamesList.toArray(new String[columnPropertyNamesList.size()]);
        primitiveColumnTypes = primitiveColumnTypesList.toArray(new String[primitiveColumnTypesList.size()]);
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
            final StringBuilder sb,
            final List<String> columnNamesList,
            final List<String> columnPropertyNamesList,
            final List<String> sqlColumnNamesList,
            final List<String> primitiveColumnTypesList,
            final StringBuilder joins) {
        final ObjectAttribute nameAttr = (ObjectAttribute)foreignClass.getEmptyInstance().getAttribute("name");

        if (nameAttr != null) {
            final String alias = addLeftJoin(
                    joins,
                    foreignClass.getTableName(),
                    mc.getTableName()
                            + "."
                            + attr.getFieldName(),
                    "id");
            sb.append(alias).append(".").append(nameAttr.getMai().getFieldName());
            columnNamesList.add(attr.getName());
            sqlColumnNamesList.add(alias + "." + nameAttr.getMai().getFieldName());
            columnPropertyNamesList.add(attr.getName());
            catalogueTypes.put(columnNamesList.get(columnNamesList.size() - 1), attr.getForeignKeyClassId());
            primitiveColumnTypesList.add("java.lang.String");
        } else {
            sb.append(mc.getTableName()).append(".").append(attr.getFieldName());
            columnNamesList.add(attr.getName());
            sqlColumnNamesList.add(mc.getTableName() + "." + attr.getFieldName());
            columnPropertyNamesList.add(attr.getName());
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
}
