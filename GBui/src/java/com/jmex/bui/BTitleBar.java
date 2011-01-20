//
// $Id: BTitleBar.java,v 1.3 2007/05/02 21:34:01 vivaldi Exp $
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

import com.jmex.bui.enumeratedConstants.TitleOptions;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.BEvent;
import com.jmex.bui.event.ComponentListener;
import com.jmex.bui.headlessWindows.BTitledWindow;
import com.jmex.bui.layout.BorderLayout;
import com.jmex.bui.layout.GroupLayout;
import com.jmex.bui.layout.Justification;
import com.jmex.bui.listener.CollapsingWindowListener;

import java.util.ArrayList;

/**
 * @author timo
 * @since 27Apr07
 */
public class BTitleBar extends BContainer {
    private BLabel title;
    private ArrayList<BButton> buttons = new ArrayList<BButton>(3);

    public BTitleBar(final String name,
                     final BLabel title,
                     final TitleOptions options) {
        super(name, new BorderLayout());

        this.title = title;
        if (title != null) {
            add(title, BorderLayout.WEST);
        }
        setStyleClass("titlebar");
        createButtons(options);
    }

    public BTitleBar(final String name,
                     final String title,
                     final TitleOptions options) {
        this(name, new BLabel(title), options);
    }

    public BTitleBar(final String _name,
                     final String _title,
                     final TitleOptions options,
                     final String titleBarStyle) {
        this(_name, _title, options);
        setStyleClass(titleBarStyle);
    }

    @Override
    public void addListener(ComponentListener listener) {
        if (listener instanceof CollapsingWindowListener) {
            BButton button;
            for (int i = 0; i < buttons.size(); i++) {
                button = buttons.get(i);
                button.removeAllListeners();
                button.addListener(listener);
            }
        }
        super.addListener(listener);
    }

    private void createButtons(TitleOptions options) {
        BContainer buttonContainer = new BContainer(GroupLayout.makeHoriz(Justification.RIGHT));
        BButton button;
        switch (options) {
            case MIN:
                button = new BButton("", BTitledWindow.WINDOW_MINIMIZE_ACTION);
                button.setStyleClass("minimizebutton");
                buttonContainer.add(button);
                buttons.add(button);
                break;
            case MAX:
                button = new BButton("", BTitledWindow.WINDOW_MAXIMIZE_ACTION);
                button.setStyleClass("maximizebutton");
                buttonContainer.add(button);
                buttons.add(button);
                break;
            case CLOSE:
                button = new BButton("", BTitledWindow.WINDOW_CLOSE_ACTION);
                button.setStyleClass("closebutton");
                buttonContainer.add(button);
                buttons.add(button);
                break;
            case MAX_MIN:
                button = new BButton("", BTitledWindow.WINDOW_MINIMIZE_ACTION);
                button.setStyleClass("minimizebutton");
                buttonContainer.add(button);
                buttons.add(button);
                button = new BButton("", BTitledWindow.WINDOW_MAXIMIZE_ACTION);
                button.setStyleClass("maximizebutton");
                buttonContainer.add(button);
                buttons.add(button);
                break;
            case MIN_CLOSE:
                button = new BButton("", BTitledWindow.WINDOW_MINIMIZE_ACTION);
                button.setStyleClass("minimizebutton");
                buttonContainer.add(button);
                buttons.add(button);
                button = new BButton("", BTitledWindow.WINDOW_CLOSE_ACTION);
                button.setStyleClass("closebutton");
                buttonContainer.add(button);
                buttons.add(button);
                break;
            case MIN_MAX_CLOSE:
                button = new BButton("", BTitledWindow.WINDOW_MINIMIZE_ACTION);
                button.setStyleClass("minimizebutton");
                buttonContainer.add(button);
                buttons.add(button);
                button = new BButton("", BTitledWindow.WINDOW_MAXIMIZE_ACTION);
                button.setStyleClass("maximizebutton");
                buttonContainer.add(button);
                buttons.add(button);
                button = new BButton("", BTitledWindow.WINDOW_CLOSE_ACTION);
                button.setStyleClass("closebutton");
                buttonContainer.add(button);
                buttons.add(button);
                break;
            case NONE:
                break;
            default:
                throw new RuntimeException("Option not implemented");
        }
        if (options != TitleOptions.NONE) {
            add(buttonContainer, BorderLayout.EAST);
        }
    }

    public void setTitle(String windowTitle) {
        title.setText(windowTitle);
    }

    @Override
    public boolean dispatchEvent(BEvent event) {
        if (event instanceof ActionEvent) {
            ActionEvent ev = (ActionEvent) event;
            if (ev.getAction().equals(BTitledWindow.WINDOW_CLOSE_ACTION) ||
                ev.getAction().equals(BTitledWindow.WINDOW_MAXIMIZE_ACTION) ||
                ev.getAction().equals(BTitledWindow.WINDOW_MINIMIZE_ACTION)) {

            }
        }
        return super.dispatchEvent(event);
    }
}
