package com.jmex.bui.listener;

import com.jmex.bui.BWindow;
import com.jmex.bui.BuiSystem;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.BEvent;
import com.jmex.bui.event.EventListener;

public class NavigationalController implements EventListener {
    public static final String BACK_ACTION = "close window";
    public static final String SHOW_ACTION = "show window";

    public NavigationalController(BWindow topWindow) {
        BuiSystem.getRootNode().addGlobalEventListener(this);
        BuiSystem.push(topWindow);
        showWindow(topWindow);
    }

    public void eventDispatched(BEvent event) {
        if (event instanceof ActionEvent) {
            ActionEvent actionEvent = (ActionEvent) event;
            if (BACK_ACTION.equals(actionEvent.getAction())) {
                // hide top window
                if (BuiSystem.getHistory().size() > 0) {
                    BWindow window = BuiSystem.pop();
                    if (window.isAdded()) {
                        window.dismiss();
                        // show the previous one
                        if (BuiSystem.getHistory().size() > 0) {
                            window = BuiSystem.getHistory().peek();
                            showWindow(window);
                        }
                    }
                }
            }
            // show the window
            else if (SHOW_ACTION.equals(actionEvent.getAction())) {
                BWindow window;
                if (BuiSystem.getHistory().size() > 0) {
                    window = BuiSystem.getHistory().peek();
                    if (window.isAdded()) {
                        window.dismiss();
                    }
                }
                window = (BWindow) actionEvent.getSource();
                BuiSystem.getHistory().push(window);
                showWindow(window);
            }
        }
    }

    private void showWindow(BWindow window) {
        if (!window.isAdded()) {
            BuiSystem.addWindow(window);
            window.pack();
            window.center();
        }
    }

    public void setGUIVisible(boolean visible) {
        if (BuiSystem.getHistory().size() > 0) {
            BWindow window = BuiSystem.getHistory().peek();
            if (visible) {
                showWindow(window);
            } else {
                if (window.isAdded()) {
                    window.dismiss();
                }
            }
        }
    }

    /**
     * Navigate all way back to the first window.
     */
    public void navigateToTop() {
        boolean wasAttached = false;
        if (BuiSystem.getHistory().size() > 1) {
            BWindow window = BuiSystem.getHistory().peek();
            if (window.isAdded()) {
                wasAttached = true;
                window.dismiss();
            }
        }

        while (BuiSystem.getHistory().size() > 1) {
            BuiSystem.getHistory().pop();
        }

        if (wasAttached) {
            BWindow window = BuiSystem.getHistory().peek();
            BuiSystem.addWindow(window);
            window.pack();
            window.center();
        }
    }

    public void navigateBack() {
        if (BuiSystem.getHistory().size() > 1) {
            BWindow window = BuiSystem.getHistory().pop();
            if (window.isAdded()) {
                window.dismiss();
                // show the previous window
                window = BuiSystem.getHistory().peek();
                BuiSystem.addWindow(window);
                window.pack();
                window.center();
            }
        }
    }

    public boolean isTopWindowVisible() {
        return (BuiSystem.getHistory().size() == 1);
    }
}