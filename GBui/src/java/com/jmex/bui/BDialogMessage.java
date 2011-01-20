//
// $Id: BDialogMessage.java,v 1.6 2007/05/02 21:34:01 vivaldi Exp $
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

import com.jmex.bui.enumeratedConstants.DisplayStyleOptions;
import com.jmex.bui.enumeratedConstants.IconOptions;
import com.jmex.bui.icon.BIcon;
import com.jmex.bui.icon.IconUtil;
import com.jmex.bui.layout.BorderLayout;
import com.jmex.bui.layout.GroupLayout;
import com.jmex.bui.layout.Justification;

//todo: change so that the style is set in the BuiSystem? and then can be propogated through
//      the subset of the window

//      otherwise parts might be one way, and other parts the other way
/**
 * @author timo
 * @since 27Apr07
 */
public class BDialogMessage extends BContainer {
    private static final BIcon INFO_ICON_MOTIF =
            IconUtil.getIcon(BDialogMessage.class.getResource("/rsrc/dialogLogos/metal-info.png"));
    private static final BIcon INFO_ICON_WINDOWS =
            IconUtil.getIcon(BDialogMessage.class.getResource("/rsrc/dialogLogos/metal-info.png"));
    private static final BIcon ERROR_ICON_MOTIF =
            IconUtil.getIcon(BDialogMessage.class.getResource("/rsrc/dialogLogos/metal-error.png"));
    private static final BIcon ERROR_ICON_WINDOWS =
            IconUtil.getIcon(BDialogMessage.class.getResource("/rsrc/dialogLogos/metal-error.png"));
    private static final BIcon WARN_ICON_MOTIF =
            IconUtil.getIcon(BDialogMessage.class.getResource("/rsrc/dialogLogos/metal-warning.png"));
    private static final BIcon WARN_ICON_WINDOWS =
            IconUtil.getIcon(BDialogMessage.class.getResource("/rsrc/dialogLogos/metal-warning.png"));
    private static final BIcon QUESTION_ICON_MOTIF =
            IconUtil.getIcon(BDialogMessage.class.getResource("/rsrc/dialogLogos/metal-question.png"));
    private static final BIcon QUESTION_ICON_WINDOWS =
            IconUtil.getIcon(BDialogMessage.class.getResource("/rsrc/dialogLogos/metal-question.png"));

    public BDialogMessage(final String _name,
                          final String message,
                          final IconOptions option,
                          final DisplayStyleOptions style) {
        this(_name, new BLabel(message), option, style);
    }

    public BDialogMessage(final String _name,
                          final String message,
                          final String messageStyle,
                          final IconOptions option,
                          final DisplayStyleOptions style) {
        this(_name, new BLabel(message, messageStyle), option, style);
    }

    public BDialogMessage(final String _name,
                          final String message,
                          final String messageStyle,
                          final String backgroundStyle,
                          final IconOptions option,
                          final DisplayStyleOptions style) {
        this(_name, new BLabel(message, messageStyle), backgroundStyle, option, style);
    }

    public BDialogMessage(final String _name,
                          final BLabel message,
                          final IconOptions option,
                          final DisplayStyleOptions style) {
        this(_name, message, "greymessagebg", option, style);
    }

    public BDialogMessage(final String _name,
                          final BLabel message,
                          final String backgroundStyle,
                          final IconOptions option,
                          final DisplayStyleOptions style) {
        super(_name, new BorderLayout());

        createMessage(_name, message, backgroundStyle, option, style);
    }

    private BLabel createIcon(IconOptions options, DisplayStyleOptions display) {
        BLabel iconLabel = new BLabel("");
        switch (options) {
            case WARNING:
                if (display == DisplayStyleOptions.WINDOWS) {
                    iconLabel.setIcon(WARN_ICON_WINDOWS);
                } else {
                    iconLabel.setIcon(WARN_ICON_MOTIF);
                }
                break;
            case ERROR:
                if (display == DisplayStyleOptions.WINDOWS) {
                    iconLabel.setIcon(ERROR_ICON_WINDOWS);
                } else {
                    iconLabel.setIcon(ERROR_ICON_MOTIF);
                }
                break;
            case QUESTION:
                if (display == DisplayStyleOptions.WINDOWS) {
                    iconLabel.setIcon(QUESTION_ICON_WINDOWS);
                } else {
                    iconLabel.setIcon(QUESTION_ICON_MOTIF);
                }
                break;
            case INFO:
            default:
                if (display == DisplayStyleOptions.WINDOWS) {
                    iconLabel.setIcon(INFO_ICON_WINDOWS);
                } else {
                    iconLabel.setIcon(INFO_ICON_MOTIF);
                }
                break;
        }
        return iconLabel;
    }

    private void createMessage(String _name,
                               BLabel message,
                               String backgroundStyle,
                               IconOptions option,
                               DisplayStyleOptions styleOption) {
        BContainer bc = GroupLayout.makeVBox(Justification.CENTER);
        bc.setName(_name);

        bc.add(message);
        if (styleOption == null) {
            styleOption = DisplayStyleOptions.MOTIF;
        }

        if (option == null) {
            option = IconOptions.INFO;
        }
        add(createIcon(option, styleOption), BorderLayout.WEST);

        add(bc, BorderLayout.CENTER);
        setStyleClass(backgroundStyle);
    }
}