/**
 * $Id$
 * $Copyright$
 */
package com.jmex.bui;

/**
 * @author torr
 * @since Aug 31, 2009 - 10:39:28 AM
 */

import com.jme.app.SimpleGame;
import com.jme.input.MouseInput;
import com.jme.math.FastMath;
import com.jme.util.Timer;
import com.jmex.bui.background.ImageBackground;
import com.jmex.bui.enumeratedConstants.ImageBackgroundMode;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.ActionListener;
import com.jmex.bui.layout.AbsoluteLayout;
import com.jmex.bui.util.Point;

import java.io.IOException;
import java.net.URL;

public class ButtonWiggleTest extends SimpleGame {

    public static void main(String[] args) {
        ButtonWiggleTest app = new ButtonWiggleTest();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
        app.start();
    }

    float transitionPosition = 1f;
    Point startPosition = new Point(270, 190);
    BWindow bWindow;
    BButton bButton;

    protected void simpleInitGame() {
        MouseInput.get().setCursorVisible(true);
        BuiSystem.init(new PolledRootNode(Timer.getTimer(), input), "/rsrc/style2.bss");
        rootNode.attachChild(BuiSystem.getRootNode());
        bWindow = new BWindow(BuiSystem.getStyle(), new AbsoluteLayout());
        bButton = new BButton("Click Me!");
        bButton.setPreferredSize(100, 100);
        bButton.addListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                System.out.println("Button Pressed!");
            }
        });

        bWindow.setSize(640, 480);
        bWindow.add(bButton, new Point(270, 190));
        BImage image = null;
        URL url = null;
        try {
            url = getClass().getClassLoader().getResource("jmetest/data/texture/Detail.jpg");
            image = new BImage(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        bWindow.setBackground(BComponent.DEFAULT, new ImageBackground(ImageBackgroundMode.SCALE_XY, image));
        BuiSystem.addWindow(bWindow);
        bWindow.center();
    }

    public void simpleUpdate() {
        updateTransition(super.tpf, 10f, -1);

        float transitionOffset = (float) FastMath.pow(transitionPosition, 2);

        int tempPosition = (int) startPosition.x;

        tempPosition -= transitionOffset * 512;

        bButton.setBounds(tempPosition,
                          bButton.getY(),
                          bButton.getWidth(),
                          bButton.getHeight());

        super.simpleUpdate();
    }

    boolean updateTransition(float tpf, float transitionOffTime, int direction) {
        // How much should we move by?
        float transitionDelta;

        if (transitionOffTime == 0) {
            transitionDelta = 1;
        } else {
            transitionDelta = tpf;
        }

        // Update the transition position.
        transitionPosition += transitionDelta * direction;

        // Did we reach the end of the transition?
        if (((direction < 0) && (transitionPosition <= 0)) ||
            ((direction > 0) && (transitionPosition >= 1))) {
            transitionPosition = FastMath.clamp(transitionPosition, 0, 1);
            return false;
        }

        // Otherwise we are still busy transitioning.
        return true;
    }
}