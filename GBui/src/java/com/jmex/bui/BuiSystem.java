//
// $Id: BuiSystem.java,v 1.9 2007/05/08 22:13:48 vivaldi Exp $
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

import com.jme.input.InputHandler;
import com.jme.scene.Controller;
import com.jme.util.Timer;
import com.jmex.bui.bss.BStyleSheetUtil;
import com.jmex.bui.event.EventListener;

import java.net.URL;
import java.util.List;
import java.util.Stack;

/**
 * The global Bui handler system
 *
 * @author timo
 * @since 27Apr07
 */
public class BuiSystem {
    private BuiSystem() {}

    private static BRootNode rootNode;
    private static BStyleSheet style;
    private static Stack<BWindow> history;

    /**
     * Instantiates everything for us so we don't have to worry about a thing
     */
    public static void init() {
        init(null, null, "/rsrc/styles.bss");
    }

    /**
     * Init BuiSystem with a BRootNode, BStyleSheet and Stack that we're going to use
     *
     * @param _style BStyleSheet the stylesheet we want to use
     */
    public static void init(final BStyleSheet _style) {
        init(null, _style);
    }

    /**
     * Init BuiSystem with a BRootNode, BStyleSheet and Stack that we're going to use
     *
     * @param _rootNode BRootNode the rootNode we want to use
     * @param _style    BStyleSheet the stylesheet we want to use
     */
    public static void init(final BRootNode _rootNode,
                            final BStyleSheet _style) {
        init(_rootNode, null, _style);
    }

    /**
     * Init BuiSystem with a BRootNode, BStyleSheet and Stack that we're going to use
     *
     * @param _rootNode BRootNode the rootNode we want to set as our "global" history
     * @param _style    String the path to our stylesheet
     */
    public static void init(final BRootNode _rootNode,
                            final String _style) {
        init(_rootNode, null, _style);
    }

    /**
     * Init BuiSystem with a BRootNode, BStyleSheet and Stack that we're going to use
     *
     * @param _rootNode BRootNode the rootNode we want to set as our "global" history
     * @param _history  the history we want to set as our "global" history
     * @param stylepath String the path to our stylesheet
     */
    public static void init(final BRootNode _rootNode,
                            final Stack<BWindow> _history,
                            final String stylepath) {
        init(_rootNode, _history, BStyleSheetUtil.getStyleSheet(stylepath));
    }

    public static void init(final String... styleSheetPaths) {
        init(null, null, BStyleSheetUtil.getStyleSheet(styleSheetPaths));
    }

    public static void init(final URL... styleSheetPaths) {
        init(null, null, BStyleSheetUtil.getStyleSheet(styleSheetPaths));
    }

    public static void init(final BRootNode _rootNode,
                            final Stack<BWindow> _history,
                            final String... list) {
        init(_rootNode, _history, BStyleSheetUtil.getStyleSheet(list));
    }

    public static void init(final BRootNode _rootNode,
                            final Stack<BWindow> _history,
                            final URL... list) {
        init(_rootNode, _history, BStyleSheetUtil.getStyleSheet(list));
    }

    /**
     * Init BuiSystem with a BRootNode, BStyleSheet and Stack that we're going to use
     *
     * @param _rootNode BRootNode the rootNode we want to set as our "global" history
     * @param _history  Stack<BWindow> the history we want to set as our "global" history
     * @param _style    BStyleSheet the stylesheet we want to set as our "global" history
     */
    public static void init(final BRootNode _rootNode,
                            final Stack<BWindow> _history,
                            final BStyleSheet _style) {
        if (_rootNode != null) {
            rootNode = _rootNode;
        } else {
            rootNode = new PolledRootNode(Timer.getTimer(), new InputHandler());
        }

        if (_style != null) {
            style = _style;
        } else {
            style = BStyleSheetUtil.getStyleSheet("/rsrc/styles.bss");
        }

        if (_history != null) {
            history = _history;
        } else {
            history = new Stack<BWindow>();
        }
    }

    /**
     * Returns the BRootNode located here
     *
     * @return BRootNode the rootnode located in this class
     */
    public static BRootNode getRootNode() {
        return rootNode;
    }

    /**
     * Returns the BStyleSheet located here
     *
     * @return BStyleSheet the stylesheet located in this class
     */
    public static BStyleSheet getStyle() {
        return style;
    }

    /**
     * returns the entire Stack
     *
     * @return Stack<BWindow> this history
     */
    public static Stack<BWindow> getHistory() {
        return history;
    }

    /**
     * return a window referenced by name
     *
     * @param name String name of the window we're looking for
     * @return BWindow retrieved by name
     */
    public static BWindow getWindow(final String name) {
        List<BWindow> lst = rootNode.getAllWindows();
        for (BWindow bWindow : lst) {
            if (bWindow.getName().equals(name)) {
                return bWindow;
            }
        }

        return null;
    }

    /**
     * Pops the last BWindow off of the stack and makes it viewable dismissing the window we've passed in
     *
     * @param currentWindow BWindow the window we're done with
     */
    public static void back(final BWindow currentWindow) {
        BWindow window = pop();
        addWindow(window);
        window.center();
        currentWindow.dismiss();
    }

    /**
     * Pushes a BWindow onto the Stack for future use
     *
     * @param window BWindow window we want to push onto the Stack
     */
    public static void push(final BWindow window) {
        history.push(window);
    }

    /**
     * returns the last BWindow inserted on the Stack
     *
     * @return BWindow the last BWindow on the Stack
     */
    public static BWindow pop() {
        return history.pop();
    }

    /**
     * Adds a BWindow to the BRootNode
     *
     * @param window BWindow the window we want to add to our BRootNode
     */
    public static void addWindow(final BWindow window) {
        getRootNode().addWindow(window);
    }

    /**
     * Removes a BWindow from the BRootNode
     *
     * @param window BWindow the window we want to add to our BRootNode
     */
    public static void removeWindow(final BWindow window) {
        getRootNode().removeWindow(window);
    }

    /**
     * Adds a controller to our BRootNode
     *
     * @param controller Controller the controller we want to add to our BRootNode
     */
    public static void addController(final Controller controller) {
        getRootNode().addController(controller);
    }

    public static void addGlobalEventListener(final EventListener listener) {
        getRootNode().addGlobalEventListener(listener);
    }
}
