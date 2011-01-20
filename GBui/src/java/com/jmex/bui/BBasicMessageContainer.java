//
// $Id: BBasicMessageContainer.java,v 1.3 2007/05/02 21:34:01 vivaldi Exp $
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

import com.jmex.bui.layout.GroupLayout;
import com.jmex.bui.layout.Policy;
import com.jmex.bui.layout.VGroupLayout;

/**
 * Basic Message Container to provide a message and a button to click on
 *
 * @author timo
 * @since 27Apr07
 */
public class BBasicMessageContainer extends BContainer {
    /**
     * Adds a BBasicMessage to our display followed by a BButton
     *
     * @param _message BBasicMessage the message we want to display
     * @param button   BButton the button we'll click on to perform an action
     */
    public BBasicMessageContainer(BBasicMessage _message,
                                  BButton button) {
        super(new VGroupLayout(Policy.EQUALIZE));

        _message.setStyleClass("message");
        add(_message);
        add(button);
    }

    /**
     * Adds a group of text fields to our display followed by a BButton
     *
     * @param button     BButton the button we'll click on to perform an action
     * @param textfields BTextField varargs of textfields we'll use in our display
     */
    public BBasicMessageContainer(BButton button,
                                  BTextField... textfields) {
        setLayoutManager(GroupLayout.makeVStretch());
        setSize(100, 100);

        for (BTextField tf : textfields) {
            add(tf);
        }

        add(button);
    }
}
