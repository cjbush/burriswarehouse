//
// $Id: Dimension.java,v 1.2 2007/04/27 19:46:35 vivaldi Exp $
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
 * Represents the size of a component.
 */
public class Dimension {
    /**
     * The width of the entity in question.
     */
    public int width;

    /**
     * The height of the entity in question.
     */
    public int height;

    public Dimension(int width,
                     int height) {
        this.width = width;
        this.height = height;
    }

    public Dimension(Dimension other) {
        width = other.width;
        height = other.height;
    }

    public Dimension() {
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int _width) {
        width = _width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int _height) {
        height = _height;
    }

    @Override
    public String toString() {
        return width + "x" + height;
    }
}
