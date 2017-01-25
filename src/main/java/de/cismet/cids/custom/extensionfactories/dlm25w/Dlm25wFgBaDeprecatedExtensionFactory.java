/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.custom.extensionfactories.dlm25w;

import Sirius.server.middleware.impls.domainserver.DomainServerImpl;
import Sirius.server.middleware.types.MetaObject;

import org.apache.log4j.Logger;

import java.util.ArrayList;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.cids.objectextension.ObjectExtensionFactory;

/**
 * Wird nicht genutzt.
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class Dlm25wFgBaDeprecatedExtensionFactory extends ObjectExtensionFactory {

    //~ Static fields/initializers ---------------------------------------------

    private static final String ATTRIBUTE_NAME = "name";
    private static final Logger LOG = Logger.getLogger(Dlm25wFgBaDeprecatedExtensionFactory.class);
    private static final String QUERY = "SELECT wk_fg. wk_k "
                + "FROM dlm25w.fg_bak f join dlm25w.k_ww_gr w on (f.ww_gr = w.id) "
                + "WHERE f.ba_cd = %s";

    //~ Methods ----------------------------------------------------------------

    @Override
    public void extend(final CidsBean bean) {
        try {
            final DomainServerImpl server = getDomainServer();

            if (server != null) {
                final MetaObject[] mo = server.getMetaObject(
                        user,
                        String.format(QUERY, String.valueOf(bean.getProperty("ba_cd"))));

                if ((mo != null) && (mo.length > 0)) {
                    final CidsBean wwGrBean = mo[0].getBean();
                    final Integer wdm = (Integer)wwGrBean.getProperty("wdm");

                    bean.setProperty("ww_gr", wwGrBean);
                    bean.setProperty("wdm", wdm);
                }
            }
        } catch (final Exception e) {
            LOG.error("Error while determining the permissions of an object of the type fg_ba", e);
        }
    }
}
