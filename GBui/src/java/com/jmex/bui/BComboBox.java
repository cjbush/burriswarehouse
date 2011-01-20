/**
 // $Id: BComboBox.java,v 1.2 2007/04/27 19:46:29 vivaldi Exp $
 // $Copyright:$
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
 */

package com.jmex.bui;

import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.BEvent;
import com.jmex.bui.event.MouseEvent;
import com.jmex.bui.icon.BIcon;
import com.jmex.bui.util.Dimension;

import java.util.ArrayList;


/**
 * Displays a selected value and allows that value to be changed by selecting from a popup menu.
 */
public class BComboBox extends BLabel {
    private LabelProvider provider;

	/**
     * Used for displaying a label that is associated with a particular non-displayable value.
     */
    @Deprecated
    public static class Item extends BComponent implements Comparable<Item> {
        public Object value;
        protected String label;

        /**
         * @param value Object
         * @param label String
         */
        public Item(final Object value,
                    final String label) {
            this.value = value;
            this.label = label;
        }

        /**
         * @return String
         */
        @Override
        public String toString() {
            return this.label;
        }

        /**
         * @param other Object
         * @return boolean
         */
        @Override
        public boolean equals(final Object other) {
            if (other instanceof Item) {
                final Item oitem = (Item) other;
                return (value == null) ? (oitem.value == null) : value.equals(oitem.value);
            } else {
                return value == null;
            }
        }

        /**
         * @param other Item
         * @return int
         */
        public int compareTo(final Item other) {
            return this.label.compareTo(other.label);
        }

    }

    /**
     * Creates an empty combo box.
     */
    public BComboBox() {
        super("");
        setFit(Fit.TRUNCATE);
    }

    /**
     * Creates a combo box with the supplied set of items. The result of {@link Object#toString} for each item will be
     * displayed in the list.
     *
     * @param items Object[]
     */
    public BComboBox(final Object[] items) {
        super("");
        setItems(items);
    }

    /**
     * Creates a combo box with the supplied set of items. The result of {@link Object#toString} for each item will be
     * displayed in the list.
     *
     * @param items Iteratable
     */
    public BComboBox(final Iterable<?> items) {
        super("");
        setItems(items);
    }

    /**
     * Appends an item to our list of items. The result of {@link Object#toString} for the item will be displayed in the
     * list.
     *
     * @param item Object
     */
    public void addItem(final Object item) {
        addItem(_items.size(), item);
    }

    /**
     * Inserts an item into our list of items at the specified position (zero being before all other items and so
     * forth).  The result of {@link Object#toString} for the item will be displayed in the list.
     *
     * @param index int
     * @param item  Object
     */
    public void addItem(final int index, final Object item) {
        _items.add(index, new ComboMenuItem(item));
        clearCache();
    }

    /**
     * Replaces any existing items in this combo box with the supplied items.
     *
     * @param items Iteratable<?>
     */
    public void setItems(final Iterable<? extends Object> items) {
        clearItems();
        for (Object item : items) {
            addItem(item);
        }
    }

    /**
     * Replaces any existing items in this combo box with the supplied items.
     *
     * @param items Object[]
     */
    public void setItems(Object[] items) {
        clearItems();
        for (Object item : items) {
            addItem(item);
        }
    }

    /**
     * Returns the index of the selected item or -1 if no item is selected.
     *
     * @return int selected index
     */
    public int getSelectedIndex() {
        return _selidx;
    }

    /**
     * Returns the selected item or null if no item is selected.
     *
     * @return Object selected index Item
     */
    @Deprecated
    public Object getSelectedItem() {
        return getItem(_selidx);
    }

    /**
     * Requires that the combo box be configured with {@link Item} items, returns the {@link Item#value} of the
     * currently selected item.
     *
     * @return Object selected index value
     */
    public Object getSelectedValue() {
        return getValue(_selidx);
    }

    /**
     * Selects the item wrapped value with the specified index.
     *
     * @param index int
     */
    @Deprecated
    public void selectItem(final int index) {
        selectItem(index, 0L, 0);
    }

    /**
     * Selects the item with the specified index. <em>Note:</em> the supplied item is compared with the item list using
     * {@link Object#equals}.
     *
     * @param item Object
     */
    @Deprecated
    public void selectItem(final Object item) {
        int selidx = -1;
        for (int ii = 0, ll = _items.size(); ii < ll; ii++) {
            ComboMenuItem mitem = _items.get(ii);
            if (mitem.item == item || 
            		(mitem != null && mitem.item.equals(item)) || 
            		(item instanceof Item && mitem.item.equals( ((Item)item).value))) {
                selidx = ii;
                break;
            }
        }
        selectItem(selidx);
    }

    /**
     * Requires that the combo box be configured with {@link Item} items, selects the item with a {@link Item#value}
     * equal to the supplied value.
     *
     * @param value Object
     */
    public void selectValue(final Object value) {
        // Item.equals only compares the values
        selectItem(new Item(value, ""));
    }

    /**
     * Returns the number of items in this combo box.
     *
     * @return int
     */
    public int getItemCount() {
        return _items.size();
    }

    /**
     * Returns the item wrapped value at the specified index.
     *
     * @param index of the Item
     * @return Object
     */
    @Deprecated
    public Object getItem(final int index) {
        return (index < 0 || index >= _items.size()) ? null : _items.get(index).item;
    }

