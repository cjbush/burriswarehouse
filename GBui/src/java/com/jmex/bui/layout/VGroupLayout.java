//
// $Id: VGroupLayout.java,v 1.2 2007/04/27 19:46:32 vivaldi Exp $
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
 * Handles vertically laid out groups.
 *
 * @see GroupLayout
 */
public class VGroupLayout extends GroupLayout {
    public VGroupLayout() {
    }

    public VGroupLayout(Justification justification) {
        setJustification(justification);
    }

    public VGroupLayout(Justification justification,
                        Policy policy) {
        setJustification(justification);
        setPolicy(policy);
    }

    public VGroupLayout(Policy policy) {
        setPolicy(policy);
    }

    // documentation inherited
    public Dimension computePreferredSize(BContainer target,
                                          int whint,
                                          int hhint) {
        DimenInfo info = computeDimens(target, false, whint, hhint);
        Dimension dims = new Dimension();

        if (_policy == Policy.STRETCH) {
            dims.height = info.maxfreehei * (info.count - info.numfix) + info.fixhei;
        } else if (_policy == Policy.EQUALIZE) {
            dims.height = info.maxhei * info.count;
        } else { // NONE or CONSTRAIN
            dims.height = info.tothei;
        }

        dims.height += (info.count - 1) * _gap;
        dims.width = info.maxwid;

        return dims;
    }

    // documentation inherited
    public void layoutContainer(BContainer target) {
        // adjust the bounds width and height to account for the insets
        Rectangle b = target.getBounds();
        Insets insets = target.getInsets();
        b.width -= insets.getHorizontal();
        b.height -= insets.getVertical();

        DimenInfo info = computeDimens(target, false, b.width, b.height);
        int nk = target.getComponentCount();
        int sx, sy;
        int tothei, totgap = _gap * (info.count - 1);
        int freecount = info.count - info.numfix;

        // when stretching, there is the possibility that a pixel or more will be lost to rounding
        // error. we account for that here and assign the extra space to the first free component
        int freefrac = 0;

        // do the on-axis policy calculations
        int defhei = 0;
        if (_policy == Policy.STRETCH) {
            if (freecount > 0) {
                int freehei = b.height - info.fixhei - totgap;
                defhei = freehei / freecount;
                freefrac = freehei % freecount;
                tothei = b.height;
            } else {
                tothei = info.fixhei + totgap;
            }
        } else if (_policy == Policy.EQUALIZE) {
            defhei = info.maxhei;
            tothei = info.fixhei + defhei * freecount + totgap;
        } else {
            tothei = info.tothei + totgap;
        }

        // do the off-axis policy calculations
        int defwid = 0;
        if (_offpolicy == Policy.STRETCH) {
            defwid = b.width;
        } else if (_offpolicy == Policy.EQUALIZE) {
            defwid = info.maxwid;
        }

        // do the justification-related calculations
        if (_justification == Justification.LEFT || _justification == Justification.BOTTOM) {
            sy = insets.bottom + tothei;
        } else if (_justification == Justification.CENTER) {
            sy = insets.bottom + b.height - (b.height - tothei) / 2;
        } else { // RIGHT or TOP
            sy = insets.bottom + b.height;
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
                newhei = info.dimens[i].height;
            } else {
                newhei = defhei + freefrac;
                // clear out the extra pixels the first time they're used
                freefrac = 0;
            }

            if (_offpolicy == Policy.NONE) {
                newwid = info.dimens[i].width;
            } else if (_offpolicy == Policy.CONSTRAIN) {
                newwid = Math.min(info.dimens[i].width, b.width);
            } else {
                newwid = defwid;
            }

            // determine our off-axis position
            if (_offjust == Justification.LEFT || _offjust == Justification.TOP) {
                sx = insets.left;
            } else if (_offjust == Justification.RIGHT || _offjust == Justification.BOTTOM) {
                sx = insets.left + (b.width - newwid);
            } else { // CENTER
                sx = insets.left + (b.width - newwid) / 2;
            }

            child.setBounds(sx, sy - newhei, newwid, newhei);
            sy -= (newhei + _gap);
        }
    }
}
