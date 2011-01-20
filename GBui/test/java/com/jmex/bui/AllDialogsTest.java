//
// $Id: AllDialogsTest.java,v 1.6 2007/05/02 21:34:06 vivaldi Exp $
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

import com.jmex.bui.BComponent;
import com.jmex.bui.BDialogBox;
import com.jmex.bui.BInputBox;
import com.jmex.bui.BuiSystem;
import com.jmex.bui.UserResponse;
import com.jmex.bui.base.BaseTest;
import com.jmex.bui.enumeratedConstants.TitleOptions;
import com.jmex.bui.event.DialogListener;
import com.jmex.bui.headlessWindows.BTitledWindow;
import com.jmex.bui.headlessWindows.DialogBoxUtil;
import com.jmex.bui.headlessWindows.InputBoxUtil;
import com.jmex.bui.headlessWindows.MessageWindowUtil;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author timo
 * @since 27Apr07
 */
public class AllDialogsTest extends BaseTest {
    protected void createWindows() {
        DialogListener responseListener = new DialogListener() {
            public void responseAvailable(UserResponse response, BComponent source) {
                System.out.println(response.toString());
                if (source instanceof BInputBox) {
                    System.out.println(((BInputBox) source).getInputText());
                }
            }
        };

        BDialogBox box = DialogBoxUtil.createQuestionDialogBox("qmessage1", "message");
        box.setDialogListener(responseListener);

        box = DialogBoxUtil.createWarningDialogBox("warnMessage1", "message");
        box.setDialogListener(responseListener);

        box = DialogBoxUtil.createInfoDialogBox("infoMessage1", "message");
        box.setDialogListener(responseListener);

        box = DialogBoxUtil.createErrorDialogBox("errorMessage1", "message");
        box.setDialogListener(responseListener);

        box = InputBoxUtil.createInfoInputBox("inputTest1", "Message");
        box.setDialogListener(responseListener);

        BTitledWindow window = MessageWindowUtil.createMessageBox("blah",
                                                                  "Mighty window",
                                                                  TitleOptions.MIN_MAX_CLOSE,
                                                                  "You might have noticed that this window is always on top. " +
                                                                  "That's because it uses another CollapsingWindowListener than the other windows.");
        BuiSystem.getRootNode().addWindow(window);
    }

    public static void main(String[] args) {
        Logger.getLogger("com.jmex.bui").setLevel(Level.WARNING);
        new AllDialogsTest().start();
    }
}
