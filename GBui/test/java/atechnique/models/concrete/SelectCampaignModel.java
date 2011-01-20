package atechnique.models.concrete;

import atechnique.Campaign;
import atechnique.models.interfaces.ISelectCampaignModel;

import java.util.List;

public class SelectCampaignModel implements ISelectCampaignModel {
    private List<Campaign> _campaigns;

    public SelectCampaignModel() {
        // TODO: compile list of available campaigns
    }

    public List<Campaign> getCampaigns() {
        return _campaigns;
    }
}
