//
// $Id: BoundedSnappingRangeModel.java,v 1.2 2007/04/27 19:46:29 vivaldi Exp $
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

package com.jmex.bui;

/**
 * Provides a Bounded range model where values snap to a period.
 */
public class BoundedSnappingRangeModel extends BoundedRangeModel {
    /**
     * Creates a bounded range model with the specified minimum, current, extent and maximum values, and a snap period.
     */
    public BoundedSnappingRangeModel(
            int min,
            int value,
            int extent,
            int max,
            int snap) {
        super(min, value, extent, max);
        _snap = snap;
    }

    /**
     * Configures the value of this model.  The new value will be adjusted if it does not fall within the range of
     * <code>min <= value <= max - extent<code> or if value is not a modulus of <code>snap</code>.
     */
    @Override
    public void setValue(int value) {
        int val = Math.min(_max - _extent, Math.max(_min, value));
        val = val - (val % _snap);
        setRange(_min, val, _extent, _max);
    }

    @Override
    // documentation inherited
    public int getScrollIncrement() {
        return _snap;
    }

    protected int _snap;
}
