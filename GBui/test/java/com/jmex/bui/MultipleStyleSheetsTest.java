package com.jmex.bui;

import com.jmex.bui.base.BaseTest;
import com.jmex.bui.bss.BStyleSheetUtil;
import com.jmex.bui.layout.GroupLayout;

public class MultipleStyleSheetsTest extends BaseTest {
    protected void createWindows() {
        BStyleSheet style1 = BStyleSheetUtil.getStyleSheetFromFile("/rsrc/styles.properties");
//        BStyleSheet style2 = BStyleSheetUtil.getStyleSheetFromFile("/rsrc/styles2.properties");
//        BStyleSheet style3 = BStyleSheetUtil.getStyleSheet("/rsrc/style2.bss");

        BuiSystem.init(new PolledRootNode(timer, input), style1);
//
        rootNode.attachChild(BuiSystem.getRootNode());

        BWindow window1 = new BWindow(style1, GroupLayout.makeVStretch());
//        window1.add(new BButton("bob", "bob"));
        window1.setSize(400, 400);

//        BWindow window2 = new BWindow(style2, GroupLayout.makeVStretch());
////        window2.add(new BButton("allen"));
//        window2.setSize(400, 400);

//        BWindow window3 = new BWindow(style3, GroupLayout.makeVStretch());
////        window2.add(new BButton("allen"));
//        window3.setSize(400, 400);


        BuiSystem.addWindow(window1);
//        window1.setLocation(200, 200);

//        BuiSystem.addWindow(window3);

//        BuiSystem.addWindow(window2);
//        window2.center();

//        BuiSystem.addWindow(window3);
//        window3.center();
    }

    public static void main(String[] args) {
        new MultipleStyleSheetsTest().start();
    }
}
