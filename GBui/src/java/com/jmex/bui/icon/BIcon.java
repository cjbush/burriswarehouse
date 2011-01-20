//
// $Id: BIcon.java,v 1.2 2007/04/27 19:46:32 vivaldi Exp $
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

package com.jmex.bui.icon;

import com.jme.renderer.Renderer;

/**
 * Provides icon imagery for various components which make use of it.
 */
public abstract class BIcon {
    /**
     * Returns the width of this icon.
     *
     * @return int
     */
    public abstract int getWidth();

    /**
     * Returns the height of this icon.
     *
     * @return int
     */
    public abstract int getHeight();

    /**
     * Renders this icon.
     *
     * @param renderer Renderer
     * @param x        int
     * @param y        int
     * @param alpha    float
     */
    public void render(Renderer renderer,
                       int x,
                       int y,
                       float alpha) {
    }

    /**
     * Called when the component that contains this icon is was added to the interface hierarchy.
     */
    public void wasAdded() {
    }

    /**
     * Called when the component that contains this icon is no longer part of a user interface hierarchy.
     */
    public void wasRemoved() {
    }
}
