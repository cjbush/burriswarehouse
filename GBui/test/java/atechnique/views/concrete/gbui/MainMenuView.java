package atechnique.views.concrete.gbui;

import atechnique.Main;
import atechnique.views.interfaces.IMainMenuListener;
import atechnique.views.interfaces.IMainMenuView;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.util.GameTaskQueueManager;
import com.jmex.bui.BButton;
import com.jmex.bui.BComponent;
import com.jmex.bui.BImage;
import com.jmex.bui.BWindow;
import com.jmex.bui.BuiSystem;
import com.jmex.bui.background.ImageBackground;
import com.jmex.bui.enumeratedConstants.ImageBackgroundMode;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.ActionListener;
import com.jmex.bui.layout.AbsoluteLayout;
import com.jmex.bui.util.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class MainMenuView extends GbuiGameState implements IMainMenuView {
    private BButton _btnPlayCampaign;
    private BButton _btnConnectToGame;
    private BButton _btnEditSettings;
    private BButton _btnExit;

    private IMainMenuListener _mainMenuListener;

    public MainMenuView() {
        super("MainMenuView");

        defineControls();

        _window = new BWindow(BuiSystem.getStyle(), new AbsoluteLayout());
        _window.setSize((int) (0.8f * 1.2f * display.getWidth() / 2),
                        (int) (1.4f * (display.getHeight() / 2)));
        try {
            GameTaskQueueManager.getManager().update(new Callable<Object>() {
                public Object call() throws Exception {
                    BImage image = new BImage(getClass().getClassLoader().
                            getResource("atechnique/images/sky/dg_east.PNG"));
                    _window.setBackground(BComponent.DEFAULT, new ImageBackground(ImageBackgroundMode.SCALE_XY, image));
                    return null;
                }
            }).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // x,y of component are the lower-left corner
        int startPosY = _window.getHeight() - 100;
        int buttonCount = 4;
        int buttonHeight = (int) (startPosY / (1.5f * buttonCount));
        int stepPosY = (int) (buttonHeight * 1.5f);
        int buttonWidth = (int) (_window.getWidth() * 0.6f);
        int posX = (_window.getWidth() - buttonWidth) / 2;
        int counter = 0;

        _btnPlayCampaign = new BButton("");
        _btnPlayCampaign.setPreferredSize(buttonWidth, buttonHeight);
        _btnPlayCampaign.addListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                _mainMenuListener.playCampaignPressed();
            }
        });
        _window.add(_btnPlayCampaign, new Point(posX, startPosY - stepPosY * counter++));

        _btnConnectToGame = new BButton("");
        _btnConnectToGame.setPreferredSize(buttonWidth, buttonHeight);
        _btnConnectToGame.setEnabled(false);
        _btnConnectToGame.addListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                _mainMenuListener.connectToGamePressed();
            }
        });
        _window.add(_btnConnectToGame, new Point(posX, startPosY - stepPosY * counter++));

        _btnEditSettings = new BButton("");
        _btnEditSettings.setPreferredSize(buttonWidth, buttonHeight);
        _btnEditSettings.addListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                _mainMenuListener.editSettingsPressed();
            }
        });
        _window.add(_btnEditSettings, new Point(posX, startPosY - stepPosY * counter++));

        _btnExit = new BButton("");
        _btnExit.setPreferredSize(buttonWidth, buttonHeight);
        _btnExit.addListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                _mainMenuListener.exitPressed();
            }
        });
        _window.add(_btnExit, new Point(posX, startPosY - stepPosY * counter++));

        _window.center();
    }

    private void defineControls() {
        KeyBindingManager keyboard = KeyBindingManager.getKeyBindingManager();
        keyboard.add("exit", KeyInput.KEY_ESCAPE);
    }

    public void addMainMenuListener(IMainMenuListener mainMenuListener) {
        _mainMenuListener = mainMenuListener;
    }

    public List<String> getTranslationTags() {
        List<String> translationTags = new ArrayList<String>(8);
        translationTags.add("MainMenu.PlayCampaign");    // Play Campaign
        translationTags.add("MainMenu.PlayCampaignDescription");    // Select a campaign to play
        translationTags.add("MainMenu.ConnectToGame");    // Connect to a Game
        translationTags.add("MainMenu.ConnectToGameDescription");    // Connect to a game on another server
        translationTags.add("MainMenu.EditSettings");    // Settings
        translationTags.add("MainMenu.EditSettingsDescription");    // Settings Button
        translationTags.add("MainMenu.Exit");    // Exit
        translationTags.add("MainMenu.ExitDescription");    // Exits this game

        return translationTags;
    }

    public void setTranslationPhrases(List<String> translationPhrases) {
        _btnPlayCampaign.setText(translationPhrases.get(0));
        _btnPlayCampaign.setTooltipText(translationPhrases.get(1));
        _btnConnectToGame.setText(translationPhrases.get(2));
        _btnConnectToGame.setTooltipText(translationPhrases.get(3));
        _btnEditSettings.setText(translationPhrases.get(4));
        _btnEditSettings.setTooltipText(translationPhrases.get(5));
        _btnExit.setText(translationPhrases.get(6));
        _btnExit.setTooltipText(translationPhrases.get(7));
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);

        if (KeyBindingManager.getKeyBindingManager().isValidCommand("exit", true)) {
            Main.exit();
        }
    }
}
