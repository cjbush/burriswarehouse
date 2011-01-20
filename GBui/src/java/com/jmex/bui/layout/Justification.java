/**
 * $ID:$
 * $COPYRIGHT:$
 */
package com.jmex.bui.layout;

/**
 * A class used to make our policy constants type-safe.
 *
 * @author torr
 * @since Mar 24, 2009 - 9:32:57 AM
 */
public enum Justification {
    CENTER(0),
    LEFT(1),
    RIGHT(2),
    TOP(3),
    BOTTOM(4);
    int code;

    Justification(int code) {
        this.code = code;
    }
}
