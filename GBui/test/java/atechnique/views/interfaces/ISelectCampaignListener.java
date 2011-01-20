package atechnique.views.interfaces;

import atechnique.Campaign;
import atechnique.CampaignScenario;

public interface ISelectCampaignListener {
    void campaignSelected(Campaign selectedCampaign);

    void scenarioSelected(CampaignScenario scenario);

    void cancelPressed();

    void startNewGamePressed();
}
