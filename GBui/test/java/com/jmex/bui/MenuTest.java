/**
 * $ID:$
 * $COPYRIGHT:$
 */
package com.jmex.bui;

import com.jmex.bui.base.BaseTest;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author torr
 * @since Jan 7, 2009 - 4:31:26 PM
 */
public class MenuTest extends BaseTest {

    protected void createWindows() {
        BWindow window = new BDecoratedWindow(BuiSystem.getStyle(), null);
//        BMenuBar bar = window.getMenuBar();
        BMenu fileMenu = new BMenu("File", window);
        BMenuItem newItem = new BMenuItem("New Item", "NEW");
        newItem.addListener(listener);
        fileMenu.addMenuItem(newItem);
        BMenuItem exitItem = new BMenuItem("Exit", "EXIT");
        exitItem.addListener(listener);
        fileMenu.addMenuItem(exitItem);
//        bar.add(fileMenu);

        window.setSize(400, 400);

        BuiSystem.addWindow(window);

        window.center();
    }

    public static void main(String[] args) {
        Logger.getLogger("com.jmex.bui").setLevel(Level.WARNING);
        new MenuTest().start();
    }
}
