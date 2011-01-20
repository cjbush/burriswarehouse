package atechnique.classfactory;

import atechnique.Main;
import atechnique.game.state.GameManager;
import atechnique.views.interfaces.IMainMenuListener;
import atechnique.views.interfaces.IMainMenuView;

public class MainMenuPresenter extends GameStatePresenter implements IMainMenuListener {
    private IMainMenuView _view;

    public MainMenuPresenter(IMainMenuView view) {
        super(view);

        _view = view;

        _view.addMainMenuListener(this);
    }

    public void playCampaignPressed() {
        GameManager.getInstance().changeState(ClassFactory.getSelectCampaignPresenter());
    }

    public void connectToGamePressed() {
//		GameManager.getInstance().changeState(ClassFactory.getConnectToGameState());
    }

    public void editSettingsPressed() {
        GameManager.getInstance().pushState(ClassFactory.getEditSettingsPresenter());
    }

    public void exitPressed() {
        Main.exit();
    }
}
