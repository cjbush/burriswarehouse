/**
 * $ID:$
 * $COPYRIGHT:$
 */
package com.jmex.bui;

import java.util.HashMap;

/**
 * Removed from BStyleSheet and made into its own class
 *
 * @author torr
 * @since Oct 9, 2008 - 11:27:50 AM
 */
public class Rule {
    public String styleClass;

    public String pseudoClass;

    public HashMap<String, Object> properties = new HashMap<String, Object>();

    public Object get(HashMap rules, String key) {
        Object value = properties.get(key);
        if (value != null) {
            return value;
        }
        Rule prule = (Rule) properties.get("parent");
        return (prule != null) ? prule.get(rules, key) : null;
    }

    @Override
    // from Object
    public String toString() {
        return "[class=" + styleClass + ", pclass=" + pseudoClass + "]";
    }
}