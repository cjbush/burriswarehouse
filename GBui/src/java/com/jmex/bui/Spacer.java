//
// $Id: Spacer.java,v 1.2 2007/04/27 19:46:29 vivaldi Exp $
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

import com.jmex.bui.util.Dimension;

/**
 * Takes up space!
 */
public class Spacer extends BComponent {
    /**
     * Creates a 1x1 spacer that will presumably be later resized by a layout manager in some appropriate manner.
     */
    public Spacer() {
        this(1, 1);
    }

    /**
     * Creates a spacer with the specified preferred dimensions.
     *
     * @param prefWidth  int
     * @param prefHeight int
     */
    public Spacer(int prefWidth,
                  int prefHeight) {
        setPreferredSize(new Dimension(prefWidth, prefHeight));
    }
}
