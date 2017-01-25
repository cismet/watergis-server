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
package de.cismet.cids.custom.helper;

import com.vividsolutions.jts.geom.Geometry;

import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class CompressedGeometry implements Serializable {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(CompressedGeometry.class);

    //~ Instance fields --------------------------------------------------------

    private byte[] compressedGeometry;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CompressedGeometry object.
     *
     * @param  geom  DOCUMENT ME!
     */
    public CompressedGeometry(final Geometry geom) {
        try {
            final ByteArrayOutputStream iout = new ByteArrayOutputStream();
            final ByteArrayOutputStream bout = new ByteArrayOutputStream();
            final ObjectOutputStream oout = new ObjectOutputStream(iout);
            oout.writeObject(geom);
            oout.flush();
            final GZIPOutputStream zstream = new GZIPOutputStream(bout);
            zstream.write(iout.toByteArray());
            zstream.finish();

//            final FileOutputStream foutUncompressed = new FileOutputStream("/home/therter/tmp/uncomp.out");
//            foutUncompressed.write(iout.toByteArray());
//            foutUncompressed.close();
//
//            final FileOutputStream foutCompressed = new FileOutputStream("/home/therter/tmp/comp.out");
//            foutCompressed.write(bout.toByteArray());
//            foutCompressed.close();
            compressedGeometry = bout.toByteArray();
        } catch (Exception e) {
            LOG.error("error while compressing.-", e);
        }
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public Geometry getGeometry() throws Exception {
        final GZIPInputStream gzipIn = new GZIPInputStream(new ByteArrayInputStream(compressedGeometry));
        final ObjectInputStream uncompressedIn = new ObjectInputStream(gzipIn);
        return (Geometry)uncompressedIn.readObject();
    }
}
