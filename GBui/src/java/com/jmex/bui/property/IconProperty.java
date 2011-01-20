/**
 * $ID:$
 * $COPYRIGHT:$
 */
package com.jmex.bui.property;

import com.jmex.bui.BImage;
import com.jmex.bui.icon.BlankIcon;
import com.jmex.bui.icon.ImageIcon;
import com.jmex.bui.provider.ResourceProvider;

import java.io.IOException;

/**
 * Removed from BStyleSheet and made into its own class
 *
 * @author torr
 * @since Oct 9, 2008 - 11:29:01 AM
 */
public class IconProperty extends Property {
    public String type;
    public String ipath;
    public int width, height;

    // from Property
    public Object resolve(ResourceProvider rsrcprov) {
        if (type.equals("image")) {
            BImage image;
            try {
                image = rsrcprov.loadImage(ipath);
            } catch (IOException ioe) {
                System.err.println("Failed to load icon image '" + ipath + "': " + ioe);
                return new BlankIcon(10, 10);
            }
            return new ImageIcon(image);
        } else if (type.equals("blank")) {
            return new BlankIcon(width, height);
        } else {
            return new BlankIcon(10, 10);
        }
    }
}