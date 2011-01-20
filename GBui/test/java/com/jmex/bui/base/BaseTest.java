//
// $Id: BaseTest2.java,v 1.8 2007/05/02 21:34:06 vivaldi Exp $
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

package com.jmex.bui.base;

import com.jme.app.SimpleGame;
import com.jme.input.KeyBindingManager;
import com.jme.input.MouseInput;
import com.jme.renderer.ColorRGBA;
import com.jmex.bui.BuiSystem;
import com.jmex.bui.PolledRootNode;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.ActionListener;

/**
 * A base class for our various visual tests.
 */
public abstract class BaseTest extends SimpleGame {
    protected ActionListener listener = new ActionListener() {
        public void actionPerformed(final ActionEvent event) {
            System.out.println(event.getAction());
        }
    };

    @Override
    protected void simpleInitGame() {
        // we don't hide the cursor
        MouseInput.get().setCursorVisible(true);

        BuiSystem.init(new PolledRootNode(timer, input), "/rsrc/style2.bss");
        rootNode.attachChild(BuiSystem.getRootNode());

        createWindows();

        KeyBindingManager kb = KeyBindingManager.getKeyBindingManager();

        // these just get in the way
        kb.remove("toggle_pause");
        kb.remove("toggle_wire");
        kb.remove("toggle_lights");
        kb.remove("toggle_bounds");
        kb.remove("camera_out");

        lightState.setEnabled(false);

        display.getRenderer().setBackgroundColor(ColorRGBA.gray);
    }

    @Override
    protected void simpleUpdate() {}

    protected abstract void createWindows();
}
