//
// $Id: BAbstractMessageWindow.java,v 1.5 2007/05/08 22:13:49 vivaldi Exp $
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

package com.jmex.bui.headlessWindows;

import com.jmex.bui.BContainer;
import com.jmex.bui.BStatusBar;
import com.jmex.bui.BStyleSheet;
import com.jmex.bui.BTitleBar;
import com.jmex.bui.event.ComponentListener;
import com.jmex.bui.event.MouseEvent;
import com.jmex.bui.layout.BorderLayout;
import com.jmex.bui.layout.GroupLayout;
import com.jmex.bui.listener.CollapsingWindowListener;
import com.jmex.bui.util.Dimension;
import com.jmex.bui.util.Rectangle;

/**
 * @author timo
 * @since 27Apr07
 */
public class BTitledWindow extends BDraggableWindow {
    public static final String WINDOW_MINIMIZE_ACTION = "minimize window";
    public static final String WINDOW_MAXIMIZE_ACTION = "maximize window";
    public static final String WINDOW_CLOSE_ACTION = "close window";

    /**
     * The current state of the window.
     *
     * @author Lucian Cristian Beskid
     */
    public enum WindowState {
        NORMAL,
        MAXIMIZED,
        MINIMIZED
    }

    private WindowState windowState = WindowState.NORMAL;
    private WindowState previousState = WindowState.NORMAL;
    private String maximizedStyle = "window";
    private String minimizedStyle = "window";

    // the window's size before maximizing
    private Rectangle originalBounds = new Rectangle(0, 0, 0, 0);
    private Rectangle maximizedBounds = new Rectangle(0, 0, 0, 0);
    private Dimension maximizedSize = new Dimension(-1, -1);
    private Dimension minimizedSize = new Dimension(-1, -1);

    private BTitleBar titleBar;
    private BStatusBar statusBar;
    private BContainer componentArea;

    public BTitledWindow(final String name,
                         final BTitleBar titleBar,
                         final BStatusBar statusBar,
                         final BStyleSheet style) {
        super(name, style, new BorderLayout());
        this.titleBar = titleBar;
        this.statusBar = statusBar;
        init();
    }

    @Override
    public void addListener(final ComponentListener listener) {
        if (listener instanceof CollapsingWindowListener) {
            titleBar.removeAllListeners();
            titleBar.addListener(listener);
        }
        super.addListener(listener);
    }

    private void init() {
//        setLayoutManager(new BorderLayout());
        if (titleBar != null) {
            add(titleBar, BorderLayout.NORTH);
        } else {
            throw new RuntimeException("The title bar cannot be null.");
        }

        if (statusBar != null) {
            add(statusBar, BorderLayout.SOUTH);
        }
        componentArea = new BContainer(GroupLayout.makeVStretch());
        add(componentArea, BorderLayout.CENTER);
    }

    public Dimension getMinimizedSize() {
        return minimizedSize;
    }

    public void setMinimizedSize(final Dimension _minimizedSize) {
        minimizedSize = _minimizedSize;
    }

    public void setMinimizedSize(final int width,
                                 final int height) {
        minimizedSize.width = width;
        minimizedSize.height = height;
    }

    public void setMaximizedSize(final int width,
                                 final int height) {
        maximizedSize.width = width;
        maximizedSize.height = height;
    }

    public Dimension getMaximizedSize() {
        return maximizedSize;
    }

    /**
     * The component area of a window is the part of the window underneath the title bar.
     * It is analogue to the Swing window's content pane.
     *
     * @return The container representing the window's component area.
     */
    public BContainer getComponentArea() {
        return componentArea;
    }

    public void setComponentArea(final BContainer compoenentArea) {
        remove(this.componentArea);
        this.componentArea.removeAll();
        this.componentArea = compoenentArea;
        invalidate();
    }

    public WindowState getWindowState() {
        return windowState;
    }

    public void setWindowState(final WindowState _windowState) {
        windowState = _windowState;
    }

