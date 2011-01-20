//
// $Id: IntegerDocument.java,v 1.2 2007/04/27 19:46:34 vivaldi Exp $
//
// BUI - a user interface library for the JME 3D engine
// Copyright (C) 2005-2006, Michael Bayne, All Rights Reserved
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

package com.jmex.bui.text;

import com.jmex.bui.BTextField;

/**
 * A document for use with a {@link BTextField} that allows only integer numeric input. <em>Note:</em> to allow fully
 * valid values to be entered one character at a time, partially valid values (like the string '-' at position zero)
 * must be allowed. Thus one cannot rely on the integer document only ever containing valid integers.
 */
public class IntegerDocument extends Document {
    /**
     * Creates a new document that allows any integer value.
     */
    public IntegerDocument() {
        this(false);
    }

    /**
     * Creates a new integer document.
     *
     * @param positive if true, only accept positive values
     */
    public IntegerDocument(boolean positive) {
        _positive = positive;
    }

    @Override
    // documentation inherited
    protected boolean validateEdit(String oldText,
                                   String newText) {
        // some special cases
        if (newText.length() == 0) {
            return true;
        }
        if (newText.startsWith("-") && _positive) {
            return false;
        }
        if (newText.equals("-")) {
            return true;
        }
        try {
            Integer.parseInt(newText);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * If true, only positive values are allowed.
     */
    protected boolean _positive;
}
