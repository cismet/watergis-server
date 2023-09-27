/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.watergisserver.trigger;

import Sirius.server.newuser.User;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.WKBReader;

import org.openide.util.lookup.ServiceProvider;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.cids.trigger.AbstractDBAwareCidsTrigger;
import de.cismet.cids.trigger.CidsTrigger;
import de.cismet.cids.trigger.CidsTriggerKey;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
@ServiceProvider(service = CidsTrigger.class)
public class FgBakTrigger extends AbstractDBAwareCidsTrigger {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(
            FgBakTrigger.class);
    private static final String FG_BAK_CLASS_NAME = "de.cismet.cids.dynamics.dlm25w.fg_bak";
    private static final String FG_BAK_TABLE_NAME = "dlm25w.fg_bak";
    private static Geometry beforeInsert;

    private static final ThreadPoolExecutor SINGLE_THREAD_EXECUTOR = new ThreadPoolExecutor(
            1,
            1,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());

    //~ Methods ----------------------------------------------------------------

    @Override
    public void afterDelete(final CidsBean cidsBean, final User user) {
    }

    @Override
    public void afterInsert(final CidsBean cidsBean, final User user) {
    }

    @Override
    public void afterUpdate(final CidsBean cidsBean, final User user) {
    }

    @Override
    public void beforeDelete(final CidsBean cidsBean, final User user) {
    }

    @Override
    public void beforeInsert(final CidsBean cidsBean, final User user) {
    }

    @Override
    public void beforeUpdate(final CidsBean cidsBean, final User user) {
        if (isFgBakObject(cidsBean)) {
            final int id = cidsBean.getMetaObject().getID();
            Connection con = null;

            try {
                if (id > 0) {
                    con = getDbServer().getConnectionPool().getConnection(true);
                    final Statement st = con.createStatement();
                    final ResultSet rs = st.executeQuery(
                            "select st_asBinary(geo_field) from dlm25w.fg_bak b join geom on (b.geom = geom.id) where b.id = "
                                    + id);

                    if (rs.next()) {
                        final GeometryFactory geomFactory = new GeometryFactory(new PrecisionModel(
                                    PrecisionModel.FLOATING),
                                -1);
                        final WKBReader wkbReader = new WKBReader(geomFactory);
                        final byte[] geometryAsByteArray = (byte[])rs.getObject(1);

                        if (geometryAsByteArray != null) {
                            beforeInsert = wkbReader.read(geometryAsByteArray);
                        }
                    }
                }
            } catch (Exception e) {
                LOG.error("Error while executing fgBak beforeUpdate trigger." + String.valueOf(id), e);
            } finally {
                if (con != null) {
                    getDbServer().getConnectionPool().releaseDbConnection(con);
                }
            }
        }
    }