    /**
     * Returns the value at the specified index, the item must be an instance of {@link Item}.
     *
     * @param index int
     * @return Object
     */
    public Object getValue(final int index) {
        Object value = (index < 0 || index >= _items.size()) ? null : ( _items.get(index).item);
        return (value instanceof Item)? ((Item)value).value: value;
    }

    /**
     * Removes all items from this combo box.
     */
    public void clearItems() {
        clearCache();
        _items.clear();
        _selidx = -1;
    }

    /**
     * Sets the preferred number of columns in the popup menu.
     *
     * @param columns int
     */
    public void setPreferredColumns(final int columns) {
        _columns = columns;
        if (_menu != null) {
            _menu.setPreferredColumns(columns);
        }
    }

	public void setLabelProvider(LabelProvider provider) {
		this.provider = provider;
		if (_selidx != -1) {
			ComboMenuItem menuItem = _items.get(_selidx);
			String text = getLabelProvidedText(menuItem.item);
			setText(text);
			BIcon icon = getImageLabel(menuItem.item);
			setIcon(icon);
		}
	}

    /**
     * @param event BEvent
     * @return boolean
     */
    @Override
    // from BComponent
    public boolean dispatchEvent(final BEvent event) {
        if (event instanceof MouseEvent && isEnabled()) {
            MouseEvent mev = (MouseEvent) event;
            switch (mev.getType()) {
                case MouseEvent.MOUSE_PRESSED:
                    if (_menu == null) {
                        _menu = new ComboPopupMenu(_columns);
                    }
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

    @Override
    // from BComponent
    protected String getDefaultStyleClass() {
        return "combobox";
    }

    @Override
    // from BComponent
    protected Dimension computePreferredSize(final int whint,
                                             final int hhint) {
        // our preferred size is based on the widest of our items; computing this is rather
        // expensive, so we cache it like we do the menu
        if (_psize == null) {
            _psize = new Dimension();
            Label label = new Label(this);
            for (ComboMenuItem mitem : _items) {
                if (mitem.item instanceof BIcon) {
                    label.setIcon((BIcon) mitem.item);
                } else {
                    label.setText(mitem.item == null ? "" : mitem.item.toString());
                }
                Dimension lsize = label.computePreferredSize(-1, -1);
                _psize.width = Math.max(_psize.width, lsize.width);
                _psize.height = Math.max(_psize.height, lsize.height);
            }
        }
        return new Dimension(_psize);
    }

    protected void selectItem(final int index,
                              final long when,
                              final int modifiers) {
        if (_selidx == index) {
            return;
        }

        _selidx = index;

        final Object item = getSelectedItem();
        if (item instanceof BIcon) {
            setIcon((BIcon) item);
        } else {
        	String text = getLabelProvidedText(item);
            setText(text);
            BIcon icon = getImageLabel(item);
            setIcon(icon);
        }

        emitEvent(new ActionEvent(this, when, modifiers, "selectionChanged"));
    }

    protected void clearCache() {
        if (_menu != null) {
            _menu.removeAll();
            _menu = null;
        }
        _psize = null;
    }

    protected class ComboPopupMenu extends BPopupMenu {
        public ComboPopupMenu(final int columns) {
            super(BComboBox.this.getWindow(), columns);
            for (final ComboMenuItem _item : _items) {
                addMenuItem(_item);
            }
        }

        @Override
        protected void itemSelected(final BMenuItem item,
                                    final long when,
                                    final int modifiers) {
            selectItem(_items.indexOf(item), when, modifiers);
            dismiss();
        }

        @Override
        protected Dimension computePreferredSize(final int whint,
                                                 final int hhint) {
            // prefer a size that is at least as wide as the combobox from which we will popup
            Dimension d = super.computePreferredSize(whint, hhint);
            d.width = Math.max(d.width, BComboBox.this.getWidth() - getInsets().getHorizontal());
            return d;
        }
    }

    protected class ComboMenuItem extends BMenuItem {
        public Object item;

        public ComboMenuItem(final Object item) {
            super(null, null, "select");

            if (item instanceof BIcon) {
                setIcon((BIcon) item);
            } else {
            	String text = getLabelProvidedText(item);
                setText(text);
                BIcon icon = getImageLabel(item);
                setIcon(icon);
            }

            this.item = item;
        }
    }

    private String getLabelProvidedText(Object item) {
    	if (item instanceof Item) {
    		item = ((Item)item).value;
    	}
    	String text = (item == null)? "": item.toString();
    	if (provider != null) {
    		text = provider.getText(item);
    	}
    	return text;
    }
    
    private BIcon getImageLabel(Object item) {
    	if (item instanceof Item) {
    		item = ((Item)item).value;
    	}
    	BIcon icon = null;
    	if (provider instanceof ImageLabelProvider) {
    		icon = ((ImageLabelProvider)provider).getImage(item);
    	}
    	return icon;
	}

    /**
     * The index of the currently selected item.
     */
    protected int _selidx = -1;

    /**
     * The list of items in this combo box.
     */
    protected ArrayList<ComboMenuItem> _items = new ArrayList<ComboMenuItem>();

    /**
     * A cached popup menu containing our items.
     */
    protected ComboPopupMenu _menu;

    /**
     * Our cached preferred size.
     */
    protected Dimension _psize;

    /**
     * Our preferred number of columns for the popup menu.
     */
    protected int _columns;
}
