//
// $Id: LineBorder.java,v 1.2 2007/04/27 19:46:31 vivaldi Exp $
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

package com.jmex.bui.border;

import com.jme.renderer.ColorRGBA;
import com.jme.renderer.RenderContext;
import com.jme.renderer.Renderer;
import com.jme.scene.state.lwjgl.records.LineRecord;
import com.jme.system.DisplaySystem;
import com.jmex.bui.BComponent;
import com.jmex.bui.BImage;
import com.jmex.bui.util.Insets;
import org.lwjgl.opengl.GL11;

/**
 * Defines a border that displays a single line around the bordered component in a specified color.
 */
public class LineBorder extends BBorder {
    public LineBorder(ColorRGBA color) {
        this(color, 1);
    }

    public LineBorder(ColorRGBA color,
                      int width) {
        _color = color;
        _left = width;
        _top = width;
        _right = width;
        _bottom = width;
        _width = width;
    }

    public Insets adjustInsets(Insets insets) {
        return new Insets(_left + insets.left, _top + insets.top,
                          _right + insets.right, _bottom + insets.bottom);
    }

    @Override
    // from BBorder
    public void render(Renderer renderer,
                       int x,
                       int y,
                       int width,
                       int height,
                       float alpha) {
        super.render(renderer, x, y, width, height, alpha);

        BComponent.applyDefaultStates();
        BImage.blendState.apply();

        if (_width > 0) {
            RenderContext ctx = DisplaySystem.getDisplaySystem().getCurrentContext();
            GL11.glColor4f(_color.r, _color.g, _color.b, _color.a * alpha);
            // First draw the bottom line.
            if (_bottom > 0) {
                ((LineRecord) ctx.getLineRecord()).applyLineWidth(_bottom);
                float offset = _bottom / 2f;
                GL11.glBegin(GL11.GL_LINE_STRIP);
                GL11.glVertex2f(x - offset, y);
                GL11.glVertex2f(x + width, y);
                GL11.glEnd();
            }
            // Next draw the right hand side.
            if (_right > 0) {
                ((LineRecord) ctx.getLineRecord()).applyLineWidth(_right);
                float offset = _right / 2f;
                GL11.glBegin(GL11.GL_LINE_STRIP);
                GL11.glVertex2f(x + width - offset, y);
                GL11.glVertex2f(x + width - offset, y + height);
                GL11.glEnd();
            }
            // Next draw the top line.
            if (_top > 0) {
                ((LineRecord) ctx.getLineRecord()).applyLineWidth(_top);
                float offset = _top / 2f;
                GL11.glBegin(GL11.GL_LINE_STRIP);
                GL11.glVertex2f(x + width, y + height - offset);
                GL11.glVertex2f(x - offset, y + height - offset);
                GL11.glEnd();
            }
            // Last draw the left hand side.
            if (_left > 0) {
                ((LineRecord) ctx.getLineRecord()).applyLineWidth(_left);
                GL11.glBegin(GL11.GL_LINE_STRIP);
                GL11.glVertex2f(x, y + height);
                GL11.glVertex2f(x, y);
                GL11.glEnd();
            }
        }
    }

    protected ColorRGBA _color;
    protected int _width, _left, _top, _right, _bottom;

    protected static final Insets ONE_PIXEL_INSETS = new Insets(1, 1, 1, 1);
}
