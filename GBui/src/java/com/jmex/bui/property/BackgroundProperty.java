/**
 * $ID:$
 * $COPYRIGHT:$
 */
package com.jmex.bui.property;

import com.jme.renderer.ColorRGBA;
import com.jmex.bui.BImage;
import com.jmex.bui.background.BlankBackground;
import com.jmex.bui.background.ImageBackground;
import com.jmex.bui.background.TintedBackground;
import com.jmex.bui.enumeratedConstants.ImageBackgroundMode;
import com.jmex.bui.provider.ResourceProvider;
import com.jmex.bui.util.Insets;

import java.io.IOException;

/**
 * Removed from BStyleSheet and made into its own class
 *
 * @author torr
 * @since Oct 9, 2008 - 11:28:44 AM
 */
public class BackgroundProperty extends Property {
    public String type;
    public ColorRGBA color;
    public String ipath;
    public ImageBackgroundMode scaleMode = ImageBackgroundMode.SCALE_XY;
    public Insets frame;

    // from Property
    public Object resolve(ResourceProvider rsrcprov) {
        if (type.equals("solid")) {
            return new TintedBackground(color);
        } else if (type.equals("image")) {
            BImage image;
            try {
                image = rsrcprov.loadImage(ipath);
            } catch (IOException ioe) {
                System.err.println("Failed to load background image '" + ipath + "': " + ioe);
                return new BlankBackground();
            }
            return new ImageBackground(scaleMode, image, frame);
        } else {
            return new BlankBackground();
        }
    }
}