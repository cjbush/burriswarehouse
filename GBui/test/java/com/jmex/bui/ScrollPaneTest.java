package com.jmex.bui;

import com.jmex.bui.base.BaseTest;
import com.jmex.bui.layout.BorderLayout;

public class ScrollPaneTest extends BaseTest {

    @Override
    protected void createWindows() {

        BWindow window = new BWindow(BuiSystem.getStyle(), new BorderLayout());
        window.setLayoutManager(new BorderLayout());
        window.setSize(400, 300);
        window.center();

        BTextArea textArea = new BTextArea(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                "Phasellus ipsum nulla, pretium sed egestas nec, rhoncus non eros. Fusce quis purus massa. " +
                "Sed enim orci, lacinia mollis tincidunt ut, sagittis rhoncus urna. Pellentesque vulputate " +
                "mi id quam dictum suscipit. Aenean aliquet semper nisl eget consectetur. Etiam leo nibh, " +
                "eleifend ac suscipit a, eleifend ac velit. Aenean elit lorem, porttitor sed accumsan id, " +
                "viverra gravida nunc. Ut sagittis pulvinar leo quis fringilla. Fusce orci sapien, pellentesque " +
                "nec fringilla cursus, consectetur vitae lectus. Nullam et pretium odio. Nunc vulputate lectus ut " +
                "tortor fermentum et pulvinar odio facilisis. Mauris quis lobortis dolor. Nunc tincidunt," +
                " purus vitae convallis commodo, justo libero pulvinar orci, non feugiat ipsum dolor ut" +
                " velit. Curabitur suscipit est non quam mattis posuere. Nunc tortor turpis, malesuada vel" +
                " luctus vel, placerat eget risus.");
        BScrollPane scrollPane = new BScrollPane(textArea);
        window.add(scrollPane, BorderLayout.CENTER);

        BuiSystem.getRootNode().addWindow(window);
    }

    public static void main(String[] args) {
        new ScrollPaneTest().start();
    }

}
