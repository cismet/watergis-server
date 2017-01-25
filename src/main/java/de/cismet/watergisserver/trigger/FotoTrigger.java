/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.watergisserver.trigger;

import Sirius.server.newuser.User;

import org.openide.util.lookup.ServiceProvider;

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
public class FotoTrigger extends AbstractDBAwareCidsTrigger {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(FotoTrigger.class);
    private static final String FG_BAK_CLASS_NAME = "de.cismet.cids.dynamics.dlm25w.foto";
    private static final String FG_BAK_TABLE_NAME = "dlm25w.foto";

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
            try {
                // If the cidsBean is a new object, the meta object contains the new id while the cidsBean has still
                // the id -1
                final Object id = cidsBean.getMetaObject().getID();
                final Object fotoNr = cidsBean.getProperty("foto_nr");
                final Statement s = getDbServer().getActiveDBConnection().getConnection().createStatement();
                // refresh the stations on fg_bak
                if (fotoNr instanceof Integer) {
                    s.execute("select dlm25w.import_fg_ba_foto_pr_pfById(" + fotoNr.toString() + ", '" + user.getName()
                                + "')");
                }
            } catch (Exception e) {
                log.error("Error while executing foto trigger.", e);
            }
        }
    }
}
