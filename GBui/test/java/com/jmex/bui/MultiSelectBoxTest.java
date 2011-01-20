package com.jmex.bui;

import com.jmex.bui.BMultiSelectBox.SelectionMode;
import com.jmex.bui.base.BaseTest;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.ActionListener;
import com.jmex.bui.layout.GroupLayout;
import com.jmex.bui.util.Dimension;

public class MultiSelectBoxTest extends BaseTest {
    ActionListener listener2 = new ActionListener() {
        public void actionPerformed(ActionEvent event) {
            BToggleButton button = (BToggleButton) event.getSource();
            if (button.isSelected()) {

            }
//            System.out.println(event.getAction());
        }
    };

    protected void createWindows() {
        BWindow window = new BWindow(BuiSystem.getStyle(), GroupLayout.makeVStretch());
        BContainer container = new BContainer(GroupLayout.makeVStretch());
        BMultiSelectBox<String> list = new BMultiSelectBox<String>() {

            protected BToggleButton createListEntry(String value) {
                BToggleButton entry = new BToggleButton(value, "select");
                entry.setProperty("entry", value);
                entry.addListener(listener2);
                entry.setStyleClass("list_entry");
                return entry;
            }
        };

        list.setSelectionMode(SelectionMode.MULTIPLE);
        list.setName("listEntry");
        list.setStyleClass("scrolling_list");
        list.setPreferredSize(new Dimension(200, 200));
        list.addListener(listener2);

        for (int i = 0; i < 10; i++) {
            String name = "Name" + i;

            list.addValue(name, false);
        }


        container.add(list);

        window.add(container);

        window.setSize(400, 400);

        BuiSystem.addWindow(window);

        window.center();
    }

    public static void main(String[] args) {
        new MultiSelectBoxTest().start();
    }
}
