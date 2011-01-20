package atechnique.classfactory;

import atechnique.game.state.GameManager;
import atechnique.models.interfaces.IStartServerModel;
import atechnique.views.interfaces.IStartServerListener;
import atechnique.views.interfaces.IStartServerView;
import com.jme.system.GameSettings;

public class StartServerPresenter extends GameStatePresenter implements IStartServerListener {
    private IStartServerView _view;
    private IStartServerModel _model;
    private GameSettings _settings;

    protected StartServerPresenter(IStartServerView view, IStartServerModel model) {
        super(view);

        _view = view;
        _model = model;
        _settings = ClassFactory.getGameSettings();

        _view.addStartServerListener(this);

        String nickname = _settings.get("nickname", "");
        _view.setNickname(nickname);

        int port = _settings.getInt("ServerPort", 9100);
        _view.setPort(port);

//TODO:		List<String> allegiances = ClassFactory.getCampaign().getAllegiances();
//		_view.setAllegiances(allegiances);
    }

    @Override
    public void cancelPressed() {
        GameManager.getInstance().popState();
    }

    @Override
    public void okPressed() {
        // TODO: get the selected nickname, server port, and allegiance
        // TODO: start the server and display the game
        GameManager.getInstance().changeState(ClassFactory.getInGameState());
    }
}
