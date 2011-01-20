//
// $Id: BLayoutManager.java,v 1.2 2007/04/27 19:46:32 vivaldi Exp $
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

package com.jmex.bui.layout;

import com.jmex.bui.BComponent;
import com.jmex.bui.BContainer;
import com.jmex.bui.util.Dimension;

/**
 * Layout managers implement a policy for laying out the children in a container. They must provide routines for
 * computing the preferred size of a target container and for actually laying out its children.
 */
public abstract class BLayoutManager {
    /**
     * Components added to a container will result in a call to this method, informing the layout manager of said
     * constraints. The default implementation does nothing.
     */
    public void addLayoutComponent(BComponent comp,
                                   Object constraints) {
    }

    /**
     * Components removed to a container for which a layout manager has been configured will result in a call to this
     * method. The default implementation does nothing.
     */
    public void removeLayoutComponent(BComponent comp) {
    }

    /**
     * Computes the preferred size for the supplied container, based on the preferred sizes of its children and the
     * layout policy implemented by this manager. <em>Note:</em> it is not necessary to add the container's insets to
     * the returned preferred size.
     */
    public abstract Dimension computePreferredSize(
            BContainer target,
            int whint,
            int hhint);

    /**
     * Effects the layout policy of this manager on the supplied target, adjusting the size and position of its children
     * based on the size and position of the target at the time of this call. <em>Note:</em> the target's insets must be
     * accounted for when laying out the children.
     */
    public abstract void layoutContainer(BContainer target);
}
