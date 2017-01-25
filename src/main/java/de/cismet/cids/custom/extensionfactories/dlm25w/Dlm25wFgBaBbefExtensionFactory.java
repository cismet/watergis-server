/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.custom.extensionfactories.dlm25w;

import org.apache.log4j.Logger;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.cids.objectextension.ObjectExtensionFactory;

/**
 * Set the ww_gr attribut.
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class Dlm25wFgBaBbefExtensionFactory extends ObjectExtensionFactory {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(Dlm25wFgBaBbefExtensionFactory.class);

    //~ Methods ----------------------------------------------------------------

    @Override
    public void extend(final CidsBean bean) {
        try {
            final CidsBean wwGrBean = (CidsBean)bean.getProperty("ba_st.von.route.ww_gr");

            bean.setProperty("ww_gr", wwGrBean);
        } catch (final Exception e) {
            LOG.error("Error while determining the permissions of an object of the type fg_ba", e);
        }
    }
}
