//
// $Id: BMessageWindowUtil.java,v 1.7 2007/05/08 22:13:49 vivaldi Exp $
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

package com.jmex.bui.headlessWindows;

import com.jmex.bui.BLabel;
import com.jmex.bui.BMessage;
import com.jmex.bui.BTitleBar;
import com.jmex.bui.BuiSystem;
import com.jmex.bui.enumeratedConstants.TitleOptions;
import com.jmex.bui.listener.CollapsingWindowListener;

/**
 * @author timo
 * @since 27Apr07
 */
public class MessageWindowUtil {

    private static final CollapsingWindowListener DEFAULT_LISTENER = new CollapsingWindowListener();

    public static BTitledWindow createMessageBox(String name,
                                                 String title, TitleOptions options, String message) {
        BTitleBar tb = new BTitleBar(name, new BLabel(title),
                                     options);
        BMessage bMessage = new BMessage(name, new BLabel(message));
        BTitledWindow window = new BTitledWindow(name, tb,
                                                 null, BuiSystem.getStyle());
        window.getComponentArea().add(bMessage);
        window.setSize(400, 200);
        window.center();
        window.addListener(DEFAULT_LISTENER);
        return window;
    }

    public static BTitledWindow createMessageBox(String title, String message) {
        return createMessageBox(null, title, TitleOptions.NONE, message);
    }
}
