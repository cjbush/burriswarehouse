package com.jmex.bui;

import com.jme.app.SimpleGame;
import com.jme.input.MouseInput;
import com.jme.light.DirectionalLight;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jme.scene.state.LightState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jmex.bui.background.TintedBackground;
import com.jmex.bui.headlessWindows.BDraggableWindow;
import com.jmex.bui.layout.BorderLayout;

public class GeomViewTest extends SimpleGame {
    private Box box;
    private long lastRotation = 0;

    @Override
    protected void simpleInitGame() {
        display.getRenderer().setBackgroundColor(ColorRGBA.orange);

        MouseInput.get().setCursorVisible(true);
        BuiSystem.init(new PolledRootNode(timer, input), "/rsrc/style2.bss");

        DirectionalLight light = new DirectionalLight();
        light.setDiffuse(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
        light.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
        light.setDirection(new Vector3f(1, -1, 0));
        light.setEnabled(true);

        Renderer renderer = DisplaySystem.getDisplaySystem().getRenderer();

        LightState ls = renderer.createLightState();
        ls.setEnabled(true);
        ls.attach(light);

        box = new Box("box", new Vector3f(), 2, 2, 2);
        Quaternion quat45 = new Quaternion();
        quat45.fromAngleAxis(0.7854f, new Vector3f(1, 1, 1));
        box.setLocalRotation(quat45);

        ZBufferState zBufferState = renderer.createZBufferState();
        zBufferState.setEnabled(true);
        zBufferState.setFunction(ZBufferState.TestFunction.LessThan);

        Node n = new Node("blah");
        n.setRenderState(ls);
        n.setRenderState(zBufferState);
        n.attachChild(box);
        n.updateRenderState();

        BGeomView view = new BGeomView();
        view.setGeometry(n);

        BWindow window = new BDraggableWindow(BuiSystem.getStyle(), null);
        window.setLayoutManager(new BorderLayout());
        window.setSize(400, 300);
        window.center();

        window.add(view, BorderLayout.CENTER);
        BuiSystem.addWindow(window);

        window.setBackground(BComponent.DEFAULT, new TintedBackground(new ColorRGBA(0, 0, 1, 0.7f)));

        rootNode.attachChild(BuiSystem.getRootNode());
    }

    @Override
    protected void simpleUpdate() {
        if (timer.getTime() - lastRotation > 10) {
            Vector3f axis = new Vector3f(1, -1, 0);
            float currentAngle = box.getLocalRotation().toAngleAxis(axis) * FastMath.RAD_TO_DEG;
            currentAngle += 1;
            if (currentAngle >= 360) {
                currentAngle = 1;
            }
            currentAngle *= FastMath.DEG_TO_RAD;
            box.setLocalRotation(box.getLocalRotation().fromAngleAxis(currentAngle, axis));
            lastRotation = timer.getTime();
        }
    }

    public static void main(String[] args) {
        GeomViewTest test = new GeomViewTest();
        test.start();
    }
}
