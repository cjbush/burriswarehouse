/**
 * $ID:$
 * $COPYRIGHT:$
 */
package com.jmex.bui.provider;

import com.jmex.bui.BCursor;
import com.jmex.bui.BImage;
import com.jmex.bui.BStyleSheet;
import com.jmex.bui.text.AWTTextFactory;
import com.jmex.bui.text.BTextFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.HashMap;


/**
 * Removed from BStyleSheet and made into its own class
 *
 * A default implementation of the stylesheet resource provider.
 *
 * @author torr
 * @since Oct 9, 2008 - 11:26:22 AM
 *
 */

/**
 *
 */
public class DefaultResourceProvider implements ResourceProvider {
    public BTextFactory createTextFactory(
            String family,
            String style,
            int size) {
        int nstyle = Font.PLAIN;
        if (style.equals(BStyleSheet.BOLD)) {
            nstyle = Font.BOLD;
        } else if (style.equals(BStyleSheet.ITALIC)) {
            nstyle = Font.ITALIC;
        } else if (style.equals(BStyleSheet.BOLD_ITALIC)) {
            nstyle = Font.ITALIC | Font.BOLD;
        }

        return new AWTTextFactory(new Font(family, nstyle, size), true);
    }

    public BImage loadImage(String path) throws IOException {
        // normalize the image path
    	String newPath = path;
        if (!newPath.startsWith("/")) {
            newPath = "/" + newPath;
        }

        // first check the cache
        WeakReference<BImage> iref = _cache.get(newPath);
        BImage image;
        if (iref != null && (image = iref.get()) != null) {
            return image;
        }

        // create and cache a new BUI image with the appropriate data
        URL url = getClass().getResource(newPath);
        if (url == null) {
        	File file = new File(path);
        	url = file.toURI().toURL();
        	if (url == null) {
        		throw new IOException("Can't locate image '" + newPath + "'.");
        	}
        }
        image = new BImage(url);
        _cache.put(newPath, new WeakReference<BImage>(image));

        return image;
    }

    public BCursor loadCursor(String path) throws IOException {
        // we'll just assume the name is an image path
        if (!path.startsWith("/")) {
            path = "/" + path;
        }

        // first check the cache
        WeakReference<BCursor> cref = _ccache.get(path);
        BCursor cursor;
        if (cref != null && (cursor = cref.get()) != null) {
            return cursor;
        }

        // create and cache a new cursor with the appropriate data
        URL url = getClass().getResource(path);
        if (url == null) {
            throw new IOException("Can't locate cursor image '" + path + "'.");
        }
        BufferedImage image = ImageIO.read(url);
        BufferedImage cimage = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = cimage.createGraphics();
        try {
            g.drawImage(image, null, 0, 0);
            cursor = new BCursor(cimage, 0, 0);
        } finally {
            g.dispose();
        }
        _ccache.put(path, new WeakReference<BCursor>(cursor));
        return cursor;
    }

    /**
     * A cache of  {@link BImage}  instances.
     */
    protected HashMap<String, WeakReference<BImage>> _cache =
            new HashMap<String, WeakReference<BImage>>();

    /**
     * A cache of  {@link BCursor}  instances.
     */
    protected HashMap<String, WeakReference<BCursor>> _ccache =
            new HashMap<String, WeakReference<BCursor>>();
}