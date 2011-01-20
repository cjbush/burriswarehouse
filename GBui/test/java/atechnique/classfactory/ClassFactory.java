package atechnique.classfactory;

import atechnique.Campaign;
import atechnique.Main;
import atechnique.models.concrete.EditSettingsModel;
import atechnique.models.concrete.SelectCampaignModel;
import atechnique.models.concrete.StartServerModel;
import atechnique.models.interfaces.IEditSettingsModel;
import atechnique.models.interfaces.ISelectCampaignModel;
import atechnique.models.interfaces.IStartServerModel;
import atechnique.views.interfaces.IEditSettingsView;
import atechnique.views.interfaces.IInGamePauseView;
import atechnique.views.interfaces.IMainMenuView;
import atechnique.views.interfaces.ISelectCampaignView;
import atechnique.views.interfaces.IStartServerView;
import atechnique.views.interfaces.ITranslator;
import com.jme.input.InputHandler;
import com.jme.input.MouseInput;
import com.jme.renderer.Camera;
import com.jme.system.GameSettings;
import com.jme.system.PreferencesGameSettings;
import com.jmex.game.state.GameState;
import com.jmex.game.state.GameStateManager;
import org.lex.input.mouse.MouseManager;
import org.lex.input.mouse.component.CombinedMouse;

import java.util.prefs.Preferences;

public class ClassFactory {
    private static Preferences _preferences;
    private static GameSettings _gameSettings;
    private static ITranslator _translator;
    private static Campaign _campaign = null;
    private static Camera _camera = null;

    private static InputHandler _inputHandler;
    private static String defaultCursor = "defaultCursor";
    private static String attackCursor = "attackCursor";
    private static String selectCursor = "selectCursor";
    private static String[] _cursors = {defaultCursor, attackCursor, selectCursor};
    public static int currentCursorIndex;
    private static MouseManager _mouseManager;
    private static IMainMenuView _mainMenuView;
    private static MainMenuPresenter _mainMenuPresenter;
    private static ISelectCampaignView _selectCampaignView;
    private static ISelectCampaignModel _selectCampaignModel;
    private static SelectCampaignPresenter _selectCampaignPresenter;
    private static IStartServerView _startServerView;
    private static IStartServerModel _startServerModel;
    private static StartServerPresenter _startServerPresenter;
    private static IEditSettingsView _editSettingsView;
    private static IEditSettingsModel _editSettingsModel;
    private static EditSettingsPresenter _editSettingsPresenter;
    private static IInGamePauseView _inGamePauseView;
    private static InGamePausePresenter _inGamePausePresenter;
    private static InGameState _inGameState = null;

    static {
        _preferences = Preferences.userNodeForPackage(Main.class);
        _gameSettings = new PreferencesGameSettings(_preferences);

        String language = _gameSettings.get("language", "en");
        String country = _gameSettings.get("country", "US");
        _translator = new Translator(language, country);

        _inputHandler = new InputHandler();
    }

    public static void initialize(Camera camera) {
        _mouseManager = new MouseManager(CombinedMouse.get());
        _mouseManager.setNativeMousePreferred(true);
        MouseInput.get().setCursorVisible(true);

        _mouseManager.setCursor(defaultCursor, "atechnique/cursors/goldenarrow_v2/default.cursor");
        _mouseManager.setCursor(attackCursor, "atechnique/cursors/goldenarrow_v2/spinning.cursor");
        _mouseManager.setCursor(selectCursor, "atechnique/cursors/goldenarrow_select/default.cursor");
        _mouseManager.useCursor(defaultCursor);

        _mouseManager.registerWithInputHandler(_inputHandler);

        _camera = camera;

        _mainMenuView = new atechnique.views.concrete.gbui.MainMenuView();
        _mainMenuPresenter = new MainMenuPresenter(_mainMenuView);
        GameStateManager.getInstance().attachChild((GameState) _mainMenuView);

        _selectCampaignView = new atechnique.views.concrete.gbui.SelectCampaignView();
        _selectCampaignModel = new SelectCampaignModel();
        _selectCampaignPresenter = new SelectCampaignPresenter(_selectCampaignView, _selectCampaignModel);
        GameStateManager.getInstance().attachChild((GameState) _selectCampaignView);

        _startServerView = new atechnique.views.concrete.gbui.StartServerView();
        _startServerModel = new StartServerModel();
        _startServerPresenter = new StartServerPresenter(_startServerView, _startServerModel);
        GameStateManager.getInstance().attachChild((GameState) _startServerView);

        _editSettingsView = new atechnique.views.concrete.gbui.EditSettingsView();
        _editSettingsModel = new EditSettingsModel();
        _editSettingsPresenter = new EditSettingsPresenter(_editSettingsView, _editSettingsModel);
        GameStateManager.getInstance().attachChild((GameState) _editSettingsView);

        _inGamePauseView = new atechnique.views.concrete.gbui.InGamePauseView();
        _inGamePausePresenter = new InGamePausePresenter(_inGamePauseView);
        GameStateManager.getInstance().attachChild((GameState) _inGamePauseView);
    }

    public static InputHandler getInputHandler() {
        return _inputHandler;
    }

    public static String getCursor(int cursorIndex) {
        return _cursors[cursorIndex];
    }

    public static int cursorCount() {
        return _cursors.length;
    }

    public static Preferences getPreferences() {
        return _preferences;
    }

    public static GameSettings getGameSettings() {
        return _gameSettings;
    }

    public static ITranslator getTranslator() {
        return _translator;
    }

    public static void setCampaign(Campaign campaign) {
        _campaign = campaign;
    }

    public static Campaign getCampaign() {
        return _campaign;
    }

    public static MouseManager getMouseManager() {
        return _mouseManager;
    }

    public static MainMenuPresenter getMainMenuPresenter() {
        return _mainMenuPresenter;
    }

    public static SelectCampaignPresenter getSelectCampaignPresenter() {
        return _selectCampaignPresenter;
    }

    public static StartServerPresenter getStartServerPresenter() {
        return _startServerPresenter;
    }

    public static EditSettingsPresenter getEditSettingsPresenter() {
        return _editSettingsPresenter;
    }

    public static InGamePausePresenter getInGamePausePresenter() {
        return _inGamePausePresenter;
    }

    public static InGameState getInGameState() {
        if (_inGameState == null) {
            _inGameState = new InGameState(_camera);
            GameStateManager.getInstance().attachChild(_inGameState);
        }

        return _inGameState;
    }
}

