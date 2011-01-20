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
public enum Policy {
    /**
     * Do not adjust the widgets on this axis.
     */
    NONE(0),

    /**
     * Stretch all the widgets to their maximum possible size on this axis.
     */
    STRETCH(1),

    /**
     * Stretch all the widgets to be equal to the size of the largest widget on this axis.
     */
    EQUALIZE(2),

    /**
     * Only valid for off-axis policy, this leaves widgets alone unless they are larger in the off-axis direction than
     * their container, in which case it constrains them to fit on the off-axis.
     */
    CONSTRAIN(3);

    int code;

    Policy(int code) {
        this.code = code;
    }
}