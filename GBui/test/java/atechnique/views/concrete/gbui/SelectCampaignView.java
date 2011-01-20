package atechnique.views.concrete.gbui;

import atechnique.Campaign;
import atechnique.views.interfaces.ISelectCampaignListener;
import atechnique.views.interfaces.ISelectCampaignView;
import com.jmex.bui.BButton;
import com.jmex.bui.BLabel;
import com.jmex.bui.BWindow;
import com.jmex.bui.BuiSystem;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.ActionListener;
import com.jmex.bui.layout.AbsoluteLayout;
import com.jmex.bui.util.Point;

import java.util.ArrayList;
import java.util.List;

public class SelectCampaignView extends GbuiGameState implements ISelectCampaignView {
    private BLabel _lblSelectCampaign;
//	private BList _lstCampaigns;
//	private TextArea _txtCampaignDescription;
//	private Panel _campaignProgression;
    //	private EditorPane _txtScenarioDescription;
    private BButton _btnCancel;
    private BButton _btnContinueGame;
    private BButton _btnStartNewGame;

    private ISelectCampaignListener _selectCampaignListener;

    public SelectCampaignView() {
        super("SelectCampaignView");

        _window = new BWindow(BuiSystem.getStyle(), new AbsoluteLayout());
        _window.setSize(700, 500);

        // Select Campaign label
        int width = _window.getWidth() - 40;
        int height = 16;
        _lblSelectCampaign = new BLabel("");
        _lblSelectCampaign.setPreferredSize(width, height);
        _window.add(_lblSelectCampaign, new Point(20, _window.getHeight() - 20));

        // List of available campaigns
//		_lstCampaigns = new BList();
//		_lstCampaigns.setSelectionMode(ListSelectionModel.SingleSelection);
//		_lstCampaigns.addListSelectionListener(new ListSelectionListener() {
//			@Override
//			public void valueChanged(ListSelectionEvent e) {
////				_selectedCampaign = (Campaign) _lstCampaigns.getSelectedValue();
//				_selectCampaignListener.campaignSelected((Campaign)_lstCampaigns.getSelectedValue());
//			}
//		});
//		_lstCampaigns.setVisibleRowCount(4);
//
//        sizeX = 180;
//        sizeY = _contentPane.getHeight() / 7.5f;
//		ScrollPane listScrollPane = _contentPane.createScrollPane("SelectCampaign.Campaigns", _lstCampaigns, sizeX, sizeY);
//		listScrollPane.setVerticalScrollBarPolicy(ScrollPane.VerticalScrollbarAlways);
//		posX = sizeX / 2 + 10;
//		posY = 410;
//		listScrollPane.setCenter(posX, posY);
//
//		// Campaign Description
//		sizeX = _contentPane.getWidth() - listScrollPane.getWidth() - 40;
//		// sizeY will remain the same as it was for the list of campaigns
//		_txtCampaignDescription = new TextArea();
//		_txtCampaignDescription.setEditable(false);
//		_txtCampaignDescription.setLineWrap(true);
//		_txtCampaignDescription.setWrapStyleWord(true);
//		ScrollPane campaignDescriptionScrollPane = _contentPane.createScrollPane("SelectCampaign.CampaignDescription", _txtCampaignDescription, sizeX, sizeY);
//		campaignDescriptionScrollPane.setVerticalScrollBarPolicy(ScrollPane.VerticalScrollbarAlways);
//		posX = _contentPane.getWidth() - 20 - (campaignDescriptionScrollPane.getWidth() / 2);
//		// posY will be the same as the list of campaigns
//		campaignDescriptionScrollPane.setCenter(posX, posY);
//
//		// Campaign flow tree
//		sizeX = _contentPane.getWidth() - 40;
//		sizeY = 201;
//		_campaignProgression = _contentPane.createPanel("SelectCampaign.CampaignProgression", sizeX, sizeY);
//		posX = _contentPane.getWidth() / 2;
//		posY = 256.5f;
//		_campaignProgression.setCenter(posX, posY);
//
//		// Scenario Description
//		// sizeX will be the same as the CampaignProgression
//		sizeY = 80.0f;
//		_txtScenarioDescription = new EditorPane();
//		_txtScenarioDescription.setEditable(false);
//		ScrollPane scenarioDescriptionScrollPane = _contentPane.createScrollPane("SelectCampaign.ScenarioDescription", _txtScenarioDescription, sizeX, sizeY);
//		scenarioDescriptionScrollPane.setVerticalScrollBarPolicy(ScrollPane.VerticalScrollbarAsNeeded);
//
//		// Cancel
        width = (_window.getWidth() / 6) - 10;
        height = 32;
        _btnCancel = new BButton("");
        _btnCancel.setPreferredSize(width, height);
        _btnCancel.addListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                _selectCampaignListener.cancelPressed();
            }
        });
        _window.add(_btnCancel, new Point(10, 20));

        _btnContinueGame = new BButton("");
        _btnContinueGame.setPreferredSize(width * 2, height);
        _btnContinueGame.setEnabled(false);
        _btnContinueGame.addListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                // TODO Auto-generated method stub
            }
        });
        _window.add(_btnContinueGame, new Point(_window.getWidth() - (10 * 2) - (width * 4), 20));

        _btnStartNewGame = new BButton("");
        _btnStartNewGame.setPreferredSize(width * 2, height);
        _btnStartNewGame.addListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                _selectCampaignListener.startNewGamePressed();
            }
        });
        _window.add(_btnStartNewGame, new Point(_window.getWidth() - 10 - (width * 2), 20));

        _window.center();
    }

    public void addSelectCampaignListener(ISelectCampaignListener selectCampaignListener) {
        _selectCampaignListener = selectCampaignListener;
    }

    public void setCampaignDescription(String description) {
//		_txtCampaignDescription.setText(description);
    }

    public void setCampaigns(List<Campaign> campaigns) {
//		DefaultListModel listModel = new DefaultListModel();
//		for (Campaign campaign : campaigns) {
//			listModel.addElement(campaign);
//		}
//		_lstCampaigns.setModel(listModel);

        // Select first in list
//		_lstCampaigns.setSelectedIndex(0);
    }

    public void updateCampaignState() {
    }

    public List<String> getTranslationTags() {
        ArrayList<String> translationTags = new ArrayList<String>();
        translationTags.add("SelectCampaign.SelectCampaign"); // Select Campaign
        translationTags.add("SelectCampaign.Cancel"); // Cancel
        translationTags.add("SelectCampaign.CancelDescription"); // Back to Main Menu
        translationTags.add("SelectCampaign.ContinueGame"); // Continue Game
        translationTags.add("SelectCampaign.ContinueGameDescription"); // Continue saved game
        translationTags.add("SelectCampaign.StartNewGame"); // Start New Game
        translationTags.add("SelectCampaign.StartNewGameDescription"); // Start selected campaign

        return translationTags;
    }

    public void setTranslationPhrases(List<String> translationPhrases) {
        _lblSelectCampaign.setText(translationPhrases.get(0));
        _btnCancel.setText(translationPhrases.get(1));
        _btnCancel.setTooltipText(translationPhrases.get(2));
        _btnContinueGame.setText(translationPhrases.get(3));
        _btnContinueGame.setTooltipText(translationPhrases.get(4));
        _btnStartNewGame.setText(translationPhrases.get(5));
        _btnStartNewGame.setTooltipText(translationPhrases.get(6));
    }
}
