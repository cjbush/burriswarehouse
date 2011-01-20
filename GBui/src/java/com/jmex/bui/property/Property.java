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
 * @since Oct 9, 2008 - 11:28:05 AM
 */
public abstract class Property {
    public abstract Object resolve(ResourceProvider rsrcprov);
}