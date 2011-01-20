//
// $Id: CollapsingWindowListener.java,v 1.3 2007/05/08 22:13:50 vivaldi Exp $
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

package com.jmex.bui.listener;

import com.jmex.bui.BComponent;
import com.jmex.bui.BWindow;
import com.jmex.bui.controller.BTiledWindowController;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.headlessWindows.BTitledWindow;
import com.jmex.bui.headlessWindows.BTitledWindow.WindowState;
import com.jmex.bui.util.Dimension;

/**
 * @author timo
 * @since 07May07
 */
public class CollapsingWindowListener extends BTiledWindowController {
    private Dimension maximizedSize = new Dimension(800, 600);

    @Override
    public void actionPerformed(final ActionEvent event) {
        BWindow window = ((BComponent) event.getSource()).getWindow();
        if (window instanceof BTitledWindow) {
            BTitledWindow titledWindow = (BTitledWindow) window;
            if (event.getAction().equals(BTitledWindow.WINDOW_MINIMIZE_ACTION)) {
                minimize(titledWindow);
            } else if (event.getAction().equals(BTitledWindow.WINDOW_MAXIMIZE_ACTION)) {
                maximize(titledWindow);
            } else if (event.getAction().equals(BTitledWindow.WINDOW_CLOSE_ACTION)) {
                close(titledWindow);
            } else {
                super.actionPerformed(event);
            }
        }
    }

    public void setMaximizedSize(Dimension maxSize) {
        this.maximizedSize = maxSize;
    }

    protected void maximize(BTitledWindow window) {
        if (window.getWindowState() != WindowState.MAXIMIZED) {
            window.maximize(maximizedSize);
        }
        // restore the window
        else {
            window.restoreSize();
        }
    }

    protected void minimize(BTitledWindow window) {
        if (window.getWindowState() != WindowState.MINIMIZED) {
            window.minimize();
        }
        // restore the window
        else {
            window.restoreSize();
        }
    }

    protected void close(BTitledWindow window) {
        window.dismiss();
    }

    protected void cancel(BTitledWindow windoww) {
        windoww.dismiss();
    }
}
