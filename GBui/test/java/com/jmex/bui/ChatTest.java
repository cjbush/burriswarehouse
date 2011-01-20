/**
 * $ID:$
 * $COPYRIGHT:$
 */
package com.jmex.bui;

import com.jmex.bui.base.BaseTest;
import com.jmex.bui.layout.BorderLayout;
import com.jmex.bui.listener.ChatListener;

/**
 * @author torr
 * @since Apr 10, 2009 - 2:44:23 PM
 */
public class ChatTest extends BaseTest {
    @Override
    protected void createWindows() {
//        for (int i = 0; i < 3; i++) {
//            BTitledWindow mw = MessageWindowUtil.
//                    createMessageBox("Window #" + i, "Window #" + i, TitleOptions.MIN_MAX_CLOSE, "Some content");
//            BuiSystem.getRootNode().addWindow(mw);
//        }

        BChatWindow bcw = new BChatWindow(BuiSystem.getStyle(), new BorderLayout(1, 2));
        bcw.add(new BChatComponent("player 1", new ChatListener(bcw)), BorderLayout.WEST);
        bcw.add(new BChatComponent("player 2", new ChatListener(bcw)), BorderLayout.EAST);

        bcw.setBounds(20, 140, 400, 250);
        BuiSystem.addWindow(bcw);
    }

    public static void main(String[] args) {
        new ChatTest().start();
    }
}
