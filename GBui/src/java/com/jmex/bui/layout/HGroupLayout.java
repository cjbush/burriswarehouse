//
// $Id: HGroupLayout.java,v 1.2 2007/04/27 19:46:32 vivaldi Exp $
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

package com.jmex.bui.layout;

import com.jmex.bui.BComponent;
import com.jmex.bui.BContainer;
import com.jmex.bui.util.Dimension;
import com.jmex.bui.util.Insets;
import com.jmex.bui.util.Rectangle;

/**
 * Handles horizontally laid out groups.
 *
 * @see GroupLayout
 */
public class HGroupLayout extends GroupLayout {

    public HGroupLayout() {
    }

    public HGroupLayout(Justification justification) {
        setJustification(justification);
    }

    public HGroupLayout(Justification justification,
                        Policy policy) {
        setJustification(justification);
        setPolicy(policy);
    }

    public HGroupLayout(Policy policy) {
        setPolicy(policy);
    }

    // documentation inherited
    public Dimension computePreferredSize(BContainer target,
                                          int whint,
                                          int hhint) {
        DimenInfo info = computeDimens(target, true, whint, hhint);
        Dimension dims = new Dimension();

        if (_policy == Policy.STRETCH) {
            dims.width = info.maxfreewid * (info.count - info.numfix) + info.fixwid;
        } else if (_policy == Policy.EQUALIZE) {
            dims.width = info.maxwid * info.count;
        } else { // NONE or CONSTRAIN
            dims.width = info.totwid;
        }

        dims.width += (info.count - 1) * _gap;
        dims.height = info.maxhei;

        return dims;
    }

    // documentation inherited
    public void layoutContainer(BContainer target) {
        // adjust the bounds width and height to account for the insets
        Rectangle b = target.getBounds();
        Insets insets = target.getInsets();
        b.width -= insets.getHorizontal();
        b.height -= insets.getVertical();

        DimenInfo info = computeDimens(target, true, b.width, b.height);
        int nk = target.getComponentCount();
        int sx, sy;
        int totwid, totgap = _gap * (info.count - 1);
        int freecount = info.count - info.numfix;

        // when stretching, there is the possibility that a pixel or more will be lost to rounding
        // error. we account for that here and assign the extra space to the first free component
        int freefrac = 0;

        // do the on-axis policy calculations
        int defwid = 0;
        float conscale = 1f;
        if (_policy == Policy.STRETCH) {
            if (freecount > 0) {
                int freewid = b.width - info.fixwid - totgap;
                defwid = freewid / freecount;
                freefrac = freewid % freecount;
                totwid = b.width;
            } else {
                totwid = info.fixwid + totgap;
            }
        } else if (_policy == Policy.EQUALIZE) {
            defwid = info.maxwid;
            totwid = info.fixwid + defwid * freecount + totgap;
        } else if (_policy == Policy.CONSTRAIN) {
            totwid = info.totwid + totgap;
            // if we exceed the width available, we must constrain
            if (totwid > b.width) {
                conscale = (b.width - totgap) / (float) info.totwid;
                totwid = b.width;
            }
        } else { // NONE
            totwid = info.totwid + totgap;
        }

        // do the off-axis policy calculations
        int defhei = 0;
        if (_offpolicy == Policy.STRETCH) {
            defhei = b.height;
        } else if (_offpolicy == Policy.EQUALIZE) {
            defhei = info.maxhei;
        }

        // do the justification-related calculations
        if (_justification == Justification.LEFT || _justification == Justification.TOP) {
            sx = insets.left;
        } else if (_justification == Justification.CENTER) {
            sx = insets.left + (b.width - totwid) / 2;
        } else { // RIGHT or BOTTOM
            sx = insets.left + b.width - totwid;
        }

        // do the layout
        for (int i = 0; i < nk; i++) {
            // skip non-visible kids
            if (info.dimens[i] == null) {
                continue;
            }

            BComponent child = target.getComponent(i);
            int newwid, newhei;

            if (_policy == Policy.NONE || isFixed(child)) {
                newwid = info.dimens[i].width;
            } else if (_policy == Policy.CONSTRAIN) {
                newwid = Math.max(1, (int) (conscale * info.dimens[i].width));
            } else {
                newwid = defwid + freefrac;
                // clear out the extra pixels the first time they're used
                freefrac = 0;
            }

            if (_offpolicy == Policy.NONE) {
                newhei = info.dimens[i].height;
            } else if (_offpolicy == Policy.CONSTRAIN) {
                newhei = Math.min(info.dimens[i].height, b.height);
            } else {
                newhei = defhei;
            }

            // determine our off-axis position
            if (_offjust == Justification.RIGHT || _offjust == Justification.TOP) {
                sy = insets.bottom + b.height - newhei;
            } else if (_offjust == Justification.LEFT || _offjust == Justification.BOTTOM) {
                sy = insets.bottom;
            } else { // CENTER
                sy = insets.bottom + (b.height - newhei) / 2;
            }

            child.setBounds(sx, sy, newwid, newhei);
            sx += child.getWidth() + _gap;
        }
    }
}
