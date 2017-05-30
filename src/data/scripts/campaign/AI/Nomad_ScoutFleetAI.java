package src.data.scripts.campaign.AI;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.Script;
import com.fs.starfarer.api.campaign.FleetAssignment;
import src.data.scripts.campaign.CampaignArmada;
import src.data.scripts.campaign.Nomad_CampaignSpawnSpecialFleet;

public class Nomad_ScoutFleetAI implements Script {

    private final int identity;

    public Nomad_ScoutFleetAI(int index) {
        this.identity = index;
    }

    public void init() {
       Nomad_CampaignSpawnSpecialFleet.armada.getEscortFleets()[this.identity].addAssignment(FleetAssignment.FOLLOW, Nomad_CampaignSpawnSpecialFleet.armada.getLeaderFleet(), 10000f, this);
    }

    @Override
    public void run() {
        Global.getSector().getCampaignUI().addMessage("Assi scout" + this.identity);
       Nomad_CampaignSpawnSpecialFleet.armada.getEscortFleets()[this.identity].addAssignment(FleetAssignment.PATROL_SYSTEM,Nomad_CampaignSpawnSpecialFleet.armada.getLeaderFleet(), 10f);
        Nomad_CampaignSpawnSpecialFleet.armada.getEscortFleets()[this.identity].addAssignment(FleetAssignment.FOLLOW, Nomad_CampaignSpawnSpecialFleet.armada.getLeaderFleet(), 10000f);
    }

}
