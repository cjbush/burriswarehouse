//
// $Id: BMenuBar.java,v 1.2 2007/04/27 19:46:29 vivaldi Exp $
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
import com.jmex.bui.layout.HGroupLayout;
import com.jmex.bui.layout.Justification;

/**
 * Creates a simple MenuBar to be displayed along the top of a BWindow.
 *
 * <br>
 * Example: <br>
 * <code><b>
 * BWindow window = new BDecoratedWindow(style, null); <br>
 * BMenuBar bar = window.getMenuBar(); <br>
 * BMenu fileMenu = new BMenu("File", window); <br>
 * BMenuItem newItem = new BMenuItem("New Item", "NEW"); <br>
 * newItem.addListener(this); <br>
 * fileMenu.addMenuItem(newItem); <br>
 * BMenuItem exitItem = new BMenuItem("Exit", "EXIT"); <br>
 * exitItem.addListener(this); <br>
 * fileMenu.addMenuItem(exitItem); <br>
 * bar.add(fileMenu);
 * </b></code>
 *
 * <br>
 * <br>
 * Styles: <br>
 * <code><b>
 * menu { <br>
 * 	border: 1 solid #999999; <br>
 * 	background: solid #66666688; <br>
 * } <br><br>
 * menu:hover { <br>
 * 	border: 1 solid #336699; <br>
 * } <br><br>
 * menubar { <br>
 * 	background: solid #66666688; <br>
 * } <br><br>
 * menuitem { <br>
 * 	border: 1 solid #999999; <br>
 * 	background: solid #66666688; <br>
 * } <br><br>
 * menuitem:hover { <br>
 * 	border: 1 solid #336699; <br>
 * } <br>
 * </b></code>
 *
 * @author Devin Gillman
 * @version 0.1
 *
 */
public class BMenuBar extends BContainer {
	public BMenuBar(String name) {
		super(name, new HGroupLayout(Justification.LEFT));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.jmex.bui.BContainer#getDefaultStyleClass()
	 */
	@Override
	protected String getDefaultStyleClass() {
		return "menubar";
	}
}
