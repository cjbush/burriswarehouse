//
// $Id: BGlComponent.java,v 1.5 2007/05/08 22:13:48 vivaldi Exp $
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

package com.jmex.bui;

import com.jme.renderer.Renderer;
import org.lwjgl.opengl.GL11;

/**
 * This class sets up a BComponent to be a GL component. You extend this class then create the renderGl method insert
 * your OpenGL (LWJGL) code into this method and add the class to your window bake at 350 for 10 minutes and run... your
 * OpenGL code should appear as expected
 *
 * @author timo aka vivaldi
 * @since 27Apr07
 */
public class BGlComponent extends BComponent {
    @Override
    public void renderComponent(final Renderer renderer) {
        super.renderComponent(renderer);

        GL11.glPushMatrix();

        BComponent.applyDefaultStates();

        GL11.glScalef(_width, _height, 0);

        renderGl();

        GL11.glPopMatrix();
    }

    public void renderGl() {}
}
