//
// $Id: InputEvent.java,v 1.2 2007/04/27 19:46:31 vivaldi Exp $
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

package com.jmex.bui.event;

/**
 * Contains information common to all input (keyboard and mouse) events. This includes the state of the modifier keys at
 * the time the event was generated.
 */
public class InputEvent extends BEvent {
    /**
     * A modifier mask indicating that the first mouse button was down at the time this event was generated.
     */
    public static final int BUTTON1_DOWN_MASK = (1 << 0);

    /**
     * A modifier mask indicating that the second mouse button was down at the time this event was generated.
     */
    public static final int BUTTON2_DOWN_MASK = (1 << 1);

    /**
     * A modifier mask indicating that the third mouse button was down at the time this event was generated.
     */
    public static final int BUTTON3_DOWN_MASK = (1 << 2);

    /**
     * A modifier mask indicating that the shift key was down at the time this event was generated.
     */
    public static final int SHIFT_DOWN_MASK = (1 << 3);

    /**
     * A modifier mask indicating that the control key was down at the time this event was generated.
     */
    public static final int CTRL_DOWN_MASK = (1 << 4);

    /**
     * A modifier mask indicating that the alt key was down at the time this event was generated.
     */
    public static final int ALT_DOWN_MASK = (1 << 5);

    /**
     * A modifier mask indicating that the meta key (Windows Logo key on some keyboards) was down at the time this event
     * was generated.
     */
    public static final int META_DOWN_MASK = (1 << 6);

    /**
     * Returns the modifier mask associated with this event.
     */
    public int getModifiers() {
        return _modifiers;
    }

    protected InputEvent(Object source,
                         long when,
                         int modifiers) {
        super(source, when);
        _modifiers = modifiers;
    }

    protected void toString(StringBuffer buf) {
        super.toString(buf);
        buf.append(", mods=").append(_modifiers);
    }

    protected int _modifiers;
}
