//
// $Id: BPasswordField.java,v 1.2 2007/04/27 19:46:29 vivaldi Exp $
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

/**
 * A derivation of {@link BTextField} that does not display the actual text, but asterisks instead.
 */
public class BPasswordField extends BTextField {
    public BPasswordField() {
    }

    public BPasswordField(int maxLength) {
        super(maxLength);
    }

    public BPasswordField(String text) {
        super(text);
    }

    public BPasswordField(String text,
                          int maxLength) {
        super(text, maxLength);
    }

    @Override
    // documentation inherited
    protected String getDisplayText() {
        String text = super.getDisplayText();
        if (text == null) {
            return null;
        } else if (_stars == null || _stars.length() != text.length()) {
            StringBuffer stars = new StringBuffer();
            for (int ii = 0; ii < text.length(); ii++) {
                stars.append("*");
            }
            _stars = stars.toString();
        }
        return _stars;
    }

    protected String _stars;
}
