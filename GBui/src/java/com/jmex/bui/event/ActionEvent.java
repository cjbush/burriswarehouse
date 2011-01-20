//
// $Id: ActionEvent.java,v 1.2 2007/04/27 19:46:31 vivaldi Exp $
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
 * Dispatched by a component when some sort of component-specific action has occurred.
 */
public class ActionEvent extends InputEvent {
    public ActionEvent(Object source,
                       long when,
                       int modifiers,
                       String action) {
        super(source, when, modifiers);
        _action = action;
    }

    /**
     * Returns the action associated with this event.
     */
    public String getAction() {
        return _action;
    }

    // documentation inherited
    @Override
    public void dispatch(ComponentListener listener) {
        super.dispatch(listener);
        if (listener instanceof ActionListener) {
            ((ActionListener) listener).actionPerformed(this);
        }
    }

    // documentation inherited
    @Override
    public boolean propagateUpHierarchy() {
        return false;
    }

    @Override
    protected void toString(StringBuffer buf) {
        super.toString(buf);
        buf.append(", action=").append(_action);
    }

    protected String _action;
}
