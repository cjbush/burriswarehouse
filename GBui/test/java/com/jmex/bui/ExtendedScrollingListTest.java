//
// $Id: ExtendedScrollingListTest.java,v 1.3 2007/05/02 21:34:07 vivaldi Exp $
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

import com.jmex.bui.base.BaseTest;
import com.jmex.bui.layout.GroupLayout;

/**
 * @author timo
 * @since 27Apr07
 */
public class ExtendedScrollingListTest extends BaseTest {
    protected void createWindows() {
        BWindow window = new BWindow(BuiSystem.getStyle(), GroupLayout.makeVStretch());

        BScrollingList<BScrollMessage, BButton> list = new BScrollingList<BScrollMessage, BButton>() {
            public BButton createComponent(BScrollMessage str) {
                BButton b = new BButton(str.getDisplayName(), str.getActionEventName());
                b.addListener(str.getListener());
                return b;
            }
        };

        window.add(list);

        for (int i = 0; i < 40; i++) {
            list.addValue(new BScrollMessage("Dradis Game State", "dradisGameState", listener), true);
        }

        BButton button = new BButton("Back", "back");
        button.addListener(listener);
        window.add(button);

        BuiSystem.addWindow(window);
        window.setSize(400, 400);

        window.center();
    }

    public static void main(String[] args) {
        new ExtendedScrollingListTest().start();
    }
}
