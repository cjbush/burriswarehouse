//
// $Id: InputWindow.java,v 1.6 2007/05/02 21:34:05 vivaldi Exp $
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

import com.jmex.bui.BDialogMessage;
import com.jmex.bui.BInputBox;
import com.jmex.bui.BLabel;
import com.jmex.bui.BTitleBar;
import com.jmex.bui.BuiSystem;
import com.jmex.bui.enumeratedConstants.DialogOptions;
import com.jmex.bui.enumeratedConstants.DisplayStyleOptions;
import com.jmex.bui.enumeratedConstants.IconOptions;
import com.jmex.bui.enumeratedConstants.TitleOptions;
import com.jmex.bui.event.ActionListener;
import com.jmex.bui.listener.CollapsingWindowListener;

/**
 * @author timo
 * @since 27Apr07
 */
// TODO this utility class is a mess: simplify or remove
public final class InputBoxUtil {
    private static final CollapsingWindowListener LISTENER = new CollapsingWindowListener();

    private InputBoxUtil() {}

    public static BInputBox createInfoInputBox(final String _name,
                                               final String message,
                                               final ActionListener listener) {
        BInputBox db = createInfoInputBox(_name, message);
        db.addListener(listener);
        return finishWindow(db);
    }

    public static BInputBox createQuestionInputBox(final String _name,
                                                   final String message,
                                                   final ActionListener listener) {
        BInputBox db = createQuestionInputBox(_name, message);
        db.addListener(listener);
        return finishWindow(db);
    }

    public static BInputBox createWarningInputBox(final String _name,
                                                  final String message,
                                                  final ActionListener listener) {
        BInputBox db = createWarningInputBox(_name, message);
        db.addListener(listener);
        return finishWindow(db);
    }

    public static BInputBox createErrorInputBox(final String _name,
                                                final String message,
                                                final ActionListener listener) {
        BInputBox db = createErrorInputBox(_name, message);
        db.addListener(listener);
        return finishWindow(db);
    }

    private static BInputBox finishWindow(final BInputBox db) {
        db.addListener(LISTENER);
        db.setSize(400, 200);
        BuiSystem.getRootNode().addWindow(db);
        db.center();
        return db;
    }

    public static BInputBox createInputBox(final String _name,
                                           final String title,
                                           final String titleClass,
                                           final String message,
                                           final DialogOptions options,
                                           final IconOptions iconOptions,
                                           final ActionListener listener) {
        BInputBox db = createInputBox(_name, title, titleClass, message, options, iconOptions);
        db.addListener(listener);
        return finishWindow(db);
    }

    public static BInputBox createInputBox(final String _name,
                                           final String title,
                                           final String titleClass,
                                           final String message,
                                           final DialogOptions options,
                                           final IconOptions iconOptions) {
        BTitleBar tb = new BTitleBar(_name, new BLabel(title, titleClass), TitleOptions.CLOSE);
        BDialogMessage dlgMessage = new BDialogMessage(_name, message, iconOptions, DisplayStyleOptions.MOTIF);

        BInputBox box = new BInputBox("input box", tb, dlgMessage, options, BuiSystem.getStyle());
        return finishWindow(box);
    }

    public static BInputBox createBasicInputBox(final String _name,
                                                final String message) {
        return createInfoInputBox(_name, message);
    }

    public static BInputBox createInfoInputBox(final String _name,
                                               String message) {
        return createInputBox(_name, "Input Box", "titlemessage", message, DialogOptions.OK, IconOptions.INFO);
    }

    public static BInputBox createErrorInputBox(final String _name,
                                                String message) {
        return createInputBox(_name, "Input Box", "titlemessage", message, DialogOptions.OK, IconOptions.ERROR);
    }

    public static BInputBox createWarningInputBox(final String _name,
                                                  String message) {
        return createInputBox(_name, "Input Box", "titlemessage", message, DialogOptions.OK, IconOptions.WARNING);
    }

    public static BInputBox createQuestionInputBox(final String _name,
                                                   String message) {
        return createInputBox(_name, "Input Box", "titlemessage", message, DialogOptions.OK, IconOptions.QUESTION);
    }
}
