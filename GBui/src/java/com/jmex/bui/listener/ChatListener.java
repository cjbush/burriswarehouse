/**
 * $ID:$
 * $COPYRIGHT:$
 */
package com.jmex.bui.listener;

import com.jme.renderer.ColorRGBA;
import com.jmex.bui.BChatComponent;
import com.jmex.bui.BChatWindow;
import com.jmex.bui.BComponent;
import com.jmex.bui.BTextArea;
import com.jmex.bui.BTextField;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.ActionListener;

/**
 * @author torr
 * @since Apr 10, 2009 - 3:07:28 PM
 */
public class ChatListener implements ActionListener {
    String inputText;
    String chatWindowName;
    BChatWindow parent;

    public ChatListener(BChatWindow parent) {
        this.parent = parent;
    }

    public void actionPerformed(final ActionEvent event) {
        final BComponent bc = ((BComponent) event.getSource()).getParent();
        if (bc instanceof BChatComponent) {

            final BChatComponent bcc = (BChatComponent) bc;
            final BTextField _input = bcc.getInput();
            final BTextArea _text = bcc.getText();
            final String inputText = _input.getText();

            if (inputText != null && !inputText.equals("")) {
                _text.appendText("You said: ", ColorRGBA.red);
                _text.appendText(inputText + "\n");
                _input.setText("");
                parent.update(bcc.getName(), inputText);
            }
        }
    }

    public void setChatWindowName(String name) {
        chatWindowName = name;
    }
}
