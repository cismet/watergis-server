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
public class HnTrigger extends AbstractDBAwareCidsTrigger {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(HnTrigger.class);
    private static final String HN_CLASS_NAME = "de.cismet.cids.dynamics.duv.hn1";
    private static final String HN_TABLE_NAME = "duv.hn";
    private static Geometry beforeInsert;

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
        if (isHnObject(cidsBean)) {
            final int id = cidsBean.getMetaObject().getID();
            Connection con = null;

            try {
                if (id > 0) {
                    con = getDbServer().getConnectionPool().getConnection(true);
                    final Statement st = con.createStatement();
                    final ResultSet rs = st.executeQuery(
                            "select st_asBinary(geom) from duv.fb where id = "
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
                LOG.error("Error while executing fb beforeUpdate trigger." + String.valueOf(id), e);
            } finally {
                if (con != null) {
                    getDbServer().getConnectionPool().releaseDbConnection(con);
                }
            }
        }
    }

    @Override
    public CidsTriggerKey getTriggerKey() {
        return new CidsTriggerKey(CidsTriggerKey.ALL, HN_TABLE_NAME);
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
    private boolean isHnObject(final CidsBean cidsBean) {
        return (cidsBean.getClass().getName().equals(HN_CLASS_NAME));
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
        if (isHnObject(cidsBean)) {
            final Connection con = null;
            try {
                final long start = System.currentTimeMillis();
                // If the cidsBean is a new object, the meta object contains the new id while the cidsBean has still
                // the id -1
                final Object id = cidsBean.getProperty("id");
                final Geometry g = (Geometry)cidsBean.getProperty("geom");

                if (g != null) {
                    final DbUpdater updater = new DbUpdater(getDbServer().getConnectionPool());
                    updater.addUpdate("select duv.recreate_hn('" + g + "', '" + beforeInsert + "')");
                    updater.execute();
                    LOG.error("time to update fb " + (System.currentTimeMillis() - start));
                }
            } catch (Exception e) {
                LOG.error("Error while executing lfk trigger.", e);
            } finally {
                if (con != null) {
                    getDbServer().getConnectionPool().releaseDbConnection(con);
                }
            }
        }
    }
}
