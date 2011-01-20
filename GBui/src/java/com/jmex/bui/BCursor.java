//
// $Id: BCursor.java,v 1.2 2007/04/27 19:46:29 vivaldi Exp $
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

package com.jmex.bui;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.logging.Level;

/**
 * Contains a cursor.
 */
public class BCursor {
    /**
     * Create a cursor from a BufferedImage.
     */
    public static Cursor createCursor(BufferedImage image,
                                      int hx,
                                      int hy) {
        int ww = image.getWidth();
        int hh = image.getHeight();
        IntBuffer data = ByteBuffer.allocateDirect(ww * hh * 4).asIntBuffer();
        for (int yy = hh - 1; yy >= 0; yy--) {
            for (int xx = 0; xx < ww; xx++) {
                data.put(image.getRGB(xx, yy));
            }
        }
        data.flip();
        try {
            return new Cursor(ww, hh, hx, hh - hy - 1, 1, data, null);
        } catch (LWJGLException e) {
            System.err.println("Unable to create cursor: " + e);
            return null;
        }
    }

    public BCursor(Cursor cursor) {
        setCursor(cursor);
    }

    public BCursor(BufferedImage image,
                   int hx,
                   int hy) {
        setCursor(image, hx, hy);
    }

    /**
     * Set the cursor to the specified value.
     */
    public void setCursor(Cursor cursor) {
        _cursor = cursor;
        _image = null;
    }

    /**
     * Create a cursor from the image with the supplied hotspot and use it.
     */
    public void setCursor(BufferedImage image,
                          int hx,
                          int hy) {
        if (Mouse.isCreated()) {
            setCursor(createCursor(image, hx, hy));
        } else {
            _image = image;
            _hx = hx;
            _hy = hy;
        }
    }

    /**
     * Retrieve the cursor.
     */
    public Cursor getCursor() {
        return _cursor;
    }

    /**
     * Display this cursor.
     */
    public void show() {
        if (!Display.isCreated()) {
            return;
        }
        if (!Mouse.isCreated()) {
            try {
                Mouse.create();
            } catch (Throwable t) {
                Log.log.log(Level.WARNING, "Problem creating mouse", t);
                return;
            }
        }
        if (_image != null) {
            setCursor(_image, _hx, _hy);
        }
        if (Mouse.getNativeCursor() != _cursor) {
            try {
                Mouse.setNativeCursor(_cursor);
            } catch (Throwable t) {
                Log.log.log(Level.WARNING, "Problem updating mouse cursor: ", t);
            }
        }
    }

    protected Cursor _cursor;

    protected BufferedImage _image;
    protected int _hx, _hy;
}
