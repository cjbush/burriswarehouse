//
// $Id: BMenu.java,v 1.2 2007/04/27 19:46:29 vivaldi Exp $
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

import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.ActionListener;
import com.jmex.bui.event.BEvent;
import com.jmex.bui.event.MouseEvent;

import com.jmex.bui.icon.BIcon;

import com.jmex.bui.util.Dimension;

/**
 * Displays a menu of items when clicked, one of which can be selected.
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
 */
public class BMenu extends BLabel {
	protected String _action;

	protected MenuPopupMenu _menu;

	/**
	 * Creates a menu with the specified textual label.
	 */
	public BMenu(String text, BWindow parentWindow) {
		this(text, "", parentWindow);
	}

	/**
	 * Creates a menu with the specified label and action. The action will be
	 * dispatched via an {@link ActionEvent} when the menu is clicked.
	 */
	public BMenu(String text, String action, BWindow parentWindow) {
		this(text, null, action, parentWindow);
	}

	/**
	 * Creates a menu with the specified label and action. The action will be
	 * dispatched via an {@link ActionEvent} to the specified
	 * {@link ActionListener} when the menu is clicked.
	 */
	public BMenu(String text, ActionListener listener, String action,
			BWindow parentWindow) {
		super(text);
		_action = action;
		if (listener != null) {
			addListener(listener);
		}
		_menu = new MenuPopupMenu(parentWindow);
	}

	/**
	 * Configures the action to be generated when this menu is clicked.
	 */
	public void setAction(String action) {
		_action = action;
	}

	/**
	 * Returns the action generated when this menu is clicked.
	 */
	public String getAction() {
		return _action;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.jmex.bui.BComponent#dispatchEvent(com.jmex.bui.event.BEvent)
	 */
	@Override
	public boolean dispatchEvent(BEvent event) {
		if (isEnabled() && event instanceof MouseEvent) {
			MouseEvent mev = (MouseEvent) event;
			switch (mev.getType()) {
			case MouseEvent.MOUSE_PRESSED:
				_menu.popup(getAbsoluteX(), getAbsoluteY(), false);
				break;

			case MouseEvent.MOUSE_RELEASED:
				break;

			default:
				return super.dispatchEvent(event);
			}

			return true;
		}

		return super.dispatchEvent(event);
	}

	/**
	 * Adds the supplied item to this menu.
	 */
	public void addMenuItem(BMenuItem item) {
		_menu.addMenuItem(item);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.jmex.bui.BLabel#getDefaultStyleClass()
	 */
	@Override
	protected String getDefaultStyleClass() {
		return "menu";
	}

	/**
	 * Called when the button is "clicked" which may due to the mouse being
	 * pressed and released while over the button or due to keyboard
	 * manipulation while the button has focus.
	 */
	protected void fireAction(long when, int modifiers) {
		emitEvent(new ActionEvent(this, when, modifiers, _action));
	}

	protected class MenuPopupMenu extends BPopupMenu {
		public MenuPopupMenu(BWindow parentWindow) {
			super(parentWindow);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see com.jmex.bui.BPopupMenu#itemSelected(com.jmex.bui.BMenuItem,
		 * long, int)
		 */
		protected void itemSelected(BMenuItem item, long when, int modifiers) {
			item.emitEvent(new ActionEvent(item, when, modifiers, item
					.getAction()));
			dismiss();
		}

		protected Dimension computePreferredSize(int whint, int hhint) {
			Dimension d = super.computePreferredSize(whint, hhint);
			d.width = Math.max(d.width, BMenu.this.getWidth()
					- getInsets().getHorizontal());
			return d;
		}
	}

	protected class MenuItem extends BMenuItem {
		public Object item;

		public MenuItem(Object item) {
			super(null, null, "select");
			if (item instanceof BIcon) {
				setIcon((BIcon) item);
			} else {
				setText(item.toString());
			}
			this.item = item;
		}
	}
}
