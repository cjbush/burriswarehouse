package atechnique.views.concrete.gbui;

import atechnique.classfactory.ClassFactory;
import com.jme.system.DisplaySystem;
import com.jme.util.GameTaskQueueManager;
import com.jme.util.Timer;
import com.jmex.bui.BWindow;
import com.jmex.bui.BuiSystem;
import com.jmex.bui.PolledRootNode;
import com.jmex.game.state.BasicGameState;

import java.util.concurrent.Callable;

public class GbuiGameState extends BasicGameState {
    // Unless we find a reason to have one of these for each sub-class, let's just share static instances
    protected static DisplaySystem display;
    protected BWindow _window;

    static {
        display = DisplaySystem.getDisplaySystem();

        // Move the mouse to the middle of the screen to start with
        ClassFactory.getMouseManager().setMousePosition(display.getWidth() / 2, display.getHeight() / 2);

        BuiSystem.init(new PolledRootNode(Timer.getTimer(), ClassFactory.getInputHandler()), "/atechnique/gbui/style2.bss");
    }

    public GbuiGameState(String name) {
        super(name);
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);

        ClassFactory.getInputHandler().update(tpf);
    }

    public void activate() {
        // activate the main GameState
        super.setActive(true);

        // Display the GBUI portion
        rootNode.attachChild(ClassFactory.getMouseManager().getMouse().getMouseSpatial());
        rootNode.attachChild(BuiSystem.getRootNode());

        GameTaskQueueManager.getManager().update(new Callable<Object>() {
            public Object call() throws Exception {
                BuiSystem.addWindow(_window);
                _window.getRootNode().updateRenderState();
                return null;
            }
        });

        rootNode.updateRenderState();
    }

    public void deactivate() {
        // If still active, hide the GBUI portion
        GameTaskQueueManager.getManager().update(new Callable<Object>() {
            public Object call() throws Exception {

                if (_window.getRootNode() != null) {
                    _window.dismiss();
                }
                return null;
            }
        });

//        rootNode.detachChild(BuiSystem.getRootNode());
//        rootNode.detachChild(ClassFactory.getMouseManager().getMouse().getMouseSpatial());
        rootNode.updateRenderState();

        // Deactivate the main GameState portion
        super.setActive(false);
    }
}
