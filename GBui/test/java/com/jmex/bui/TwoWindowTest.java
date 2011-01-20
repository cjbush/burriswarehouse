//
// $Id: TwoWindowTest.java,v 1.3 2007/05/02 21:34:07 vivaldi Exp $
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
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.ActionListener;
import com.jmex.bui.layout.GroupLayout;

/**
 * @author timo
 * @since 27Apr07
 */
public class TwoWindowTest extends BaseTest {
    private ActionListener listener2 = new ActionListener() {
        public void actionPerformed(ActionEvent event) {
            handleInput(event);
        }
    };

    private BWindow login;
    private BWindow mainMenu;

    protected void createWindows() {
        setupLoginWindow();
        setupMainMenuWindow();
    }

    public void setupLoginWindow() {
        //instantiate our login window
        //set our style from our BuiSystem and set our layout to stretch everything vertically
        login = new BWindow(BuiSystem.getStyle(), GroupLayout.makeVStretch());

        //set the size of our login window to 400x400
        login.setSize(400, 400);

        //create a new BButton called "loginButton" with the display "Login" and an "actionMessage" of "login"
        BButton loginButton = new BButton("Login", "login");

        //add our listener2 to the loginButton so it knows what to do with the "actionMessage" when the button is clicked
        loginButton.addListener(listener2);

        //add the loginButton to our login window
        login.add(loginButton);

        //add our login window to our BRootNode
        BuiSystem.addWindow(login);

        //center our window -- this could go anywhere in the code I simply place it after my addWindow so I remember that I did it
        login.center();
    }

    public void setupMainMenuWindow() {
        mainMenu = new BWindow(BuiSystem.getStyle(), GroupLayout.makeVStretch());
        mainMenu.setSize(400, 400);

        //create a new BButton that displays "Back" and has an "actionMessage" of "back"
        BButton backButton = new BButton("Back", "back");
        backButton.addListener(listener2);
        mainMenu.add(backButton);
    }

    public static void main(String[] args) {
        TwoWindowTest app = new TwoWindowTest();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
        app.start();
    }

    public void handleInput(ActionEvent event) {
        String action = event.getAction();
        if (action.equals("login")) {
            BuiSystem.push(login);

            BuiSystem.addWindow(mainMenu);
            mainMenu.center();
            login.dismiss();
        } else if (action.equals("back")) {
            BuiSystem.back(mainMenu);
        }
    }
}