    @Override
    public CidsTriggerKey getTriggerKey() {
        return new CidsTriggerKey(CidsTriggerKey.ALL, FG_BAK_TABLE_NAME);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   o  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int compareTo(final CidsTrigger o) {
        return 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   cidsBean  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private boolean isFgBakObject(final CidsBean cidsBean) {
        return (cidsBean.getClass().getName().equals(FG_BAK_CLASS_NAME));
    }

    @Override
    public void afterCommittedInsert(final CidsBean cidsBean, final User user) {
        restat(cidsBean, user);
    }

    @Override
    public void afterCommittedUpdate(final CidsBean cidsBean, final User user) {
        restat(cidsBean, user);
    }

    @Override
    public void afterCommittedDelete(final CidsBean cidsBean, final User user) {
        restat(cidsBean, user);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  cidsBean  DOCUMENT ME!
     * @param  user      DOCUMENT ME!
     */
    private void restat(final CidsBean cidsBean, final User user) {
        if (isFgBakObject(cidsBean)) {
            Object id = null;
            Connection con = null;
            try {
                final long start = System.currentTimeMillis();
                boolean fgBakJustMoved = false;

                if (beforeInsert != null) {
                    final Geometry g = (Geometry)cidsBean.getProperty("geom.geo_field");

                    if (g != null) {
                        con = getDbServer().getConnectionPool().getConnection(true);
                        final Statement s = con.createStatement();
                        final ResultSet set = s.executeQuery("select dlm25w.isGeometryTranslated('" + beforeInsert
                                        + "', '" + g
                                        + "')");

                        if (set.next()) {
                            fgBakJustMoved = set.getBoolean(1);
                        }
                    }
                }
                // If the cidsBean is a new object, the meta object contains the new id while the cidsBean has still
                // the id -1
                id = cidsBean.getMetaObject().getID();
//                final Statement s = getDbServer().getConnectionPool().getConnection(true).createStatement();
                final DbUpdater updater = new DbUpdater(getDbServer().getConnectionPool());
                // refresh the stations on fg_bak
                if (fgBakJustMoved) {
                    updater.addUpdate("select dlm25w.replace_fg_bakMoveOnly(" + id.toString() + ")");
                } else {
                    updater.addUpdate("select dlm25w.replace_fg_bak(" + id.toString() + ")");
                }
                updater.execute();
                // refresh fg_ba
                updater.addUpdate("select dlm25w.import_fg_ba(" + id.toString() + ", '" + user.getName() + "')");
                updater.execute();
                // the gmd and gb themes should be recreated before the fg_ba station are refreshed. Otherwise, the gmd,
                // gb object could be moved to an other route and this leads to overlapping objects
                updater.addUpdate("select dlm25w.import_fg_ba_gmdByFgBak(" + id.toString() + ", '" + user.getName()
                            + "')");
                updater.addUpdate("select dlm25w.import_fg_ba_gbByFgBak(" + id.toString() + ", '" + user.getName()
                            + "')");
                updater.execute();

                // refresh the stations on fg_ba
                if (fgBakJustMoved) {
                    updater.addUpdate("select dlm25w.replace_fg_ba_by_fg_bakMoveOnly(" + id.toString() + ")");
                } else {
                    updater.addUpdate("select dlm25w.replace_fg_ba_by_fg_bak(" + id.toString() + ")");
                }
                updater.execute();
                // refresh fg_lak
                updater.addUpdate("select dlm25w.import_fg_lak_by_fg_bak(" + id.toString() + ", '" + user.getName()
                            + "')");
                updater.execute();

                // refresh the stations on fg_lak
                if (fgBakJustMoved) {
                    updater.addUpdate("select dlm25w.replace_fg_lak_by_fg_bak(" + id.toString() + ", 100000000)");
                } else {
                    updater.addUpdate("select dlm25w.replace_fg_lak_by_fg_bak(" + id.toString() + ")");
                }
                updater.execute();
                // refresh fg_la
                updater.addUpdate("select dlm25w.import_fg_la_by_fg_bak(" + id.toString() + ", '" + user.getName()
                            + "')");
                updater.execute();

                // refresh the stations on fg_la
                if (fgBakJustMoved) {
                    updater.addUpdate("select dlm25w.replace_fg_la_by_fg_bak(" + id.toString() + ", 100000000)");
                } else {
                    updater.addUpdate("select dlm25w.replace_fg_la_by_fg_bak(" + id.toString() + ")");
                }
                // refresh stat layer
                updater.addUpdate("select dlm25w.add_fg_ba_stat(" + id.toString() + ")");
                updater.addUpdate("select dlm25w.add_fg_la_stat(" + id.toString() + ")");
                // refresh fotoPrPf
                updater.addUpdate("select dlm25w.import_fg_ba_foto_pr_pfbyBakId(" + id.toString() + ")");
                // refresh presentation layer
                updater.addUpdate("select dlm25w.import_fg_bak_pr_pf(" + id.toString() + ", '" + user.getName() + "')");
                updater.addUpdate("select dlm25w.import_fg_ba_pr_pfByBakId(" + id.toString() + ", '" + user.getName()
                            + "')");
                updater.addUpdate("select dlm25w.import_fg_lak_pr_pf_ByFgBak(" + id.toString() + ", '" + user.getName()
                            + "')");
                updater.addUpdate("select dlm25w.import_fg_la_pr_pfByFgBak(" + id.toString() + ", '" + user.getName()
                            + "')");
                updater.execute();

                updater.addUpdate("select dlm25w.import_fg_ba_geroByBak(" + id.toString() + ")");
                updater.execute();
                updater.addUpdate("select dlm25w.import_fg_ba_gerogByBak(" + id.toString() + ")");
                updater.execute();
                updater.addUpdate("select dlm25w.import_fg_ba_gerogaByBak(" + id.toString() + ")");
                updater.execute();

                if (beforeInsert != null) {
                    final Geometry g = (Geometry)cidsBean.getProperty("geom.geo_field");

                    if ((g != null) && beforeInsert.equalsExact(g)) {
                        beforeInsert = null;
                    } else if (g != null) {
                        final Geometry oldWithoutNew = beforeInsert.buffer(0.1).difference(g.buffer(0.1));

                        if (oldWithoutNew.isEmpty()) {
                            beforeInsert = null;
                        } else {
                            beforeInsert = oldWithoutNew;
                        }
                    }
                }
                if (beforeInsert != null) {
                    updater.addUpdate("select dlm25w.import_fg_ba_geroga_tile(st_buffer('" + beforeInsert
                                + "', 20), '" + user.getName() + "')");
                    updater.execute();
                }

                updater.addUpdate("select dlm25w.import_fg_ba_gerog_rsByBak(" + id.toString() + ")");

                if (beforeInsert != null) {
                    updater.addUpdate("select dlm25w.import_fg_ba_geroga_rs_tile(st_buffer('" + beforeInsert
                                + "', 40), '" + user.getName() + "', Array[1,3,5,10,15,20,25,30]::double precision[])");
                }

                updater.execute();

                beforeInsert = null;
                LOG.error("time to update stations " + (System.currentTimeMillis() - start));
            } catch (Exception e) {
                LOG.error("Error while executing fgBak trigger." + String.valueOf(id), e);
            } finally {
                if (con != null) {
                    getDbServer().getConnectionPool().releaseDbConnection(con);
                }
            }

            if (SINGLE_THREAD_EXECUTOR.getPoolSize() < 2) {
                final Thread t = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                Connection con = null;

                                try {
                                    con = getDbServer().getConnectionPool().getConnection(true);
                                    final Statement s = con.createStatement();
                                    s.execute(
                                        "select dlm25w.migrate_fg_bak_wk_to_fg_lak_2()");
                                    s.execute(
                                        "select dlm25w.import_fg_ba_geroga_rsByBak("
                                                + cidsBean.getMetaObject().getID()
                                                + ")");
                                    s.execute(
                                        "select dlm25w.add_fg_ba_stat_10("
                                                + String.valueOf(cidsBean.getMetaObject().getID())
                                                + ")");
                                    s.execute(
                                        "select dlm25w.add_fg_la_stat_10("
                                                + String.valueOf(cidsBean.getMetaObject().getID())
                                                + ")");
                                    s.execute(
                                        "select duv.recreate_fg_ba_duvByFg("
                                                + String.valueOf(cidsBean.getMetaObject().getID())
                                                + ")");
                                } catch (Exception e) {
                                    LOG.error(
                                        "Error while executing async fgBak trigger."
                                                + String.valueOf(cidsBean.getMetaObject().getID()),
                                        e);
                                } finally {
                                    if (con != null) {
                                        getDbServer().getConnectionPool().releaseDbConnection(con);
                                    }
                                }
                            }
                        });

                SINGLE_THREAD_EXECUTOR.execute(t);
            }
        }
    }
}
