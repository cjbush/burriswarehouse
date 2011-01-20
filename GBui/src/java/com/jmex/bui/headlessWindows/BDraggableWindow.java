//
// $Id: BDraggableWindow.java,v 1.4 2007/05/08 22:13:49 vivaldi Exp $
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

package com.jmex.bui.headlessWindows;

import com.jmex.bui.BStyleSheet;
import com.jmex.bui.BWindow;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.BEvent;
import com.jmex.bui.event.MouseEvent;
import com.jmex.bui.layout.BLayoutManager;

/**
 * @author timo
 * @since 27Apr07
 */
public class BDraggableWindow extends BWindow {
    public static final String WINDOW_ACTIVATE_ACTION = "activate window";
    private boolean armed;
    private int grabOffsetX = -1;
    private int grabOffsetY = -1;
    private boolean dragging = false;

    public BDraggableWindow(final String name,
                            final BStyleSheet style,
                            final BLayoutManager layout) {
        super(name, style, layout);
    }

    public BDraggableWindow(final BStyleSheet style,
                            final BLayoutManager layout) {
        super(style, layout);
    }

    @Override
    public boolean dispatchEvent(final BEvent event) {
        if (event instanceof MouseEvent) {
            MouseEvent mev = (MouseEvent) event;
            switch (mev.getType()) {
                case MouseEvent.MOUSE_ENTERED:
                    armed = true;
                    break; // we don't consume this event

                case MouseEvent.MOUSE_EXITED:
                    if (!dragging) {
                        armed = false;
                    }
                    break; // we don't consume this event
                case MouseEvent.MOUSE_DRAGGED:
                    if (dragging) {
                        _x = mev.getX() + grabOffsetX;
                        _y = mev.getY() + grabOffsetY;
                    }
                    break;
                case MouseEvent.MOUSE_PRESSED:
                    if (mev.getButton() == 0) {
                        armed = true;
                        dragging = true;

                        // remember the offset of the mouse pointer (won't change until released)
                        grabOffsetY = _y - mev.getY();
                        grabOffsetX = _x - mev.getX();

                        fireAction(mev.getWhen(), mev.getModifiers());
                    }
                    return true; // consume this event

                case MouseEvent.MOUSE_RELEASED:
                    if (armed && dragging) {
                        // this means the windows reached its final position (window released)
                        armed = false;
                        dragging = false;
                        windowReleased(mev);
                    }
                    return true; // consume this event
            }
        }

        return super.dispatchEvent(event);
    }

    protected void fireAction(final long when,
                              final int modifiers) {
        emitEvent(new ActionEvent(this, when, modifiers, WINDOW_ACTIVATE_ACTION));
    }

    /**
     * This method is called when the window is released. It does nothing by default.
     *
     * @param event The mouse event generated when the window was released.
     */
    protected void windowReleased(MouseEvent event) {}
}
