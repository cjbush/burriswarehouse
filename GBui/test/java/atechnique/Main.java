package atechnique;

import atechnique.classfactory.ClassFactory;
import atechnique.game.state.GameManager;
import com.jme.renderer.ColorRGBA;
import com.jmex.game.StandardGame;

public class Main {
    private static StandardGame _game;

    /**
     * @param args String[]
     */
    public static void main(String[] args) {
        _game = new StandardGame("ATechnique", StandardGame.GameType.GRAPHICAL, ClassFactory.getGameSettings());
        _game.setBackgroundColor(ColorRGBA.darkGray);
        _game.start();

        ClassFactory.initialize(_game.getCamera());

        GameManager.getInstance().start(ClassFactory.getMainMenuPresenter());
    }

    public static void exit() {
        _game.shutdown();
    }

    public static void changeResolution() {
        _game.recreateGraphicalContext();
    }
}
