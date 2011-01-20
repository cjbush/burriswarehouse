/**
 * $ID:$
 * $COPYRIGHT:$
 */
package com.jmex.bui.provider;

import com.jmex.bui.BCursor;
import com.jmex.bui.BImage;
import com.jmex.bui.text.BTextFactory;

import java.io.IOException;

/**
 * Removed from BStyleSheet and made into its own class
 * <p/>
 * An interface used by the stylesheet to obtain font and image resources.
 *
 * @author torr
 * @since Oct 9, 2008 - 11:25:12 AM
 */
public interface ResourceProvider {
    /**
     * Creates a factory that will render text using the specified font.
     *
     * @param family String
     * @param style  String
     * @param size   int
     * @return BTextFactory btf
     */
    public BTextFactory createTextFactory(String family,
                                          String style,
                                          int size);

    /**
     * Loads the image with the specified path.
     *
     * @param path String
     * @return BImage
     * @throws IOException io
     */
    public BImage loadImage(String path) throws IOException;

    /**
     * Loads the cursor with the specified name.
     *
     * @param name String
     * @return BCursor
     * @throws IOException io
     */
    public BCursor loadCursor(String name) throws IOException;
}