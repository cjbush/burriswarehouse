//
// $Id: BBasicMessage.java,v 1.3 2007/05/02 21:34:01 vivaldi Exp $
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
package com.jmex.bui.dragndrop;

import com.jmex.bui.BuiSystem;
import com.jmex.bui.event.BEvent;

/** @author ivicaz */
public class BDropEvent extends BEvent {
  public static final Object NOTHING = new Object();
  private BDragEvent BDragEvent;

  public BDropEvent(Object source, BDragEvent BDragEvent) {
    super(source, BuiSystem.getRootNode().getTickStamp());
    if (BDragEvent == null)
      throw new IllegalArgumentException("BDragEvent = null");
    this.BDragEvent = BDragEvent;
  }

  public Object getSource() {
    return super.getSource();
  }

  public BDragEvent getDragEvent() {
    return BDragEvent;
  }

  @Override public String toString() {
    return new StringBuilder().append("BDropEvent@").append(hashCode()).append("{").
            append("source=").append(getSource()).append(", BDragEvent=").append(BDragEvent).append("}").toString();
  }
}
