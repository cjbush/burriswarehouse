package com.jmex.bui;

import com.jme.app.SimpleGame;
import com.jme.input.InputHandler;
import com.jme.input.KeyInput;
import com.jme.input.MouseInput;
import com.jme.input.action.KeyExitAction;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.shape.Box;
import com.jme.scene.state.MaterialState;
import com.jmex.bui.icon.ImageIcon;
import com.jmex.bui.provider.DefaultResourceProvider;
import com.jmex.bui.text.BTextFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.logging.Logger;

/**
 * @author ivicaz
 */
public abstract class AbstractBuiTest extends SimpleGame {
    protected void simpleInitGame() {
        setupInput();
        attachGreenBox();

        MouseInput.get().setCursorVisible(true);

        PolledRootNode root = new PolledRootNode(timer, input);
        rootNode.attachChild(root);
        initializeBuiSystem(root);

        addGui();
    }

    private void setupInput() {
        input = new InputHandler();
        input.addAction(new KeyExitAction(this), "exit-action", KeyInput.KEY_ESCAPE, false);
    }

    private void attachGreenBox() {
        Box box = new Box("boxy", new Vector3f(-3, -3, -3), new Vector3f(2, 2, 2));
        Quaternion q = new Quaternion();
        q.fromAngles(FastMath.HALF_PI / 2, FastMath.HALF_PI / 2, 0.0F);
        box.setLocalRotation(q);

        MaterialState ms = display.getRenderer().createMaterialState();
        ms.setAmbient(ColorRGBA.black);
        ms.setDiffuse(ColorRGBA.green);
        box.setRenderState(ms);

        rootNode.attachChild(box);
    }

    /**
     * Use BuiSystem to access stylesheet and root node.
     */
    protected abstract void addGui();

    private void initializeBuiSystem(BRootNode root) {
        InputStreamReader stin = null;
        try {
            stin = new InputStreamReader(ClassLoader.getSystemResourceAsStream("rsrc/style2.bss"));
            BStyleSheet stylesheet = new BStyleSheet(stin, new TestFileResourceProvider());
            BuiSystem.init(root, stylesheet);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        } finally {
            try {
                if (stin != null) {
                    stin.close();
                }
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }


    protected BImage getImage(String name) {
        try {
            return new BImage(new File(name).toURI().toURL());
        } catch (MalformedURLException e) {
            throw new IllegalStateException(e);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    protected ImageIcon getImageIcon(String name) {
        return new ImageIcon(getImage(name));
    }

    private class TestFileResourceProvider extends DefaultResourceProvider {
        public BTextFactory createTextFactory(String s, String s1, int i) {
            Logger.getAnonymousLogger().severe("THIS METHOD STILL USES DefaultResourceProvide!");
            return super.createTextFactory(s, s1, i);
        }

        public BImage loadImage(String s) throws IOException {
            BImage image = getImage(s);
            if (image == null) {
                return super.loadImage(s);
            }
            return image;
        }

        public BCursor loadCursor(String s) throws IOException {
            Logger.getAnonymousLogger().severe("THIS METHOD STILL USES DefaultResourceProvide!");
            return super.loadCursor(s);
        }
    }
}