/**
 * $ID:$
 * $COPYRIGHT:$
 */
package com.jmex.bui.property;

import com.jmex.bui.provider.ResourceProvider;

import java.io.IOException;

/**
 * Removed from BStyleSheet and made into its own class
 *
 * @author torr
 * @since Oct 9, 2008 - 11:29:17 AM
 */
public class CursorProperty extends Property {
    public String name;

    // from Property
    public Object resolve(ResourceProvider rsrcprov) {
        try {
            return rsrcprov.loadCursor(name);
        } catch (IOException ioe) {
            System.err.println("Failed to load cursor '" + name + "': " + ioe);
            return null;
        }
    }
}