//
// $Id: DialogWindow.java,v 1.6 2007/05/02 21:34:05 vivaldi Exp $
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

import com.jmex.bui.BDialogBox;
import com.jmex.bui.BDialogMessage;
import com.jmex.bui.BLabel;
import com.jmex.bui.BTitleBar;
import com.jmex.bui.BuiSystem;
import com.jmex.bui.enumeratedConstants.DialogOptions;
import com.jmex.bui.enumeratedConstants.DisplayStyleOptions;
import com.jmex.bui.enumeratedConstants.IconOptions;
import com.jmex.bui.enumeratedConstants.TitleOptions;
import com.jmex.bui.listener.CollapsingWindowListener;

/**
 * @author timo
 * @since 27Apr07
 */
//TODO this utility class is a mess: simplify or remove
public final class DialogBoxUtil {
    private static final CollapsingWindowListener LISTENER = new CollapsingWindowListener();

    private DialogBoxUtil() {}

    private static BDialogBox finishWindow(BDialogBox dialog) {
        dialog.addListener(LISTENER);
        dialog.setSize(400, 200);
        BuiSystem.getRootNode().addWindow(dialog);
        dialog.center();

        return dialog;
    }

    public static BDialogBox createDialogBox(final String _name,
                                             final String title,
                                             final String titleClass,
                                             final String message,
                                             final DialogOptions options,
                                             final IconOptions iconOptions,
                                             final DisplayStyleOptions style) {
        BTitleBar tb = new BTitleBar(_name, new BLabel(title, titleClass), TitleOptions.CLOSE);
        BDialogMessage _message = new BDialogMessage(_name, message, iconOptions, style);
        BDialogBox box = new BDialogBox(_name, tb, _message, options, BuiSystem.getStyle());
        return finishWindow(box);
    }

    public static BDialogBox createBasicDialogBox(final String _name,
                                                  final String message) {
        return createInfoDialogBox(_name, message, DisplayStyleOptions.MOTIF);
    }

    public static BDialogBox createInfoDialogBox(final String _name,
                                                 final String message) {
        return createDialogBox(_name,
                               "Dialog Box",
                               "titlemessage",
                               message,
                               DialogOptions.OK,
                               IconOptions.INFO,
                               DisplayStyleOptions.MOTIF);
    }

    public static BDialogBox createErrorDialogBox(final String _name,
                                                  final String message) {
        return createDialogBox(_name,
                               "Dialog Box",
                               "titlemessage",
                               message,
                               DialogOptions.OK,
                               IconOptions.ERROR,
                               DisplayStyleOptions.MOTIF);
    }

    public static BDialogBox createWarningDialogBox(final String _name,
                                                    final String message) {
        return createDialogBox(_name,
                               "Dialog Box",
                               "titlemessage",
                               message,
                               DialogOptions.OK,
                               IconOptions.WARNING,
                               DisplayStyleOptions.MOTIF);
    }

    public static BDialogBox createQuestionDialogBox(final String _name,
                                                     final String message) {
        return createDialogBox(_name,
                               "Dialog Box",
                               "titlemessage",
                               message,
                               DialogOptions.OK,
                               IconOptions.QUESTION,
                               DisplayStyleOptions.MOTIF);
    }


    public static BDialogBox createInfoDialogBox(final String _name,
                                                 final String message,
                                                 final DisplayStyleOptions style) {
        return createDialogBox(_name, "Dialog Box", "titlemessage", message, DialogOptions.OK, IconOptions.INFO, style);
    }

    public static BDialogBox createErrorDialogBox(final String _name,
                                                  final String message,
                                                  final DisplayStyleOptions style) {
        return createDialogBox(_name,
                               "Dialog Box",
                               "titlemessage",
                               message,
                               DialogOptions.OK,
                               IconOptions.ERROR,
                               style);
    }

    public static BDialogBox createWarningDialogBox(final String _name,
                                                    final String message,
                                                    final DisplayStyleOptions style) {
        return createDialogBox(_name,
                               "Dialog Box",
                               "titlemessage",
                               message,
                               DialogOptions.OK,
                               IconOptions.WARNING,
                               style);
    }

    public static BDialogBox createQuestionDialogBox(final String _name,
                                                     final String message,
                                                     final DisplayStyleOptions style) {
        return createDialogBox(_name,
                               "Dialog Box",
                               "titlemessage",
                               message,
                               DialogOptions.OK,
                               IconOptions.QUESTION,
                               style);
    }
}
