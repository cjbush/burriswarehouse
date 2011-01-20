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

import com.jmex.bui.BComponent;
import com.jmex.bui.BuiSystem;
import com.jmex.bui.event.BEvent;

/** @author ivicaz */
public class BDragEvent extends BEvent {
  private Object draggedObject;

  public BDragEvent(BComponent source, Object draggedObject) {
    super(source, BuiSystem.getRootNode().getTickStamp());
    this.draggedObject = draggedObject;
  }

  public BComponent getSource() {
    return (BComponent) super.getSource();
  }

  public Object getDraggedObject() {
    return draggedObject;
  }

  @Override public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("BDragEvent@").append(hashCode()).append("{");
    builder.append("source = ").append(getSource()).append(", ");
    builder.append("draggedObject = ").append(draggedObject).append("}");
    return builder.toString();
  }
}
