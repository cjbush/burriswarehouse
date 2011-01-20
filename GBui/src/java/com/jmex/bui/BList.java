//
// $Id: BList.java,v 1.2 2007/04/27 19:46:29 vivaldi Exp $
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.jmex.bui.dragndrop.BDragSourceListener;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.ActionListener;
import com.jmex.bui.event.SelectionListener;
import com.jmex.bui.event.StateChangedEvent;
import com.jmex.bui.event.StateChangedEvent.SelectionState;
import com.jmex.bui.icon.BIcon;
import com.jmex.bui.layout.GroupLayout;
import com.jmex.bui.layout.Justification;
import com.jmex.bui.layout.Policy;

/**
 * Displays a list of entries that can be selected and fires a {@link StateChangedEvent} when the selected value
 * changes. Each entry by default is displayed as a string obtained by calling {@link Object#toString} on the
 * supplied values.  An alternative text can be displayed by providing a {@link LabelProvider}.
 */
public class BList extends BContainer {
    /**
     * The action fired when the list selection changes.
     */
    public static final String SELECT = "select";
    
    /**
     * The values contained in the list.
     */
    protected List<Object> values = new ArrayList<Object>();
    protected List<SelectionListener> selectionListeners = new LinkedList<SelectionListener>();
    
    /**
     * The index of the current selection (or -1 for none).
     */
    protected int _selidx = -1;
    private LabelProvider labelProvider;

    /**
     * Listens for button selections.
     */
    protected ActionListener _slistener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (_selidx != -1) {
                ((BToggleButton) _children.get(_selidx)).setSelected(false);
            }
            _selidx = _children.indexOf(e.getSource());
            emitEvent(new ActionEvent(BList.this, e.getWhen(),
                                      e.getModifiers(), SELECT));
        }
    };
    protected SelectionListener selectionListener = new SelectionListener() {
		@Override
		public void stateChanged(StateChangedEvent e) {
			StateChangedEvent event = StateChangedEvent.create(BList.this, e.getType() == SelectionState.Selected);
			for (SelectionListener each : selectionListeners) {
				each.stateChanged(event);
			}
		}
    };
    /**
     * Creates an empty list.
     */
    public BList() {
        this(null);
    }

    /**
     * Creates a list and populates it with the supplied values.
     */
    public BList(Object[] values) {
        super(GroupLayout.makeVert(
                Policy.NONE,
                Justification.TOP,
                Policy.STRETCH));
        if (values != null) {
            for (int ii = 0; ii < values.length; ii++) {
                addValue(values[ii]);
            }
        }
    }
    
    @Override
    public void add(BComponent child) {
    	throw new IllegalStateException("BList does not support adding of components");
    }

    /**
     * Adds a value to the list.
     */
    public void addValue(Object value) {
        // list entries can be selected by clicking on them, but unselected
        // only by clicking another entry
    	String text = value.toString();
    	BIcon icon = null;
    	if (labelProvider != null) {
    		text = labelProvider.getText(value);
    	}
    	if (labelProvider instanceof ImageLabelProvider) {
    		ImageLabelProvider imageLabelProvider = (ImageLabelProvider)labelProvider;
			icon = imageLabelProvider.getImage(value);
    	}
		BToggleButton button = new BToggleButton(text) {
            protected void fireAction(long when, int modifiers) {
                if (!_selected) {
                    super.fireAction(when, modifiers);
                }
            }
        };
        button.setStyleClass("list_entry");
        button.addListener(_slistener);
        button.addSelectionListener(selectionListener);
        button.setIcon(icon);
        super.add(button);
        values.add(value);
    }
	
    /**
     * Adds the list of values after the current values contained in the list
     * @param toAdd all values to be added in order
     */
    public void addAllValues(Collection<? extends Object> toAdd) {
		for (Object each : toAdd) {
			addValue(each);
		}
	}

    /**
     * Returns the value at the given index
     * @param i
     * @return
     */
	public Object getValue(int i) {
		return values.get(i);
	}
	
    @Override
    public void remove(BComponent child) {
    	int index = _children.indexOf(child);
    	if (index != -1) {
    		values.remove(index);
    		if (index == _selidx) {
    			_selidx = -1;
    		}
    	}
    	super.remove(child);
    }
    
    @Override
    public void remove(int index) {
    	BComponent component = _children.get(index);
    	remove(component);
    }
    
    /**
     * Removes a value from the list, if it is present.
     *
     * @return true if the value was removed, false if it was not in the list
     */
    public boolean removeValue(Object value) {
        int idx = values.indexOf(value);
        if (idx == -1) {
            return false;
        }
        if (idx == _selidx) {
            _selidx = -1;
        }
        BComponent child = _children.get(idx); 
        // memory leak, remember to clean up
        if (child instanceof BToggleButton) {
        	BToggleButton button = (BToggleButton)child; 
        	button.removeAllListeners();
        	button.removeSelectionListener(selectionListener);
        }
		remove(child);
        values.remove(idx);
        return true;
    }

    /**
     * Returns the currently selected value.
     *
     * @return the selected value, or <code>null</code> for none
     */
    public Object getSelectedValue() {
        return (_selidx == -1) ? null : values.get(_selidx);
    }

    /**
     * Sets the selected value.
     *
     * @param value the value to select, or <code>null</code> for none
     */
    public void setSelectedValue(Object value) {
        int idx = (value == null) ? -1 : values.indexOf(value);
        if (idx == _selidx) {
            return;
        }
        if (_selidx != -1) {
        	((BToggleButton) _children.get(_selidx)).setSelected(false);
        }
        if (idx != -1) {
            ((BToggleButton) _children.get(idx)).setSelected(true);
        }
        _selidx = idx;
    }

    /*
     * (non-Javadoc)
     * @see com.jmex.bui.BContainer#getDefaultStyleClass()
     */
    @Override
    protected String getDefaultStyleClass() {
        return "list";
    }
    
	public void addSelectionListener(SelectionListener listener) {
		if (!selectionListeners.contains(listener)) {
			selectionListeners.add(listener);
		}
	}
	
	public void removeSelectionListener(SelectionListener listener) {
		selectionListeners.remove(listener);
	}

	public void setLabelProvider(LabelProvider labelProvider) {
		for (Object each : values) {
			int index = values.indexOf(each);
			BToggleButton toggleButton = (BToggleButton)_children.get(index);
			String text = labelProvider.getText(each);
			if (text == null) {
				text = each.toString();
			}
			toggleButton.setText(text);
			if (labelProvider instanceof ImageLabelProvider) {
				ImageLabelProvider provider = (ImageLabelProvider) labelProvider;
				BIcon image = provider.getImage(each);
				toggleButton.setIcon(image);
			}
		}
		this.labelProvider = labelProvider;
	}

	public void addDragListener(BDragSourceListener dragSourceListener) {
		
	}
}
