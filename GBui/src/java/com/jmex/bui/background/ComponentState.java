/**
 * $ID$
 * $COPYRIGHT$
 */
package com.jmex.bui.background;

/**
 * @author timo
 * @since Mar 18, 2008 4:37:31 PM
 */
public enum ComponentState {
    /**
     * The default component state. This is used to select the component's style pseudoclass among other things.
     */
    DEFAULT(""),

    /**
     * A component state indicating that the mouse is hovering over the component. This is used to select the
     * component's style pseudoclass among other things.
     */
    HOVER("hover"),

    /**
     * A component state indicating that the component is disabled. This is used to select the component's style
     * pseudoclass among other things.
     */
    DISABLED("disabled"),

    /**
     * Indicates that this button is in the down state.
     */
    DOWN("down");

    String statePClasses;

    ComponentState(String str) {
        statePClasses = str;
    }
}
