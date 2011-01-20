package atechnique.views.interfaces;

import atechnique.Campaign;

import java.util.List;

public interface ISelectCampaignView extends IGameStateView {
    void addSelectCampaignListener(ISelectCampaignListener selectCampaignListener);

    void setCampaigns(List<Campaign> campaigns);

    void setCampaignDescription(String description);

    void updateCampaignState();
}
