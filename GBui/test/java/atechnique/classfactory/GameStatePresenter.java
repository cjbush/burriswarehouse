package atechnique.classfactory;

import atechnique.game.state.ATechniqueGameState;
import atechnique.views.interfaces.IGameStateView;

public class GameStatePresenter implements ATechniqueGameState {
    protected IGameStateView _view;

    protected GameStatePresenter(IGameStateView view) {
        _view = view;
        _view.setTranslationPhrases(ClassFactory.getTranslator().getTranslatedPhrases(_view.getTranslationTags()));
    }

    public void enter() {
        _view.activate();
    }

    public void exit() {
        _view.deactivate();
    }

    public void pause() {
        _view.deactivate();
    }

    public void resume() {
        _view.activate();
    }
}