    public void maximize(final Dimension size) {
        if (windowState != WindowState.MAXIMIZED) {
            if (size != null) {
                // save the current state
                if (windowState != WindowState.MINIMIZED) {
                    originalBounds = getBounds();
                }
                // if there are defaults, take them into account
                if (maximizedSize.width > 0 && maximizedSize.height > 0) {
                    setSize(Math.min(maximizedSize.width, size.width), Math
                            .min(maximizedSize.height, size.height));
                }
                // else just use the provided size
                else {
                    setSize(size.width, size.height);
                }
                previousState = windowState;
                windowState = WindowState.MAXIMIZED;
                componentArea.setVisible(true);
                center();
                maximizedBounds.x = _x;
                maximizedBounds.y = _y;
                maximizedBounds.width = _width;
                maximizedBounds.height = _height;
            }
            // attempt to use our own settings if any
            else if (maximizedSize.width > 0 && maximizedSize.height > 0) {
                setSize(maximizedSize.width, maximizedSize.height);
                if (windowState != WindowState.MINIMIZED) {
                    originalBounds = getBounds();
                }
                previousState = windowState;
                windowState = WindowState.MAXIMIZED;
                componentArea.setVisible(true);
                center();
                maximizedBounds.x = _x;
                maximizedBounds.y = _y;
                maximizedBounds.width = _width;
                maximizedBounds.height = _height;
            }
        }
    }

    public void minimize() {
        if (windowState != WindowState.MINIMIZED) {
            // save the bounds if necessary
            if (windowState == WindowState.NORMAL) {
                originalBounds = getBounds();
            } else if (windowState == WindowState.MAXIMIZED) {
                maximizedBounds = getBounds();
            }
            // adjust the new y position
            setLocation(_x, _y + _height - titleBar.getHeight());
            componentArea.setVisible(false);
            setSize(getWidth(), titleBar.getHeight());
            previousState = windowState;
            windowState = WindowState.MINIMIZED;
        }
    }

    /**
     * Restores the size of the BTitledWindow if it is maximized or minimized.
     */
    public void restoreSize() {
        // restore from maximized to normal
        if (windowState == WindowState.MAXIMIZED) {
            setSize(originalBounds.width, originalBounds.height);
            setLocation(originalBounds.x, originalBounds.y);
            componentArea.setVisible(true);
            previousState = windowState;
            windowState = WindowState.NORMAL;
        }
        // restore from minimized to previous
        else if (windowState == WindowState.MINIMIZED) {
            if (previousState == WindowState.NORMAL) {
                setSize(originalBounds.width, originalBounds.height);
                setLocation(originalBounds.x, originalBounds.y);
                previousState = windowState;
                windowState = WindowState.NORMAL;
                componentArea.setVisible(true);
            } else if (previousState == WindowState.MAXIMIZED) {
                setSize(maximizedBounds.width, maximizedBounds.height);
                setLocation(maximizedBounds.x, maximizedBounds.y);
                previousState = windowState;
                windowState = WindowState.MAXIMIZED;
                componentArea.setVisible(true);
            }
        }
    }

    @Override
    protected void windowReleased(final MouseEvent event) {
        // we'll need to update the saved bounds if the window
        // was dragged in a minimized state
        if (windowState == WindowState.MINIMIZED) {
            originalBounds.x = _x - originalBounds.width + titleBar.getWidth();
            originalBounds.y = _y - originalBounds.height + titleBar.getHeight();
        }
    }

    public String getMaximizedStyleClass() {
        return maximizedStyle;
    }

    public void setMaximizedStyleClass(final String maximizedStyleClass) {
        this.maximizedStyle = maximizedStyleClass;
    }

    public String getMinimizedStyleClass() {
        return minimizedStyle;
    }

    public void setMinimizedStyleClass(final String minimizedStyleClass) {
        this.minimizedStyle = minimizedStyleClass;
    }
}
