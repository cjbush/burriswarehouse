//
// $Id: TintedBackground.java,v 1.2 2007/04/27 19:46:30 vivaldi Exp $
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

package com.jmex.bui.background;

import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jmex.bui.BComponent;
import com.jmex.bui.BImage;
import org.lwjgl.opengl.GL11;

/**
 * Displays a partially transparent solid color in the background.
 */
public class TintedBackground extends BBackground {
    /**
     * Creates a tinted background with the specified color.
     */
    public TintedBackground(ColorRGBA color) {
        _color = color;
    }

    // documentation inherited
    public void render(Renderer renderer,
                       int x,
                       int y,
                       int width,
                       int height,
                       float alpha) {
        super.render(renderer, x, y, width, height, alpha);

        BComponent.applyDefaultStates();
        BImage.blendState.apply();

        GL11.glColor4f(_color.r, _color.g, _color.b, _color.a * alpha);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x + width, y);
        GL11.glVertex2f(x + width, y + height);
        GL11.glVertex2f(x, y + height);
        GL11.glEnd();
    }

    protected ColorRGBA _color;
}
