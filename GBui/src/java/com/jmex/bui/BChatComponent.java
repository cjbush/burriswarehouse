/**
 * $ID:$
 * $COPYRIGHT:$
 */
package com.jmex.bui;

import com.jme.renderer.ColorRGBA;
import com.jmex.bui.enumeratedConstants.Orientation;
import com.jmex.bui.layout.BorderLayout;
import com.jmex.bui.listener.ChatListener;

/**
 * @author torr
 * @since Apr 10, 2009 - 2:58:38 PM
 */
public class BChatComponent extends BContainer {
    private BTextArea _text = new BTextArea();
    private BTextField _input = new BTextField();
    private String chatWindowName;

    public BChatComponent(String name, ChatListener chatListener) {
        super(name);
        setLayoutManager(new BorderLayout(1, 2));

        add(_text = new BTextArea(), BorderLayout.CENTER);
        add(_input = new BTextField(), BorderLayout.SOUTH);
        add(new BScrollBar(Orientation.VERTICAL, _text.getScrollModel()),
            BorderLayout.EAST);
        add(new BScrollBar(Orientation.HORIZONTAL, 0, 25, 50, 100),
            BorderLayout.NORTH);

        _input.addListener(chatListener);
    }

    public String getChatWindowName() {
        return chatWindowName;
    }

    public void setChatWindowName(final String _chatWindowName) {
        chatWindowName = _chatWindowName;
    }

    public BTextField getInput() {
        return _input;
    }

    public BTextArea getText() {
        return _text;
    }

    public void update(String senderName, String message) {
        if (!getName().equals(senderName)) {
            _text.appendText(senderName + ": ", ColorRGBA.blue);
            _text.appendText(message + "\n");
        }
    }
}

