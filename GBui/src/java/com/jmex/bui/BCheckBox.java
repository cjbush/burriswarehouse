//
// $Id: BCheckBox.java,v 1.2 2007/04/27 19:46:29 vivaldi Exp $
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

import com.jmex.bui.icon.BIcon;

/**
 * Displays a label with a check-box button next to it.
 */
public class BCheckBox extends BToggleButton {
    public BCheckBox(String label) {
        super(label);
    }

    @Override
    // documentation inherited
    protected String getDefaultStyleClass() {
        return "checkbox";
    }

    @Override
    // documentation inherited
    protected void configureStyle(BStyleSheet style) {
        super.configureStyle(style);

        for (int ii = 0; ii < getStateCount(); ii++) {
            _icons[ii] = style.getIcon(this, getStatePseudoClass(ii));
        }
        _label.setIcon(_icons[getState()]);
    }

    @Override
    // documentation inherited
    protected void stateDidChange() {
        super.stateDidChange();

        // configure our checkbox icon
        _label.setIcon(_icons[getState()]);
    }

    protected BIcon[] _icons = new BIcon[getStateCount()];
}
