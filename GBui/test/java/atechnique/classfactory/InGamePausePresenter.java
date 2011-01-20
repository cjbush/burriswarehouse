package atechnique.classfactory;

import atechnique.game.state.GameManager;
import atechnique.views.interfaces.IInGamePauseListener;
import atechnique.views.interfaces.IInGamePauseView;

public class InGamePausePresenter extends GameStatePresenter implements IInGamePauseListener {
    private IInGamePauseView _view;

    public InGamePausePresenter(IInGamePauseView view) {
        super(view);

        _view = view;

        _view.addInGamePauseListener(this);
    }

    @Override
    public void resumeGamePressed() {
        GameManager.getInstance().popState();
    }

    @Override
    public void editSettingsPressed() {
        GameManager.getInstance().pushState(ClassFactory.getEditSettingsPresenter());
    }

    @Override
    public void exitGamePressed() {
        GameManager.getInstance().changeState(ClassFactory.getMainMenuPresenter());
    }

    @Override
    public void saveAsPressed() {
//		GameManager.getInstance().changeState(ClassFactory.getSelectCampaignPresenter());
    }

    @Override
    public void savePressed() {
//		GameManager.getInstance().changeState(ClassFactory.getSelectCampaignPresenter());
    }
}
