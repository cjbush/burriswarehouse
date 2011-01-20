//
// $Id: MouseListener.java,v 1.2 2007/04/27 19:46:31 vivaldi Exp $
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
 * Dispatches mouse events to listeners on a component.
 */
public interface MouseListener extends ComponentListener {
    /**
     * Dispatched when a button is pressed within the bounds of the target component.
     */
    public void mousePressed(MouseEvent event);

    /**
     * Dispatched when a button is released after having been pressed within the bounds of the target component.
     */
    public void mouseReleased(MouseEvent event);

    /**
     * Dispatched when the mouse enters the bounds of the target component.
     */
    public void mouseEntered(MouseEvent event);

    /**
     * Dispatched when the mouse exits the bounds of the target component.
     */
    public void mouseExited(MouseEvent event);
}
