/**
 * $ID:$
 * $COPYRIGHT:$
 */
package com.jmex.bui;

import com.jmex.bui.layout.BLayoutManager;

/**
 * @author torr
 * @since Apr 10, 2009 - 3:14:36 PM
 */
public class BChatWindow extends BWindow {
    public BChatWindow(final String name, final BStyleSheet style, final BLayoutManager layout) {
        super(name, style, layout);
    }

    public BChatWindow(final BStyleSheet style, final BLayoutManager layout) {
        super(style, layout);
    }

    public void update(String senderName, String message) {
        applyOperation(new ChatChildOp(senderName, message));
    }
}