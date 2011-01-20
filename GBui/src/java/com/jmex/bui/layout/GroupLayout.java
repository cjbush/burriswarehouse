//
// $Id: GroupLayout.java,v 1.2 2007/04/27 19:46:32 vivaldi Exp $
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

import java.util.HashMap;
import java.util.Map;

/**
 * Group layout managers lay out widgets in horizontal or vertical groups.
 */
public abstract class GroupLayout extends BLayoutManager {
    /**
     * The group layout managers supports two constraints: fixedness and weight. A fixed component will not be stretched
     * along the major axis of the group. Those components that are stretched will have the extra space divided among
     * them according to their weight (specifically receiving the ratio of their weight to the total weight of all of
     * the free components in the container).
     * <p/>
     * <p/> If a constraints object is constructed with fixedness set to true and with a weight, the weight will be
     * ignored.
     */
    public static class Constraints {
        /**
         * Whether or not this component is fixed.
         */
        public boolean fixed = false;

        /**
         * The weight of this component relative to the other components in the container.
         */
        public int weight = 1;

        /**
         * Constructs a new constraints object with the specified fixedness and weight.
         *
         * @param fixed boolean
         */
        public Constraints(boolean fixed) {
            this.fixed = fixed;
        }

        /**
         * Constructs a new constraints object with the specified fixedness and weight.
         *
         * @param weight int
         */
        public Constraints(int weight) {
            this.weight = weight;
        }
    }

    /**
     * The default gap used by a group layout.
     */
    public static final int DEFAULT_GAP = 5;

    /**
     * A constraints object that indicates that the component should be fixed and have the default weight of one. This
     * is so commonly used that we create and make this object available here.
     */
    public final static Constraints FIXED = new Constraints(true);

    public GroupLayout setPolicy(Policy policy) {
        _policy = policy;
        return this;
    }

    public Policy getPolicy() {
        return _policy;
    }

    public GroupLayout setOffAxisPolicy(Policy offpolicy) {
        _offpolicy = offpolicy;
        return this;
    }

    public Policy getOffAxisPolicy() {
        return _offpolicy;
    }

    public GroupLayout setGap(int gap) {
        _gap = gap;
        return this;
    }

    public int getGap() {
        return _gap;
    }

    public GroupLayout setJustification(Justification justification) {
        _justification = justification;
        return this;
    }

    public Justification getJustification() {
        return _justification;
    }

    public GroupLayout setOffAxisJustification(Justification justification) {
        _offjust = justification;
        return this;
    }

    public Justification getOffAxisJustification() {
        return _offjust;
    }

    // documentation inherited from interface
    public void addLayoutComponent(BComponent comp,
                                   Object constraints) {
        if (constraints != null) {
            if (constraints instanceof Constraints) {
                if (_constraints == null) {
                    _constraints = new HashMap<BComponent, Object>();
                }

                _constraints.put(comp, constraints);
            } else {
                throw new RuntimeException(
                        "GroupLayout constraints object must be of type GroupLayout.Constraints");
            }
        }
    }

    // documentation inherited from interface
    public void removeLayoutComponent(BComponent comp) {
        if (_constraints != null) {
            _constraints.remove(comp);
        }
    }

    protected boolean isFixed(BComponent child) {
        if (_constraints == null) {
            return false;
        }

        Constraints c = (Constraints) _constraints.get(child);

        if (c != null) {
            return c.fixed;
        }

        return false;
    }

    protected int getWeight(BComponent child) {
        if (_constraints == null) {
            return 1;
        }

        Constraints c = (Constraints) _constraints.get(child);
        if (c != null) {
            return c.weight;
        }

        return 1;
    }

    /**
     * Computes dimensions of the children widgets that are useful for the group layout managers.
     *
     * @param parent BContainer
     * @param horiz  boolean
     * @param whint  int
     * @param hhint  int
     * @return DimenInfo
     */
    protected DimenInfo computeDimens(BContainer parent,
                                      boolean horiz,
                                      int whint,
                                      int hhint) {
        int count = parent.getComponentCount();
        DimenInfo info = new DimenInfo();
        info.dimens = new Dimension[count];

        // first compute the dimensions of our fixed children (to which we pass the width and
        // height hints straight through because they can theoretically take up the whole size)
        for (int ii = 0; ii < count; ii++) {
            BComponent child = parent.getComponent(ii);
            if (!child.isVisible()) {
                continue;
            }

            // we need to count all of our visible children first
            info.count++;
            if (!isFixed(child)) {
                continue;
            }

            Dimension csize = computeChildDimens(info, ii, child, whint, hhint);
            info.fixwid += csize.width;
            info.fixhei += csize.height;
            info.numfix++;
            info.dimens[ii] = csize;
        }

        // if we have no fixed components, stop here
        if (info.numfix == info.count) {
            return info;
        }

        // if we're stretching, divide up the remaining space (minus gaps) and let the free
        // children know what they're getting when we first ask them for their preferred size
        if (_policy == Policy.STRETCH) {
            if (horiz) {
                if (whint > 0) {
                    int owhint = whint;
                    whint -= (info.fixwid + _gap * (info.count - 1));
                    whint /= (info.count - info.numfix);
                }
            } else {
                if (hhint > 0) {
                    hhint -= (info.fixhei + _gap * (info.count - 1));
                    hhint /= (info.count - info.numfix);
                }
            }
        }

        for (int ii = 0; ii < count; ii++) {
            BComponent child = parent.getComponent(ii);
            if (!child.isVisible() || isFixed(child)) {
                continue;
            }

            Dimension csize = computeChildDimens(info, ii, child, whint, hhint);
            info.totweight += getWeight(child);
            if (csize.width > info.maxfreewid) {
                info.maxfreewid = csize.width;
            }
            if (csize.height > info.maxfreehei) {
                info.maxfreehei = csize.height;
            }
            info.dimens[ii] = csize;
        }

        return info;
    }

