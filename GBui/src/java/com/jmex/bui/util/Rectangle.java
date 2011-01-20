//
// $Id: Rectangle.java,v 1.2 2007/04/27 19:46:35 vivaldi Exp $
//
// BUI - a user interface library for the JME 3D engine
// Copyright (C) 2005, Michael Bayne, All Rights Reserved
//
// This library is free software; you can redistribute it and/or modify it
// under the terms of the GNU Lesser General Public License as published
// by the Free Software Foundation; either version 2.1 of the License, or
// (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

package com.jmex.bui.util;

/**
 * Represents the bounds of a component.
 */
public class Rectangle {
    /**
     * The x position of the entity in question.
     */
    public int x;

    /**
     * The y position of the entity in question.
     */
    public int y;

    /**
     * The width of the entity in question.
     */
    public int width;

    /**
     * The height of the entity in question.
     */
    public int height;

    public Rectangle(int x,
                     int y,
                     int width,
                     int height) {
        set(x, y, width, height);
    }

    public Rectangle(Rectangle other) {
        set(other.x, other.y, other.width, other.height);
    }

    public Rectangle() {
    }

    /**
     * Sets the fields of this rectangle.
     */
    public void set(int x,
                    int y,
                    int width,
                    int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Adds the specified rectangle to this rectangle, causing this rectangle to become the union of itself and the
     * specified rectangle.
     */
    public void add(int x,
                    int y,
                    int width,
                    int height) {
        int fx = Math.max(this.x + this.width, x + width);
        int fy = Math.max(this.y + this.height, y + height);
        this.x = Math.min(x, this.x);
        this.y = Math.min(y, this.y);
        this.width = fx - this.x;
        this.height = fy - this.y;
    }

    @Override
    // documentation inherited
    public boolean equals(Object other) {
        if (other instanceof Rectangle) {
            Rectangle orect = (Rectangle) other;
            return x == orect.x && y == orect.y &&
                   width == orect.width && height == orect.height;
        }
        return false;
    }

    @Override
    // documentation inherited
    public int hashCode() {
        return x ^ y ^ width ^ height;
    }

    @Override
    /**
     * Generates a string representation of this instance.
     */
    public String toString() {
        return width + "x" + height + (x >= 0 ? "+" : "") + x +
               (y >= 0 ? "+" : "") + y;
    }
}
