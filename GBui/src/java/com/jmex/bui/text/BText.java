//
// $Id: BText.java,v 1.2 2007/04/27 19:46:34 vivaldi Exp $
//
// BUI - a user interface library for the JME 3D engine
// Copyright (C) 2005-2006, Michael Bayne, All Rights Reserved
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

package com.jmex.bui.text;

import com.jme.renderer.Renderer;
import com.jmex.bui.util.Dimension;

/**
 * Contains a "run" of text.  Specializations of this class render text in different ways, for example using JME's
 * internal bitmapped font support or by using the AWT to render the run of text to an image and texturing a quad with
 * that entire image.
 */
public abstract class BText {
    /**
     * Returns the length in characters of this text.
     *
     * @return int length of text
     */
    public abstract int getLength();

    /**
     * Returns the screen dimensions of this text.
     *
     * @return Dimension size of text
     */
    public abstract Dimension getSize();

    /**
     * Returns the character position to which the cursor should be moved given that the user clicked the specified
     * coordinate (relative to the text's bounds).
     *
     * @param x int
     * @param y int
     * @return int hit position
     */
    public abstract int getHitPos(int x,
                                  int y);

    /**
     * Returns the x position for the cursor at the specified character index. Note that the position should be "before"
     * that character.
     */
    public abstract int getCursorPos(int index);

    /**
     * Renders this text to the display.
     */
    public abstract void render(Renderer render,
                                int x,
                                int y,
                                float alpha);

    /**
     * Optional rendering this text scaled to a certain height/width.
     */
    public void render(Renderer render,
                       int x,
                       int y,
                       int w,
                       int h,
                       float alpha) {
        render(render, x, y, alpha);
    }

    /**
     * Called when the component that contains this text is was added to the interface hierarchy.
     */
    public abstract void wasAdded();

    /**
     * Called when the component that contains this text is no longer part of a user interface hierarchy.
     */
    public abstract void wasRemoved();
}
