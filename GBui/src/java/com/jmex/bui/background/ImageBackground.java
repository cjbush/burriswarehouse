//
// $Id: ImageBackground.java,v 1.2 2007/04/27 19:46:30 vivaldi Exp $
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

package com.jmex.bui.background;

import com.jme.renderer.Renderer;
import com.jmex.bui.BImage;
import com.jmex.bui.enumeratedConstants.ImageBackgroundMode;
import static com.jmex.bui.enumeratedConstants.ImageBackgroundMode.*;
import com.jmex.bui.util.Insets;

/**
 * Supports image backgrounds in a variety of ways. Specifically:
 * <p/>
 * <ul> <li> Centering the image either horizontally, vertically or both. <li> Scaling the image either horizontally,
 * vertically or both. <li> Tiling the image either horizontally, vertically or both. <li> Framing the image in a fancy
 * way: the background image is divided into nine sections (three across and three down), the corners are rendered
 * unscaled, the central edges are scaled in one direction and the center section is scaled in both directions.
 * <p/>
 * <pre>
 * +----------+----------------+----------+
 * | unscaled |  <- scaled ->  | unscaled |
 * +----------+----------------+----------+
 * |    ^     |       ^        |    ^     |
 * |  scaled  |  <- scaled ->  |  scaled  |
 * |    v     |       v        |    v     |
 * +----------+----------------+----------+
 * | unscaled |  <- scaled ->  | unscaled |
 * +----------+----------------+----------+
 * </pre>
 * </ul>
 */
public class ImageBackground extends BBackground {

    /**
     * Creates an image background in the specified mode using the supplied image.
     */
    public ImageBackground(ImageBackgroundMode mode,
                           BImage image) {
        this(mode, image, null);
    }

    /**
     * Creates an image background in the specified mode using the supplied image and the special frame. This should
     * only be used if one of the framing modes is being used and the supplied frame will be used instead of the default
     * frame which divides the image in thirds.
     */
    public ImageBackground(ImageBackgroundMode mode,
                           BImage image,
                           Insets frame) {
        _mode = mode;
        _image = image;
        _frame = frame;

        // compute the frame for our framed mode if one was not specially provided
        if (_frame == null && (_mode == FRAME_X || _mode == FRAME_Y || _mode == FRAME_XY)) {
            int twidth = _image.getImageWidth(), theight = _image.getImageHeight();
            _frame = new Insets();
            _frame.left = twidth / 3;
            _frame.right = twidth / 3;
            _frame.top = theight / 3;
            _frame.bottom = theight / 3;
        }
    }

    // documentation inherited
    public int getMinimumWidth() {
        return (_mode == FRAME_XY || _mode == FRAME_X)
                ? (_frame.left + _frame.right)
                : _image.getImageWidth();
    }

    /**
     * Returns the minimum height allowed by this background.
     */
    public int getMinimumHeight() {
        return (_mode == FRAME_XY || _mode == FRAME_Y)
                ? _frame.top + _frame.bottom
                : _image.getImageHeight();
    }

