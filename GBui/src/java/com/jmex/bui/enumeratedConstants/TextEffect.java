/*
 * Copyright (C) 2001-2005 Pleasant nightmare studio
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.jmex.bui.enumeratedConstants;

/**
 * @author deus
 * @version 1.0
 * @since Feb 18, 2009 12:42:33 PM
 */
public enum TextEffect {
    NORMAL(0, "none"),
    OUTLINE(1, "outline"),
    SHADOW(2, "shadow"),
    PLAIN(3, "plain"),
    GLOW(4, "glow");

    public int stylesheetId;
    public String stylesheetAttribute;

    TextEffect(int stylesheetId, String stylesheetAttribute) {
        this.stylesheetId = stylesheetId;
        this.stylesheetAttribute = stylesheetAttribute;
    }

    public static TextEffect fromInt(int value) {
        for (TextEffect textEffect : values())
            if (textEffect.stylesheetId == value)
                return textEffect;
        throw new IllegalArgumentException("TextEffect non-existent for value: " + value);
    }

    public static TextEffect fromStylesheetAttributeString(String value) {
        for (TextEffect textEffect : values())
            if (textEffect.stylesheetAttribute.equals(value))
                return textEffect;
        throw new IllegalArgumentException("TextEffect non-existent for value: " + value);
    }
}
