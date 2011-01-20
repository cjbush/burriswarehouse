//
// $Id: FocusEvent.java,v 1.2 2007/04/27 19:46:31 vivaldi Exp $
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
 * An event dispatched to a component when it receives or loses the focus.
 */
public class FocusEvent extends BEvent {
    /**
     * Indicates that a component gained the focus.
     */
    public static final int FOCUS_GAINED = 0;

    /**
     * Indicates that a component lost the focus.
     */
    public static final int FOCUS_LOST = 1;

    public FocusEvent(Object source,
                      long when,
                      int type) {
        super(source, when);
        _type = type;
    }

    /**
     * Indicates whether this was a {@link #FOCUS_GAINED} or {@link #FOCUS_LOST} event.
     */
    public int getType() {
        return _type;
    }

    // documentation inherited
    public boolean propagateUpHierarchy() {
        return false;
    }

    protected int _type;
}
