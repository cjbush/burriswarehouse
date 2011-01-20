//
// $Id: BButton.java,v 1.2 2007/04/27 19:46:29 vivaldi Exp $
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

import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.ActionListener;
import com.jmex.bui.event.BEvent;
import com.jmex.bui.event.MouseEvent;
import com.jmex.bui.icon.BIcon;

/**
 * Displays a simple button that can be depressed and which generates an action event when pressed and released.
 */
public class BButton extends BLabel implements BConstants {
    /**
     * Indicates that this button is in the down state.
     */
    public static final int DOWN = BComponent.STATE_COUNT;// + 0;

    /**
     * Creates a button with the specified textual label.
     *
     * @param text String
     */
    public BButton(String text) {
        this(text, "");
    }

    /**
     * Creates a button with the specified label and action. The action will be dispatched via an {@link ActionEvent}
     * when the button is clicked.
     *
     * @param text   String
     * @param action String
     */
    public BButton(String text,
                   String action) {
        this(text, null, action);
    }

    /**
     * Creates a button with the specified label and action. The action will be dispatched via an {@link ActionEvent} to
     * the specified {@link ActionListener} when the button is clicked.
     *
     * @param text     String
     * @param listener ActionLIstener
     * @param action   String
     */
    public BButton(String text,
                   ActionListener listener,
                   String action) {
        super(text);
        _action = action;
        if (listener != null) {
            addListener(listener);
        }
    }

    /**
     * Creates a button with the specified icon and action. The action will be dispatched via an {@link ActionEvent}
     * when the button is clicked.
     *
     * @param icon   BIcon
     * @param action String
     */
    public BButton(BIcon icon,
                   String action) {
        this(icon, null, action);
    }

    /**
     * Creates a button with the specified icon and action. The action will be dispatched via an {@link ActionEvent} to
     * the specified {@link ActionListener} when the button is clicked.
     *
     * @param icon     BIcon
     * @param listener ActionListener
     * @param action   String
     */
    public BButton(BIcon icon,
                   ActionListener listener,
                   String action) {
        super(icon);
        _action = action;
        if (listener != null) {
            addListener(listener);
        }
    }

    /**
     * Configures the action to be generated when this button is clicked.
     *
     * @param action String
     */
    public void setAction(String action) {
        _action = action;
    }

    /**
     * Returns the action generated when this button is clicked.
     *
     * @return String action
     */
    public String getAction() {
        return _action;
    }

    @Override
    // documentation inherited
    public int getState() {
        int state = super.getState();
        if (state == DISABLED) {
            return state;
        }

        if (_armed && _pressed) {
            return DOWN;
        } else {
            return state; // most likely HOVER
        }
    }

    @Override
    // documentation inherited
    public boolean dispatchEvent(BEvent event) {
        if (isEnabled() && event instanceof MouseEvent) {
            int ostate = getState();
            MouseEvent mev = (MouseEvent) event;
            switch (mev.getType()) {
                case MouseEvent.MOUSE_ENTERED:
                    _armed = _pressed;
                    // let the normal component hovered processing take place
                    return super.dispatchEvent(event);

                case MouseEvent.MOUSE_EXITED:
                    _armed = false;
                    // let the normal component hovered processing take place
                    return super.dispatchEvent(event);

                case MouseEvent.MOUSE_PRESSED:
                    if (mev.getButton() == 0) {
                        _pressed = true;
                        _armed = true;
                    } else if (mev.getButton() == 1) {
                        // clicking the right mouse button after arming the
                        // button disarms it
                        _armed = false;
                    }
                    break;

                case MouseEvent.MOUSE_RELEASED:
                    if (_armed && _pressed) {
                        // create and dispatch an action event
                        fireAction(mev.getWhen(), mev.getModifiers());
                        _armed = false;
                    }
                    _pressed = false;
                    break;

                default:
                    return super.dispatchEvent(event);
            }

            // update our background image if necessary
            int state = getState();
            if (state != ostate) {
                stateDidChange();
            }

            return true;
        }

        return super.dispatchEvent(event);
    }

    @Override
    // documentation inherited
    protected String getDefaultStyleClass() {
        return "button";
    }

    @Override
    // documentation inherited
    protected int getStateCount() {
        return STATE_COUNT;
    }

    @Override
    // documentation inherited
    protected String getStatePseudoClass(int state) {
        if (state >= BComponent.STATE_COUNT) {
            return STATE_PCLASSES[state - BComponent.STATE_COUNT];
        } else {
            return super.getStatePseudoClass(state);
        }
    }

    @Override
    // documentation inherited
    protected void configureStyle(BStyleSheet style) {
        super.configureStyle(style);

        // check to see if our stylesheet provides us with an icon
        if (_label.getIcon() == null) {
            BIcon icon = style.getIcon(this, getStatePseudoClass(DEFAULT));
            if (icon != null) {
                _label.setIcon(icon);
            }
        }
    }

    /**
     * Called when the button is "clicked" which may due to the mouse being pressed and released while over the button
     * or due to keyboard manipulation while the button has focus.
     *
     * @param when      long
     * @param modifiers int
     */
    protected void fireAction(long when,
                              int modifiers) {
        emitEvent(new ActionEvent(this, when, modifiers, _action));
    }

    protected boolean _armed, _pressed;
    protected String _action;

    protected static final int STATE_COUNT = BComponent.STATE_COUNT + 1;
    protected static final String[] STATE_PCLASSES = {"down"};
}
