//
// $Id: BToggleButton.java,v 1.2 2007/04/27 19:46:29 vivaldi Exp $
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

import java.util.LinkedList;
import java.util.List;

import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.BEvent;
import com.jmex.bui.event.MouseEvent;
import com.jmex.bui.event.SelectionListener;
import com.jmex.bui.event.StateChangedEvent;
import com.jmex.bui.icon.BIcon;

/**
 * Like a {@link BButton} except that it toggles between two states (selected
 * and normal) when clicked.
 */
public class BToggleButton extends BButton {
	/**
	 * Indicates that this button is in the selected state.
	 */
	public static final int SELECTED = BButton.STATE_COUNT + 0;

	/**
	 * Indicates that this button is in the selected state and is disabled.
	 */
	public static final int DISSELECTED = BButton.STATE_COUNT + 1;

	/**
	 * Creates a button with the specified textual label.
	 */
	public BToggleButton(String text) {
		super(text);
	}

	/**
	 * Creates a button with the specified label and action. The action will be
	 * dispatched via an {@link ActionEvent} when the button changes state.
	 */
	public BToggleButton(String text, String action) {
		super(text, action);
	}

	/**
	 * Creates a button with the specified icon and action. The action will be
	 * dispatched via an {@link ActionEvent} when the button changes state.
	 */
	public BToggleButton(BIcon icon, String action) {
		super(icon, action);
	}

	/**
	 * Returns whether or not this button is in the selected state.
	 */
	public boolean isSelected() {
		return _selected;
	}

	/**
	 * Configures the selected state of this button.
	 */
	public void setSelected(boolean selected) {
		if (_selected != selected) {
			_selected = selected;
			stateDidChange();
			fireSelectionChanged(selected);
		}
	}

	protected void fireSelectionChanged(boolean selected) {
		StateChangedEvent event = StateChangedEvent.create(this, selected);
		for (SelectionListener each : selectionListeners) {
			each.stateChanged(event);
		}
	}

	@Override
	public boolean dispatchEvent(BEvent event) {
		boolean selection = false;
		if (event instanceof MouseEvent) {
			MouseEvent mev = (MouseEvent) event;
			selection = _armed && _pressed
					&& mev.getType() == MouseEvent.MOUSE_RELEASED;
		}
		boolean dispatchEvent = super.dispatchEvent(event);
		if (selection) {
			fireSelectionChanged(true);
		}
		return dispatchEvent;
	}

	public void addSelectionListener(SelectionListener listener) {
		if (!selectionListeners.contains(listener)) {
			selectionListeners.add(listener);
		}
	}

	public void removeSelectionListener(SelectionListener listener) {
		selectionListeners.remove(listener);
	}

	@Override
	// documentation inherited
	public int getState() {
		int state = super.getState();
		return _selected ? (state == DISABLED ? DISSELECTED : SELECTED) : state;
	}

	@Override
	// documentation inherited
	protected int getStateCount() {
		return STATE_COUNT;
	}

	@Override
	// documentation inherited
	protected String getStatePseudoClass(int state) {
		if (state >= BButton.STATE_COUNT) {
			return STATE_PCLASSES[state - BButton.STATE_COUNT];
		} else {
			return super.getStatePseudoClass(state);
		}
	}

	@Override
	// documentation inherited
	protected void fireAction(long when, int modifiers) {
		// when the button fires its action (it was clicked) we know that it's
		// time to change state from selected to deselected or vice versa
		_selected = !_selected;
		super.fireAction(when, modifiers);
	}

	/**
	 * Used to track whether we are selected or not.
	 */
	protected boolean _selected;
	private List<SelectionListener> selectionListeners = new LinkedList<SelectionListener>();

	protected static final int STATE_COUNT = BButton.STATE_COUNT + 2;
	protected static final String[] STATE_PCLASSES = { "selected",
			"disselected" };
}
