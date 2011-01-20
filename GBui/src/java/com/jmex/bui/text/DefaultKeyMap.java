//
// $Id: DefaultKeyMap.java,v 1.2 2007/04/27 19:46:34 vivaldi Exp $
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

import com.jme.input.KeyInput;
import com.jmex.bui.event.InputEvent;

/**
 * Defines a default key mapping for our text editing components.
 */
public class DefaultKeyMap extends BKeyMap {
    public DefaultKeyMap() {
        addMapping(ANY_MODIFIER, KeyInput.KEY_RETURN, EditCommands.ACTION);
        addMapping(ANY_MODIFIER, KeyInput.KEY_NUMPADENTER, EditCommands.ACTION);
        addMapping(ANY_MODIFIER, KeyInput.KEY_BACK, EditCommands.BACKSPACE);
        addMapping(ANY_MODIFIER, KeyInput.KEY_DELETE, EditCommands.DELETE);

        addMapping(ANY_MODIFIER, KeyInput.KEY_LEFT, EditCommands.CURSOR_LEFT);
        addMapping(ANY_MODIFIER, KeyInput.KEY_RIGHT, EditCommands.CURSOR_RIGHT);

        addMapping(ANY_MODIFIER, KeyInput.KEY_HOME, EditCommands.START_OF_LINE);
        addMapping(ANY_MODIFIER, KeyInput.KEY_END, EditCommands.END_OF_LINE);

        addMapping(
                ANY_MODIFIER, KeyInput.KEY_ESCAPE, EditCommands.RELEASE_FOCUS);

        // some emacs commands because I love them so
        addMapping(InputEvent.CTRL_DOWN_MASK, KeyInput.KEY_A,
                   EditCommands.START_OF_LINE);
        addMapping(InputEvent.CTRL_DOWN_MASK, KeyInput.KEY_E,
                   EditCommands.END_OF_LINE);
        addMapping(InputEvent.CTRL_DOWN_MASK, KeyInput.KEY_D,
                   EditCommands.DELETE);
        addMapping(InputEvent.CTRL_DOWN_MASK, KeyInput.KEY_K,
                   EditCommands.CLEAR);
    }
}
