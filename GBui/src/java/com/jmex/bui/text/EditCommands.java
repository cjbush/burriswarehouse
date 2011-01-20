//
// $Id: EditCommands.java,v 1.2 2007/04/27 19:46:34 vivaldi Exp $
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

/**
 * Defines the various commands handled by our text editing components.
 */
public interface EditCommands {
    /**
     * A text editing command.
     */
    public static final int ACTION = 0;

    /**
     * A text editing command.
     */
    public static final int BACKSPACE = 1;

    /**
     * A text editing command.
     */
    public static final int DELETE = 2;

    /**
     * A text editing command.
     */
    public static final int CURSOR_LEFT = 3;

    /**
     * A text editing command.
     */
    public static final int CURSOR_RIGHT = 4;

    /**
     * A text editing command.
     */
    public static final int START_OF_LINE = 5;

    /**
     * A text editing command.
     */
    public static final int END_OF_LINE = 6;

    /**
     * A text editing command.
     */
    public static final int RELEASE_FOCUS = 7;

    /**
     * A text editing command.
     */
    public static final int CLEAR = 8;
}
