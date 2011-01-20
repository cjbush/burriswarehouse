//
// $Id: TiledMessageWindowTest.java,v 1.7 2007/05/08 22:13:50 vivaldi Exp $
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
import com.jmex.bui.headlessWindows.BTitledWindow;
import com.jmex.bui.headlessWindows.MessageWindowUtil;

/**
 * @author timo
 * @since 27Apr07
 */
public class TiledMessageWindowTest extends BaseTest {
    @Override
    protected void createWindows() {
        for (int i = 0; i < 3; i++) {
            BTitledWindow mw = MessageWindowUtil.createMessageBox("Message box #" + i,
                                                                  "You won't be able to dismiss this evil window.");
            BuiSystem.getRootNode().addWindow(mw);
        }
    }

    public static void main(String[] args) {
        new TiledMessageWindowTest().start();
    }
}
