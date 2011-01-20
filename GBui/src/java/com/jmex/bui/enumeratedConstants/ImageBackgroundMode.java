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
 * @since Feb 18, 2009 1:34:30 PM
 */
public enum ImageBackgroundMode {
    CENTER_XY(0, "centerxy"),
    CENTER_X(1, "centerx"),
    CENTER_Y(2, "centery"),
    SCALE_XY(3, "scalexy"),
    SCALE_X(4, "scalex"),
    SCALE_Y(5, "scaley"),
    TILE_XY(6, "tilexy"),
    TILE_X(7, "tilex"),
    TILE_Y(8, "tiley"),
    FRAME_XY(9, "framexy"),
    FRAME_X(10, "framex"),
    FRAME_Y(11, "framey");

    public int stylesheetId;
    public String stylesheetAttribute;

    ImageBackgroundMode(int stylesheetId, String stylesheetAttribute) {
        this.stylesheetId = stylesheetId;
        this.stylesheetAttribute = stylesheetAttribute;
    }

    public static ImageBackgroundMode fromInt(int value) {
        for (ImageBackgroundMode mode : values())
            if (mode.stylesheetId == value)
                return mode;
        throw new IllegalArgumentException("ImageBackgroundMode non-existent for value: " + value);
    }

    public static ImageBackgroundMode fromStylesheetAttributeString(String value) {
        for (ImageBackgroundMode mode : values())
            if (mode.stylesheetAttribute.equals(value))
                return mode;
        throw new IllegalArgumentException("ImageBackgroundMode non-existent for value: " + value);
    }
}
