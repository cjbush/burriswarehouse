//
// $Id: ImageIcon.java,v 1.2 2007/04/27 19:46:32 vivaldi Exp $
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
import com.jmex.bui.BImage;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Displays an image as an icon.
 */
public class ImageIcon extends BIcon {
    /**
     * Creates an icon from the supplied source image.
     */
    public ImageIcon(BImage image) {
        _image = image;
    }

    /**
     * Converts the supplied AWT icon into a BUI icon.
     */
    public ImageIcon(Icon icon) {
        BufferedImage cached = new BufferedImage(
                icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D gfx = cached.createGraphics();
        try {
            icon.paintIcon(null, gfx, 0, 0);
            _image = new BImage(cached);
        } finally {
            gfx.dispose();
        }
    }

    // documentation inherited
    public int getWidth() {
        return _image.getImageWidth();
    }

    // documentation inherited
    public int getHeight() {
        return _image.getImageHeight();
    }

    // documentation inherited
    public void wasAdded() {
        super.wasAdded();
        _image.reference();
    }

    // documentation inherited
    public void wasRemoved() {
        super.wasRemoved();
        _image.release();
    }

    // documentation inherited
    public void render(Renderer renderer,
                       int x,
                       int y,
                       float alpha) {
        super.render(renderer, x, y, alpha);
        _image.render(renderer, x, y, alpha);
    }

    protected BImage _image;
}
