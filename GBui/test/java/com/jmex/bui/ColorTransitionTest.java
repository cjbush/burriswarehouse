/**
 * $Id$
 * $Copyright$
 */
package com.jmex.bui;

import com.jmex.bui.base.BaseTest;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.ActionListener;
import com.jmex.bui.layout.GroupLayout;

/**
 * @author torr
 * @since Apr 24, 2009 - 1:37:43 PM
 */
public class ColorTransitionTest extends BaseTest {
    static boolean switcher = true;
    protected ActionListener listener2 = new ActionListener() {
        public void actionPerformed(final ActionEvent event) {
            if (switcher) {
                window.remove(bob);
                bob = new BButton("click");
                bob.setStyleClass("blue");
                window.add(bob);
            } else {
                window.remove(bob);
                bob = new BButton("click");
                bob.setStyleClass("green");

                window.add(bob);
            }

            switcher = !switcher;
        }
    };

    BButton bob2 = new BButton("click");
    BLabel bob = new BLabel("bob");
    BWindow window;

    protected void createWindows() {
        window = new BDecoratedWindow(BuiSystem.getStyle(), null);
        window.setLayoutManager(GroupLayout.makeVStretch());

        window.add(bob);

        bob2.addListener(listener2);
        window.add(bob2);
        window.setSize(100, 25);

        BuiSystem.addWindow(window);
        window.center();
    }

    public static void main(String[] args) {
        new ColorTransitionTest().start();
    }
}
