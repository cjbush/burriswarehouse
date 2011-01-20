/**
 * $ID:$
 * $COPYRIGHT:$
 */
package com.jmex.bui.property;

import com.jmex.bui.provider.ResourceProvider;

/**
 * Removed from BStyleSheet and made into its own class
 *
 * @author torr
 * @since Oct 9, 2008 - 11:28:26 AM
 */
public class FontProperty extends Property {
    public String family;
    public String style;
    public int size;

    // from Property
    public Object resolve(ResourceProvider rsrcprov) {
//             System.out.println("Resolving text factory [family=" + family +
//                                ", style=" + style + ", size=" + size + "].");
        return rsrcprov.createTextFactory(family, style, size);
    }
}