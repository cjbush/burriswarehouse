package com.jmex.bui;

import com.jmex.bui.enumeratedConstants.DialogOptions;
import com.jmex.bui.event.ActionListener;
import com.jmex.bui.layout.HGroupLayout;
import com.jmex.bui.layout.Justification;
import com.jmex.bui.layout.Policy;
import com.jmex.bui.layout.GroupLayout;
import com.jmex.bui.util.Dimension;

import java.util.ArrayList;

public class BButtonBar extends BContainer {
    private static final Dimension DEFAULT_SIZE = new Dimension(50, 30);
    private ArrayList<BButton> buttons = new ArrayList<BButton>(2);
    private DialogOptions dialogOptions;

    public BButtonBar(String _name, DialogOptions options) {
        super(_name, new HGroupLayout(Justification.CENTER, Policy.EQUALIZE));
        dialogOptions = options;
        createButtons();
    }

    public void setButtonListener(ActionListener listener) {
        for (BButton button : buttons) {
            button.addListener(listener);
        }
    }

    private void createButtons() {
        BButton button;
        if (dialogOptions != null) {
            switch (dialogOptions) {
                case CANCEL:
                    button = new BButton("Cancel", UserResponse.CANCEL.toString());
                    button.setPreferredSize(DEFAULT_SIZE);
                    button.setStyleClass("dialogbutton");
                    add(button);
                    buttons.add(button);
                    break;
                case OK:
                    button = new BButton("OK", UserResponse.OK.toString());
                    button.setPreferredSize(DEFAULT_SIZE);
                    button.setStyleClass("dialogbutton");
                    add(button);
                    buttons.add(button);
                    break;
                case YES_NO:
                    button = new BButton("Yes", UserResponse.YES.toString());
                    button.setPreferredSize(DEFAULT_SIZE);
                    button.setStyleClass("dialogbutton");
                    add(button);
                    buttons.add(button);
                    button = new BButton("No", UserResponse.NO.toString());
                    button.setPreferredSize(DEFAULT_SIZE);
                    button.setStyleClass("dialogbutton");
                    add(button);
                    buttons.add(button);
                    break;
                case YES_CANCEL:
                    button = new BButton("Yes", UserResponse.YES.toString());
                    button.setPreferredSize(DEFAULT_SIZE);
                    button.setStyleClass("dialogbutton");
                    add(button);
                    buttons.add(button);
                    button = new BButton("Cancel", UserResponse.CANCEL.toString());
                    button.setSize(50, 12);
                    button.setStyleClass("dialogbutton");
                    add(button);
                    buttons.add(button);
                    break;
                case YES_NO_CANCEL:
                    button = new BButton("Yes", UserResponse.YES.toString());
                    button.setPreferredSize(DEFAULT_SIZE);
                    button.setStyleClass("dialogbutton");
                    add(button);
                    buttons.add(button);
                    button = new BButton("No", UserResponse.NO.toString());
                    button.setPreferredSize(DEFAULT_SIZE);
                    button.setStyleClass("dialogbutton");
                    add(button);
                    buttons.add(button);
                    button = new BButton("Cancel", UserResponse.CANCEL.toString());
                    button.setPreferredSize(DEFAULT_SIZE);
                    button.setStyleClass("dialogbutton");
                    add(button);
                    buttons.add(button);
                    break;
                default:
                    throw new RuntimeException("Option not implemented");
            }
        }
    }

    public DialogOptions getDialogOptions() {
        return dialogOptions;
    }
}
