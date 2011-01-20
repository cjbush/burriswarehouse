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

import com.jmex.bui.BLabel;
import com.jmex.bui.BWindow;
import com.jmex.bui.BuiSystem;
import com.jmex.bui.background.BlankBackground;
import com.jmex.bui.icon.BIcon;
import com.jmex.bui.layout.BorderLayout;

/** @author ivicaz */
public class BDragIconWindow extends BWindow {
  private BLabel iconLabel;

  public BDragIconWindow() {
    super("dragging-notification-window", BuiSystem.getStyle(), new BorderLayout());
    setBackground(DEFAULT, new BlankBackground());
    iconLabel = new BLabel("");
    add(iconLabel, BorderLayout.CENTER);
  }

  public void setIcon(BIcon icon) {
    iconLabel.setIcon(icon);
  }
}
