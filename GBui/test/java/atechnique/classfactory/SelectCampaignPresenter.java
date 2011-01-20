package atechnique.classfactory;

import atechnique.Campaign;
import atechnique.CampaignScenario;
import atechnique.game.state.GameManager;
import atechnique.models.interfaces.ICampaignChangedListener;
import atechnique.models.interfaces.ISelectCampaignModel;
import atechnique.views.interfaces.ISelectCampaignListener;
import atechnique.views.interfaces.ISelectCampaignView;

import java.util.List;

public class SelectCampaignPresenter extends GameStatePresenter implements ISelectCampaignListener, ICampaignChangedListener {
    private ISelectCampaignView _view;
    private ISelectCampaignModel _model;

    public SelectCampaignPresenter(ISelectCampaignView view, ISelectCampaignModel model) {
        super(view);

        _view = view;
        _model = model;

        List<Campaign> campaigns = _model.getCampaigns();
        if (campaigns != null) {
            for (Campaign campaign : campaigns) {
                campaign.addCampaignChangedListener(this);
            }
        }

        _view.addSelectCampaignListener(this);
        _view.setCampaigns(campaigns);
    }

    @Override
    public void campaignSelected(Campaign selectedCampaign) {
        ClassFactory.setCampaign(selectedCampaign);
        _view.setCampaignDescription(ClassFactory.getCampaign().getDescription());
        _view.updateCampaignState();
    }

    @Override
    public void scenarioSelected(CampaignScenario scenario) {
        ClassFactory.getCampaign().setSelectedScenario(scenario);
    }

    @Override
    public void cancelPressed() {
        GameManager.getInstance().changeState(ClassFactory.getMainMenuPresenter());
    }

    @Override
    public void startNewGamePressed() {
        GameManager.getInstance().pushState(ClassFactory.getStartServerPresenter());
    }

    @Override
    public void stateChanged() {
        _view.updateCampaignState();
    }
}
