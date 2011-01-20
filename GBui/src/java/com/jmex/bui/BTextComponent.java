//
// $Id: BTextComponent.java,v 1.2 2007/04/27 19:46:29 vivaldi Exp $
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

import com.jme.renderer.ColorRGBA;
import com.jmex.bui.background.ComponentState;
import com.jmex.bui.enumeratedConstants.HorizontalAlignment;
import com.jmex.bui.enumeratedConstants.TextEffect;
import com.jmex.bui.enumeratedConstants.VerticalAlignment;
import com.jmex.bui.text.BTextFactory;

/**
 * Defines methods and mechanisms common to components that render a string of text.
 */
public abstract class BTextComponent extends BComponent {
    /**
     * Updates the text displayed by this component.
     *
     * @param text String
     */
    public abstract void setText(String text);

    /**
     * Returns the text currently being displayed by this component.
     *
     * @return String text
     */
    public abstract String getText();

    /**
     * Returns a text factory suitable for creating text in the style defined by the component's current state.
     *
     * @return textFact BTextFactory
     */
    public BTextFactory getTextFactory() {
        BTextFactory textfact = _textfacts[getState()];
        return (textfact != null) ? textfact : _textfacts[ComponentState.DEFAULT.ordinal()];
    }

    // TODO in following methods, I'm not sure that checking against non-null is necessary
    // Needs investigation to figure


    /**
     * Returns the horizontal alignment for this component's text.
     *
     * @return int BConstants
     */
    public HorizontalAlignment getHorizontalAlignment() {
        if (_haligns != null) {
            HorizontalAlignment halign = _haligns[getState()];
            return (halign != null) ? halign : _haligns[ComponentState.DEFAULT.ordinal()];
        }
        return HorizontalAlignment.LEFT;
    }

    /**
     * Returns the vertical alignment for this component's text.
     *
     * @return int BConstants
     */
    public VerticalAlignment getVerticalAlignment() {
        if (_valigns != null) {
            VerticalAlignment valign = _valigns[getState()];
            return (valign != null) ? valign : _valigns[ComponentState.DEFAULT.ordinal()];
        }
        return VerticalAlignment.CENTER;
    }

    /**
     * Returns the effect for this component's text.
     *
     * @return int BConstants
     */
    public TextEffect getTextEffect() {
        if (_teffects != null) {
            TextEffect teffect = _teffects[getState()];
            return (teffect != null) ? teffect : _teffects[ComponentState.DEFAULT.ordinal()];
        }
        return TextEffect.NORMAL;
    }

    /**
     * Returns the effect size for this component's text.
     *
     * @return int BConstants
     */
    public int getEffectSize() {
        if (_effsizes != null) {
            int effsize = _effsizes[getState()];
            return (effsize > 0) ? effsize : _effsizes[ComponentState.DEFAULT.ordinal()];
        }
        return BConstants.DEFAULT_SIZE;
    }

    /**
     * Returns the color to use for our text effect.
     *
     * @return ColorRGBA white
     */
    public ColorRGBA getEffectColor() {
        if (_effcols != null) {
            ColorRGBA effcol = _effcols[getState()];
            return (effcol != null) ? effcol : _effcols[ComponentState.DEFAULT.ordinal()];
        }
        return ColorRGBA.white;
    }

    /**
     * documentation inherited
     *
     * @param style BStyleSheet
     * @see BComponent#configureStyle
     */
    @Override
    protected void configureStyle(BStyleSheet style) {
        super.configureStyle(style);
        final int stateCount = getStateCount();
        HorizontalAlignment[] haligns = new HorizontalAlignment[stateCount];
        for (int ii = 0; ii < stateCount; ii++) {
            haligns[ii] = style.getTextAlignment(this, getStatePseudoClass(ii));
        }
        _haligns = checkNonDefaultHorizontalAlignment(haligns);

        VerticalAlignment[] valigns = new VerticalAlignment[stateCount];
        for (int ii = 0; ii < stateCount; ii++) {
            valigns[ii] = style.getVerticalAlignment(
                    this, getStatePseudoClass(ii));
        }
        _valigns = checkNonDefaultVerticalAlignment(valigns);

        TextEffect[] teffects = new TextEffect[stateCount];
        for (int ii = 0; ii < stateCount; ii++) {
            teffects[ii] = style.getTextEffect(this, getStatePseudoClass(ii));
        }
        _teffects = checkNonDefaultTextEffectsAlignment(teffects);

        int[] effsizes = new int[stateCount];
        for (int ii = 0; ii < stateCount; ii++) {
            effsizes[ii] = style.getEffectSize(this, getStatePseudoClass(ii));
        }
        _effsizes = checkNonDefault(effsizes, BConstants.DEFAULT_SIZE);

        boolean nondef = false;
        ColorRGBA[] effcols = new ColorRGBA[stateCount];
        for (int ii = 0; ii < stateCount; ii++) {
            effcols[ii] = style.getEffectColor(this, getStatePseudoClass(ii));
            nondef = nondef || (effcols[ii] != null);
            _textfacts[ii] =
                    style.getTextFactory(this, getStatePseudoClass(ii));
        }

        if (nondef) {
            _effcols = effcols;
        }
    }

//    /**
//     * Returns the text factory that should be used by the supplied label (for which we are by definition acting as
//     * container) to generate its text.
//     * //todo: what was this supposed to do and is it out of sync with the BUI project? -- timo 18Mar08
//     *
//     * @param forLabel Label
//     *
//     * @return textFactory BTextFactory
//     */
//    protected BTextFactory getTextFactory(Label forLabel) {
//        return getTextFactory();
//    }

    /**
     * Creates a text configuration for the supplied label (for which we are by definition acting as container).
     *
     * @param forLabel Label
     * @param twidth   int
     * @return config Label.Config
     */
    protected Label.Config getLabelConfig(Label forLabel,
                                          int twidth) {
        Label.Config config = new Label.Config();
        config.text = forLabel.getText();
        config.color = getColor();
        config.effect = getTextEffect();
        config.effectSize = getEffectSize();
        config.effectColor = getEffectColor();
        config.minwidth = config.maxwidth = twidth;
        return config;
    }

    private HorizontalAlignment[] checkNonDefaultHorizontalAlignment(HorizontalAlignment[] alignments) {
        for (HorizontalAlignment style : alignments) {
            if (style != HorizontalAlignment.LEFT) {
                return alignments;
            }
        }
        return null;
    }

    private VerticalAlignment[] checkNonDefaultVerticalAlignment(VerticalAlignment[] alignments) {
        for (VerticalAlignment style : alignments) {
            if (style != VerticalAlignment.CENTER) {
                return alignments;
            }
        }
        return null;
    }

    private TextEffect[] checkNonDefaultTextEffectsAlignment(TextEffect[] textEffects) {
        for (TextEffect style : textEffects) {
            if (style != TextEffect.NORMAL) {
                return textEffects;
            }
        }
        return null;
    }

    protected int[] checkNonDefault(int[] styles,
                                    int defval) {
        for (int style : styles) {
            if (style != -1 && style != defval) {
                return styles;
            }
        }
        return null;
    }

    protected HorizontalAlignment[] _haligns;
    protected VerticalAlignment[] _valigns;
    protected TextEffect[] _teffects;
    protected int[] _effsizes;
    protected ColorRGBA[] _effcols;
    protected BTextFactory[] _textfacts = new BTextFactory[getStateCount()];
}
