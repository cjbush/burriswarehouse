/**
 * $ID:$
 * $COPYRIGHT:$
 */
package com.jmex.bui;

import com.jmex.bui.base.BaseTest;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.ActionListener;
import com.jmex.bui.layout.GroupLayout;

/**
 * Test border of 0 around BWindow so that the white line doesn't appear
 * comment out window.setStyleClass("champion");
 * and uncomment window.setStyleClass("window");
 * and you'll see the white line
 *
 * @author torr
 * @since Mar 18, 2008 - 1:08:33 PM
 */
public class BorderlessWindowTest extends BaseTest {
    public static void main(String[] args) {
        new BorderlessWindowTest().start();
    }

    private ActionListener listener2 = new ActionListener() {
        public void actionPerformed(ActionEvent event) {
            System.out.println(bc.getSelectedItem());
            System.out.println(bc.getSelectedValue());
        }
    };

    private BComboBox bc;

    protected void createWindows() {
        BWindow window = new BWindow(BuiSystem.getStyle(), GroupLayout.makeVStretch());
        window.setStyleClass("champion");
//        window.setStyleClass("window");

        bc = new BComboBox();

        BComboBox.Item item = new BComboBox.Item("this", "this2");
        bc.addItem(item);

        BComboBox.Item item2 = new BComboBox.Item("this3", "this4");
        bc.addItem(item2);

        bc.addListener(listener2);
        window.add(bc);
        window.setSize(100, 25);

        BuiSystem.addWindow(window);
        window.center();
    }
}
