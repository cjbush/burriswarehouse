package atechnique.views.concrete.gbui;

import atechnique.views.interfaces.IInGamePauseListener;
import atechnique.views.interfaces.IInGamePauseView;
import com.jmex.bui.BButton;
import com.jmex.bui.BWindow;
import com.jmex.bui.BuiSystem;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.ActionListener;
import com.jmex.bui.layout.AbsoluteLayout;
import com.jmex.bui.util.Point;

import java.util.ArrayList;
import java.util.List;

public class InGamePauseView extends GbuiGameState implements IInGamePauseView {
    private BButton _btnSave;
    private BButton _btnSaveAs;
    private BButton _btnEditSettings;
    private BButton _btnExitGame;
    private BButton _btnResumeGame;

    private IInGamePauseListener _inGamePauseListener;

    public InGamePauseView() {
        super("InGamePauseView");

        _window = new BWindow(BuiSystem.getStyle(), new AbsoluteLayout());
        _window.setStyleClass("pausebackground");
        _window.setSize(display.getWidth(), display.getHeight());

        int buttonCount = 5;
        int buttonWidth = _window.getWidth() / 3;
        int buttonHeight = 32;
        int buttonSpacing = (int) (buttonHeight * 0.5);
        int totalButtonHeight = (buttonCount * buttonHeight) + ((buttonCount - 1) * buttonSpacing);
        int startPosY = totalButtonHeight + (_window.getHeight() - totalButtonHeight) / 2;
        int stepY = buttonHeight + buttonSpacing;
        int posX = (_window.getWidth() - buttonWidth) / 2;
        int buttonIndex = 0;

        _btnSave = new BButton("");
        _btnSave.setPreferredSize(buttonWidth, buttonHeight);
        _btnSave.setEnabled(false);
        _btnSave.addListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                _inGamePauseListener.savePressed();
            }
        });
        _window.add(_btnSave, new Point(posX, startPosY - (stepY * buttonIndex++)));

        _btnSaveAs = new BButton("");
        _btnSaveAs.setPreferredSize(buttonWidth, buttonHeight);
        _btnSaveAs.setEnabled(false);
        _btnSaveAs.addListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                _inGamePauseListener.saveAsPressed();
            }
        });
        _window.add(_btnSaveAs, new Point(posX, startPosY - (stepY * buttonIndex++)));

        _btnEditSettings = new BButton("");
        _btnEditSettings.setPreferredSize(buttonWidth, buttonHeight);
        _btnEditSettings.addListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                _inGamePauseListener.editSettingsPressed();
            }
        });
        _window.add(_btnEditSettings, new Point(posX, startPosY - (stepY * buttonIndex++)));

        _btnExitGame = new BButton("");
        _btnExitGame.setPreferredSize(buttonWidth, buttonHeight);
        _btnExitGame.addListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                _inGamePauseListener.exitGamePressed();
            }
        });
        _window.add(_btnExitGame, new Point(posX, startPosY - (stepY * buttonIndex++)));

        _btnResumeGame = new BButton("");
        _btnResumeGame.setPreferredSize(buttonWidth, buttonHeight);
        _btnResumeGame.addListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                _inGamePauseListener.resumeGamePressed();
            }
        });
        _window.add(_btnResumeGame, new Point(posX, startPosY - (stepY * buttonIndex++)));

        _window.center();
    }

    public void addInGamePauseListener(IInGamePauseListener inGamePauseListener) {
        _inGamePauseListener = inGamePauseListener;
    }

    public List<String> getTranslationTags() {
        ArrayList<String> translationTags = new ArrayList<String>();
        translationTags.add("InGamePause.Save");    // Save
        translationTags.add("InGamePause.SaveDescription");    // Save the current campaign
        translationTags.add("InGamePause.SaveAs");    // Save As...
        translationTags.add("InGamePause.SaveAsDescription");    // Save the current campaign under a different name
        translationTags.add("InGamePause.EditSettings");    // Settings
        translationTags.add("InGamePause.EditSettingsDescription");    // Edit game settings
        translationTags.add("InGamePause.Exit");    // Exit
        translationTags.add("InGamePause.ExitDescription");    // Exits this game
        translationTags.add("InGamePause.ResumeGame");    // Resume Game
        translationTags.add("InGamePause.ResumeGameDescription");    // Exit this menu and return to the game in progress

        return translationTags;
    }

    public void setTranslationPhrases(List<String> translationPhrases) {
        _btnSave.setText(translationPhrases.get(0));
        _btnSave.setTooltipText(translationPhrases.get(1));
        _btnSaveAs.setText(translationPhrases.get(2));
        _btnSaveAs.setTooltipText(translationPhrases.get(3));
        _btnEditSettings.setText(translationPhrases.get(4));
        _btnEditSettings.setTooltipText(translationPhrases.get(5));
        _btnExitGame.setText(translationPhrases.get(6));
        _btnExitGame.setTooltipText(translationPhrases.get(7));
        _btnResumeGame.setText(translationPhrases.get(8));
        _btnResumeGame.setTooltipText(translationPhrases.get(9));
    }
}
