//
// $Id: JMEBitmapTextFactory.java,v 1.2 2007/04/27 19:46:34 vivaldi Exp $
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

package com.jmex.bui.text;

import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Spatial;
import com.jme.scene.Text;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.bui.enumeratedConstants.TextEffect;
import com.jmex.bui.util.Dimension;

import java.net.URL;

/**
 * Creates instances of {@link BText} for text rendering.
 */
public class JMEBitmapTextFactory extends BTextFactory {
    /**
     * JME text is all hardcoded to 16 pixels presently
     */
    private static final int HARDCODED_SIZE = 16;

    /**
     * Creates a bitmap text factory with the specified font URL and the supplied per-character width and height.
     */
    public JMEBitmapTextFactory(URL font) {
        this(TextureManager.loadTexture(font,
                Texture.MinificationFilter.NearestNeighborNoMipMaps,
                Texture.MagnificationFilter.NearestNeighbor));
    }

    public JMEBitmapTextFactory(Texture font) {
        if (font == null)
            throw new IllegalArgumentException("font = null");
        _tstate = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        _tstate.setEnabled(true);
        _tstate.setTexture(font);

        _width = font.getImage().getWidth();
        _height = font.getImage().getHeight();

        // create an alpha state that we'll use to blend our font over the
        // background
        _astate = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
        _astate.setBlendEnabled(true);
        _astate.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
        _astate.setDestinationFunction(BlendState.DestinationFunction.One);
        _astate.setEnabled(true);
    }

    // documentation inherited
    public int getHeight() {
        return HARDCODED_SIZE;
    }

    // documentation inherited
    public BText createText(final String text,
                            final ColorRGBA color,
                            TextEffect effect,
                            int effectSize,
                            ColorRGBA effectColor,
                            boolean useAdvance) {
        // compute the dimensions of this text
        final Dimension dims = new Dimension(text.length() * _width, _height);

        // create a text object to display it
        final Text tgeom = new Text("text", text);
        tgeom.setTextureCombineMode(Spatial.TextureCombineMode.Replace);
        tgeom.setRenderState(_tstate);
        tgeom.setRenderState(_astate);
        tgeom.setTextColor(new ColorRGBA(color));

        // wrap it all up in the right object
        return new BText() {
            public int getLength() {
                return text.length();
            }

            public Dimension getSize() {
                return dims;
            }

            public int getHitPos(int x,
                                 int y) {
                return (x - 5) / 10;
            }

            public int getCursorPos(int index) {
                // JME characters are hardcoded to 10x16
                return 10 * index;
            }

            public void wasAdded() {
            }

            public void wasRemoved() {
            }

            public void render(Renderer renderer,
                               int x,
                               int y,
                               float alpha) {
                x -= 4; // TEMP: handle Text offset bug
                tgeom.setLocalTranslation(new Vector3f(x, y, 0));
                tgeom.getTextColor().a = alpha * color.a;
                renderer.draw(tgeom);
            }

            public void release() {
                // TODO: unload factory wide texture at some point?
            }
        };
    }

    // documentation inherited
    public BText[] wrapText(
            String text,
            ColorRGBA color,
            TextEffect effect,
            int effectSize,
            ColorRGBA effectColor,
            int maxWidth
            /*, int[] remain */) {
        // determine how many characters we can fit (note: JME currently
        // assumes all text is width 10 so we propagate that hack)
        int maxChars = maxWidth / 10;

        // deal with the easy case
        if (text.length() <= maxChars) {
//             remain[0] = 0;
            return new BText[]{createText(text, color)};
        }

        // scan backwards from maxChars looking for whitespace
        for (int ii = maxChars; ii >= 0; ii--) {
            if (Character.isWhitespace(text.charAt(ii))) {
                // subtract one to absorb the whitespace that we used to wrap
//                 remain[0] = (text.length() - ii - 1);
                return new BText[]{createText(text.substring(0, ii), color)};
            }
        }

        // ugh, found no whitespace, just hard-wrap at maxChars
//         remain[0] = (text.length() - maxChars);
        return new BText[]{createText(text.substring(0, maxChars), color)};
    }

    protected int _width, _height;
    protected TextureState _tstate;
    protected BlendState _astate;
}
