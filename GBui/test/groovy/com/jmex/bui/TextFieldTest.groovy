/**
 * $ID$
 * $COPYRIGHT$
 */
package com.jmex.bui

import com.jme.renderer.ColorRGBA
import com.jmex.bui.BConstants
import com.jmex.bui.BScrollBar
import com.jmex.bui.BTextArea
import com.jmex.bui.BTextField
import com.jmex.bui.BWindow
import com.jmex.bui.BuiSystem
import com.jmex.bui.base.BaseTest2
import com.jmex.bui.event.ActionListener
import com.jmex.bui.layout.BorderLayout


/**
 *
 * @author timo
 * @since Oct 11, 2008 7:06:55 PM
 */
public class TextFieldTest extends BaseTest2 {
  BTextArea _text;
  BTextField _input;

  protected void createWindows() {
    BWindow window = new BWindow(BuiSystem.getStyle(), new BorderLayout(5, 5));
    window.add(_text = new BTextArea(), BorderLayout.CENTER);
    window.add(_input = new BTextField(), BorderLayout.SOUTH);
    window.add(new BScrollBar(BConstants.VERTICAL, _text.getScrollModel()),
               BorderLayout.EAST);
    window.add(new BScrollBar(BConstants.HORIZONTAL, 0, 25, 50, 100),
               BorderLayout.NORTH);
    _input.addListener({
                       String inputText = _input.getText();
                       if (inputText != null && !inputText.equals("")) {
                       _text.appendText("You said: ", ColorRGBA.red);
                       _text.appendText(_input.getText() + "\n");
                       _input.setText("");
                       }
                       } as ActionListener);

    BuiSystem.getRootNode().addWindow(window);
    window.setSize(400, 400);
    window.setLocation(25, 25);
  }

  public static void main(String[] args) {
    TextFieldTest test = new TextFieldTest();
    test.start();
  }
}