    /**
     * A helper function for {@link #computeDimens}.
     */
    protected Dimension computeChildDimens(
            DimenInfo info,
            int ii,
            BComponent child,
            int whint,
            int hhint) {
        Dimension csize = child.getPreferredSize(whint, hhint);
        info.totwid += csize.width;
        info.tothei += csize.height;
        if (csize.width > info.maxwid) {
            info.maxwid = csize.width;
        }
        if (csize.height > info.maxhei) {
            info.maxhei = csize.height;
        }
        return csize;
    }

    /**
     * Convenience method for creating a horizontal group layout manager.
     */
    public static GroupLayout makeHoriz(
            Policy policy,
            Justification justification,
            Policy offpolicy) {
        HGroupLayout lay = new HGroupLayout();
        lay.setPolicy(policy);
        lay.setJustification(justification);
        lay.setOffAxisPolicy(offpolicy);
        return lay;
    }

    /**
     * Convenience method for creating a vertical group layout manager.
     */
    public static GroupLayout makeVert(
            Policy policy,
            Justification justification,
            Policy offpolicy) {
        VGroupLayout lay = new VGroupLayout();
        lay.setPolicy(policy);
        lay.setJustification(justification);
        lay.setOffAxisPolicy(offpolicy);
        return lay;
    }

    /**
     * Convenience method for creating a horizontal group layout manager.
     */
    public static GroupLayout makeHoriz(Justification justification) {
        HGroupLayout lay = new HGroupLayout();
        lay.setJustification(justification);
        return lay;
    }

    /**
     * Convenience method for creating a vertical group layout manager.
     */
    public static GroupLayout makeVert(Justification justification) {
        VGroupLayout lay = new VGroupLayout();
        lay.setJustification(justification);
        return lay;
    }

    /**
     * Convenience method for creating a horizontal group layout manager that stretches in both directions.
     */
    public static GroupLayout makeHStretch() {
        HGroupLayout lay = new HGroupLayout();
        lay.setPolicy(Policy.STRETCH);
        lay.setOffAxisPolicy(Policy.STRETCH);
        return lay;
    }

    /**
     * Convenience method for creating a vertical group layout manager that stretches in both directions.
     */
    public static GroupLayout makeVStretch() {
        VGroupLayout lay = new VGroupLayout();
        lay.setPolicy(Policy.STRETCH);
        lay.setOffAxisPolicy(Policy.STRETCH);
        return lay;
    }

    /**
     * Makes a container configured with a horizontal group layout manager.
     */
    public static BContainer makeHBox(Justification justification) {
        HGroupLayout lay = new HGroupLayout();
        lay.setJustification(justification);
        return new BContainer(lay);
    }

    /**
     * Makes a horizontal box of components that uses the supplied (on-axis) justification.
     */
    public static BContainer makeHBox(Justification justification,
                                      BComponent... comps) {
        BContainer cont = makeHBox(justification);
        for (BComponent comp : comps) {
            cont.add(comp);
        }
        return cont;
    }

    /**
     * Creates a container configured with a vertical group layout manager.
     */
    public static BContainer makeVBox(Justification justification) {
        VGroupLayout lay = new VGroupLayout();
        lay.setJustification(justification);
        return new BContainer(lay);
    }

    /**
     * Makes a vertical box of components that uses the supplied (on-axis) justification.
     */
    public static BContainer makeVBox(Justification justification,
                                      BComponent... comps) {
        BContainer cont = makeVBox(justification);
        for (BComponent comp : comps) {
            cont.add(comp);
        }
        return cont;
    }

    protected Policy _policy = Policy.NONE;
    protected Policy _offpolicy = Policy.CONSTRAIN;
    protected int _gap = DEFAULT_GAP;
    protected Justification _justification = Justification.CENTER;
    protected Justification _offjust = Justification.CENTER;

    protected Map<BComponent, Object> _constraints;
}
