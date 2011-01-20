//
// $Id: BSlider.java,v 1.2 2007/04/27 19:46:29 vivaldi Exp $
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

import com.jme.renderer.Renderer;
import com.jmex.bui.enumeratedConstants.Orientation;
import com.jmex.bui.event.BEvent;
import com.jmex.bui.event.MouseEvent;
import com.jmex.bui.icon.BIcon;
import com.jmex.bui.util.Dimension;
import com.jmex.bui.util.Insets;

/**
 * Displays a track with a little frob somewhere along its length that allows a user to select a smoothly varying value
 * between two bounds.
 */
public class BSlider extends BComponent
        implements BConstants {
    /**
     * Creates a slider with the specified orientation, range and value.
     *
     * @param orient either {@link com.jmex.bui.enumeratedConstants.Orientation#HORIZONTAL} or {@link com.jmex.bui.enumeratedConstants.Orientation#VERTICAL}.
     */
    public BSlider(Orientation orient,
                   int min,
                   int max,
                   int value) {
        this(orient, new BoundedRangeModel(min, value, 0, max));
    }

    /**
     * Creates a slider with the specified orientation and range model. Note that the extent must be set to zero.
     *
     * @param orient either {@link com.jmex.bui.enumeratedConstants.Orientation#HORIZONTAL} or {@link com.jmex.bui.enumeratedConstants.Orientation#VERTICAL}.
     */
    public BSlider(Orientation orient,
                   BoundedRangeModel model) {
        _orient = orient;
        _model = model;
    }

    /**
     * Returns a reference to the slider's range model.
     */
    public BoundedRangeModel getModel() {
        return _model;
    }

    @Override
    // documentation inherited
    protected String getDefaultStyleClass() {
        return ((_orient == Orientation.HORIZONTAL) ? "h" : "v") + "slider";
    }

    @Override
    // documentation inherited
    protected void configureStyle(BStyleSheet style) {
        super.configureStyle(style);

        // load up our frobs
        for (int ii = 0; ii < getStateCount(); ii++) {
            _frobs[ii] = style.getIcon(this, getStatePseudoClass(ii));
        }
    }

    @Override
    // documentation inherited
    protected void wasAdded() {
        super.wasAdded();
        for (int ii = 0; ii < _frobs.length; ii++) {
            if (_frobs[ii] != null) {
                _frobs[ii].wasAdded();
            }
        }
    }

    @Override
    // documentation inherited
    protected void wasRemoved() {
        super.wasRemoved();
        for (int ii = 0; ii < _frobs.length; ii++) {
            if (_frobs[ii] != null) {
                _frobs[ii].wasRemoved();
            }
        }
    }

    @Override
    // documentation inherited
    protected Dimension computePreferredSize(int whint,
                                             int hhint) {
        Dimension psize =
                new Dimension(getFrob().getWidth(), getFrob().getHeight());
        if (_orient == Orientation.HORIZONTAL) {
            psize.width *= 2;
        } else {
            psize.height *= 2;
        }
        return psize;
    }

    @Override
    // documentation inherited
    public boolean dispatchEvent(BEvent event) {
        if (isEnabled() && event instanceof MouseEvent) {
            MouseEvent mev = (MouseEvent) event;
            int mx = mev.getX() - getAbsoluteX(),
                    my = mev.getY() - getAbsoluteY();
            switch (mev.getType()) {
                case MouseEvent.MOUSE_PRESSED:
                    if (mev.getButton() == 0) {
                        // move the slider based on the current mouse position
                        updateValue(mx, my);
                    }
                    break;

                case MouseEvent.MOUSE_DRAGGED:
                    // move the slider based on the current mouse position
                    updateValue(mx, my);
                    break;

                case MouseEvent.MOUSE_WHEELED:
                    // move by 1/10th if we're wheeled
                    int delta = _model.getRange() / 10, value = _model.getValue();
                    _model.setValue(mev.getDelta() > 0 ?
                                    value + delta : value - delta);
                    break;

                default:
                    return super.dispatchEvent(event);
            }

            return true;
        }

        return super.dispatchEvent(event);
    }

    @Override
    // documentation inherited
    protected void renderComponent(Renderer renderer) {
        super.renderComponent(renderer);

        // render our frob at the appropriate location
        Insets insets = getInsets();
        BIcon frob = getFrob();
        int x, y, range = _model.getRange();
        int offset = _model.getValue() - _model.getMinimum();
        if (_orient == Orientation.HORIZONTAL) {
            y = (getHeight() - frob.getHeight()) / 2;
            x = insets.left + (getWidth() - insets.getHorizontal() -
                               frob.getWidth()) * offset / range;
        } else {
            x = (getWidth() - frob.getWidth()) / 2;
            y = insets.bottom + (getHeight() - insets.getVertical() -
                                 frob.getHeight()) * offset / range;
        }
        frob.render(renderer, x, y, _alpha);
    }

    protected void updateValue(int mx,
                               int my) {
        Insets insets = getInsets();
        BIcon frob = getFrob();
        if (_orient == Orientation.HORIZONTAL) {
            int fwid = frob.getWidth();
            _model.setValue((mx - fwid / 2) * _model.getRange() /
                            (getWidth() - insets.getHorizontal() - fwid));
        } else {
            int fhei = frob.getHeight();
            _model.setValue((my - fhei / 2) * _model.getRange() /
                            (getHeight() - insets.getVertical() - fhei));
        }
    }

    protected BIcon getFrob() {
        BIcon frob = _frobs[getState()];
        return (frob != null) ? frob : _frobs[DEFAULT];
    }

    protected Orientation _orient;
    protected BoundedRangeModel _model;
    protected BIcon[] _frobs = new BIcon[getStateCount()];
}
