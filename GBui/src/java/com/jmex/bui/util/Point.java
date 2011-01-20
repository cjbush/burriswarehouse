//
// $Id: Point.java,v 1.2 2007/04/27 19:46:35 vivaldi Exp $
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
 * Represents the location of a component.
 */
public class Point {
    /**
     * The x position of the entity in question.
     */
    public int x;

    /**
     * The y position of the entity in question.
     */
    public int y;

    public Point(int x,
                 int y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point other) {
        x = other.x;
        y = other.y;
    }

    public Point() {
    }

    @Override
    public String toString() {
        return (x >= 0 ? "+" : "") + x + (y >= 0 ? "+" : "") + y;
    }
}
