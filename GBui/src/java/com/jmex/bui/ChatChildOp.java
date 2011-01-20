/**
 * $ID:$
 * $COPYRIGHT:$
 */
package com.jmex.bui;

/**
 * @author torr
 * @since Apr 10, 2009 - 3:46:31 PM
 */
public class ChatChildOp implements BContainer.ChildOp {
    private String sender;
    private String message;

    public ChatChildOp(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public void apply(final BComponent child) {
        if (child instanceof BChatComponent) {
            BChatComponent chil = (BChatComponent) child;
            chil.update(sender, message);
        }
    }
}
