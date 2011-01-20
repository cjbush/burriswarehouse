//
// $Id: ScrollingListTest.java,v 1.3 2007/05/02 20:23:04 vivaldi Exp $
//
// BUI - a user interface library for the JME 3D engine
// Copyright (C) 2006, Pär Winzell, All Rights Reserved
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

package com.jmex.bui

import com.jmex.bui.BButton
import com.jmex.bui.BButtonScrollingList
import com.jmex.bui.BConstants
import com.jmex.bui.BContainer
import com.jmex.bui.BRootNode
import com.jmex.bui.BScrollingList
import com.jmex.bui.BStyleSheet
import com.jmex.bui.BWindow
import com.jmex.bui.SLTWBActionListener
import com.jmex.bui.base.BaseTest
import com.jmex.bui.event.ActionListener
import com.jmex.bui.layout.BorderLayout
import com.jmex.bui.layout.GroupLayout
import java.util.logging.Level
import java.util.logging.Logger


public class ScrollingListTestWithButton extends BaseTest implements BConstants {
  final ActionListener listener = new SLTWBActionListener()

  BScrollingList<String, BButton> list;

  @Override
  protected void createWindows(BRootNode root,
                               BStyleSheet style) {
    final def window = new BWindow(style, new BorderLayout(5, 5));
    final def removeAll = new BButton("removeAll", "removeAll");
    final def removeValues = new BButton("removeValues", "removeValues");
    final def populate = new BButton("populate", "populate");
    final def c = new BContainer();

    list = new BButtonScrollingList<String, BButton>()

    root.addWindow(window);

    c.setLayoutManager(GroupLayout.makeHStretch())
    c.with() {
      add(removeAll)
      add(populate);
      add(removeValues);
    }

    window.with() {
      add(list, BorderLayout.CENTER);
      setSize(400, 400);
      setLocation(25, 25);

      add(c, BorderLayout.SOUTH);

    }

    listener.list = list

    listener.populate();

    removeAll.addListener(listener);
    removeValues.addListener(listener);
    populate.addListener(listener);
  }

  public static void main(String[] args) {
    Logger.getLogger("com.jmex.bui").setLevel(Level.WARNING);
    ScrollingListTestWithButton test = new ScrollingListTestWithButton();
    test.start();
  }
}