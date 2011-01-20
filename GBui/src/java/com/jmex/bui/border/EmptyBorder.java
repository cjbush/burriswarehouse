//
// $Id: EmptyBorder.java,v 1.2 2007/04/27 19:46:31 vivaldi Exp $
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

package com.jmex.bui.border;

import com.jmex.bui.util.Insets;

/**
 * Defines a border with no rendered geometry but that simply takes up space.
 */
public class EmptyBorder extends BBorder {
    public EmptyBorder(int left,
                       int top,
                       int right,
                       int bottom) {
        _insets = new Insets(left, top, right, bottom);
    }

    // documentation inherited
    public Insets adjustInsets(Insets insets) {
        return _insets.add(insets);
    }

    protected Insets _insets;
}
