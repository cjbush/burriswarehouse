/**
 * $ID:$
 * $COPYRIGHT:$
 */
package com.jmex.bui

import com.jmex.bui.BButton
import com.jmex.bui.BContainer
import com.jmex.bui.BWindow
import com.jmex.bui.BuiSystem
import com.jmex.bui.base.BaseTest2
import com.jmex.bui.layout.AbsoluteLayout
import com.jmex.bui.layout.GroupLayout
import com.jmex.bui.util.Rectangle

/**
 *
 *
 * @author torr
 * @since Dec 12, 2008 - 9:59:01 AM
 *
 */
class ButtonArtifactTest extends BaseTest2 {

  protected void createWindows() {
    BWindow window = new BWindow(BuiSystem.getStyle(), GroupLayout.makeVStretch());

    BContainer centerContainer = new BContainer(new AbsoluteLayout());

    def bc = new BButton("I was here");
    bc.setSize(300, 300)

//        centerContainer.add(bc, new Rectangle(190, A0, 100, 40));
    centerContainer.add(bc, new Rectangle(190, 0, 100, 40));

    window.add(centerContainer);
    window.setSize(400, 400);

    BuiSystem.addWindow(window);
    window.center();
  }

  public static void main(String[] args) {
    new ButtonArtifactTest().start();
  }
}