    // documentation inherited
    public void render(Renderer renderer,
                       int x,
                       int y,
                       int width,
                       int height,
                       float alpha) {
        super.render(renderer, x, y, width, height, alpha);

        switch (_mode) {
            case CENTER_X:
            case CENTER_XY:
            case CENTER_Y:
                renderCentered(renderer, x, y, width, height, alpha);
                break;

            case SCALE_X:
            case SCALE_XY:
            case SCALE_Y:
                renderScaled(renderer, x, y, width, height, alpha);
                break;

            case TILE_X:
            case TILE_XY:
            case TILE_Y:
                renderTiled(renderer, x, y, width, height, alpha);
                break;

            case FRAME_X:
            case FRAME_XY:
            case FRAME_Y:
                renderFramed(renderer, x, y, width, height, alpha);
                break;
        }
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

    protected void renderCentered(
            Renderer renderer,
            int x,
            int y,
            int width,
            int height,
            float alpha) {
        if (_mode == CENTER_X || _mode == CENTER_XY) {
            x += (width - _image.getWidth()) / 2;
        }
        if (_mode == CENTER_Y || _mode == CENTER_XY) {
            y += (height - _image.getHeight()) / 2;
        }
        _image.render(renderer, x, y, alpha);
    }

    protected void renderScaled(
            Renderer renderer,
            int x,
            int y,
            int width,
            int height,
            float alpha) {
        switch (_mode) {
            case SCALE_X:
                y = (height - _image.getImageHeight()) / 2;
                height = _image.getImageHeight();
                break;
            case SCALE_Y:
                x = (width - _image.getImageWidth()) / 2;
                width = _image.getImageWidth();
                break;
        }
        _image.render(renderer, x, y, width, height, alpha);
    }

    protected void renderTiled(
            Renderer renderer,
            int x,
            int y,
            int width,
            int height,
            float alpha) {
        int iwidth = _image.getImageWidth(), iheight = _image.getImageHeight();
        if (_mode == TILE_X) {
            renderRow(renderer, x, y, width, Math.min(height, iheight), alpha);
        } else if (_mode == TILE_Y) {
            int up = height / iheight;
            iwidth = Math.min(width, iwidth);
            for (int yy = 0; yy < up; yy++) {
                _image.render(renderer, 0, 0, iwidth, iheight,
                        x, y + yy * iheight, iwidth, iheight, alpha);
            }
            int remain = height % iheight;
            if (remain > 0) {
                _image.render(renderer, 0, 0, iwidth, remain,
                        x, y + up * iheight, iwidth, remain, alpha);
            }
        } else if (_mode == TILE_XY) {
            int up = height / iheight;
            for (int yy = 0; yy < up; yy++) {
                renderRow(renderer, x, y + yy * iheight, width, iheight, alpha);
            }
            int remain = height % iheight;
            if (remain > 0) {
                renderRow(renderer, x, y + up * iheight, width, remain, alpha);
            }
        }
    }

    protected void renderRow(
            Renderer renderer,
            int x,
            int y,
            int width,
            int iheight,
            float alpha) {
        int iwidth = _image.getImageWidth();
        int across = width / iwidth;
        for (int xx = 0; xx < across; xx++) {
            _image.render(renderer, 0, 0, iwidth, iheight,
                    x + xx * iwidth, y, iwidth, iheight, alpha);
        }
        int remain = width % iwidth;
        if (remain > 0) {
            _image.render(renderer, 0, 0, remain, iheight,
                    x + across * iwidth, y, remain, iheight, alpha);
        }
    }

    protected void renderFramed(
            Renderer renderer,
            int x,
            int y,
            int width,
            int height,
            float alpha) {
        // render each of our image sections appropriately
        int twidth = _image.getImageWidth(), theight = _image.getImageHeight();

        // draw the corners
        _image.render(renderer, 0, 0, _frame.left, _frame.bottom, x, y, alpha);
        _image.render(renderer, twidth - _frame.right, 0, _frame.right, _frame.bottom,
                x + width - _frame.right, y, alpha);
        _image.render(renderer, 0, theight - _frame.top, _frame.left, _frame.top,
                x, y + height - _frame.top, alpha);
        _image.render(renderer, twidth - _frame.right, theight - _frame.top, _frame.right, _frame.top,
                x + width - _frame.right, y + height - _frame.top, alpha);

        // draw the "gaps"
        int wmiddle = twidth - _frame.getHorizontal(), hmiddle = theight - _frame.getVertical();
        int gwmiddle = width - _frame.getHorizontal(), ghmiddle = height - _frame.getVertical();
        _image.render(renderer, _frame.left, 0, wmiddle, _frame.bottom,
                x + _frame.left, y, gwmiddle, _frame.bottom, alpha);
        _image.render(renderer, _frame.left, theight - _frame.top, wmiddle, _frame.top, x + _frame.left,
                y + height - _frame.top, gwmiddle, _frame.top, alpha);
        _image.render(renderer, 0, _frame.bottom, _frame.left, hmiddle, x, y + _frame.bottom,
                _frame.left, ghmiddle, alpha);
        _image.render(renderer, twidth - _frame.right, _frame.bottom, _frame.right, hmiddle,
                x + width - _frame.right, y + _frame.bottom, _frame.right, ghmiddle, alpha);

        // draw the center
        _image.render(renderer, _frame.left, _frame.bottom, wmiddle, hmiddle,
                x + _frame.left, y + _frame.bottom, gwmiddle, ghmiddle, alpha);
    }

    protected ImageBackgroundMode _mode;
    protected BImage _image;
    protected Insets _frame;

    protected static final int CENTER = 0;
    protected static final int SCALE = 1;
    protected static final int TILE = 2;
    protected static final int FRAME = 3;
}
