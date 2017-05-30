package src.data.scripts.campaign;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.FleetAssignment;
import com.fs.starfarer.api.campaign.SectorEntityToken;

public class CampaignArmada {

    private CampaignFleetAPI leaderFleet;
    private final CampaignFleetAPI[] escortFleet;
    private SectorEntityToken home;

    public CampaignArmada(int sizeEscort) {
        this.escortFleet = new CampaignFleetAPI[sizeEscort];
        this.home = Global.getSector().getEntityById("stationNom1");
        if (this.home == null) {
            Global.getSector().getEntityById("nur_C");
        }
    }

    public CampaignFleetAPI getLeaderFleet() {
        return this.leaderFleet;
    }

    public void setLeaderFleet(CampaignFleetAPI leaderFleet) {
        this.leaderFleet = leaderFleet;
    }

    public CampaignFleetAPI[] getEscortFleets() {
        return this.escortFleet;
    }

    public void setEscortFleets(CampaignFleetAPI fleet, int index) {
        if (index >= this.escortFleet.length) {
            return;
        }

        this.escortFleet[index] = fleet;
    }

    public void despawn() {

        if (home == null) {
            if (this.leaderFleet != null) {
                this.leaderFleet.despawn();
            }
            for (CampaignFleetAPI escortFleet1 : this.escortFleet) {
                if (escortFleet1 != null) {
                    escortFleet1.despawn();
                }
            }
        }
        if (this.leaderFleet != null) {
            this.leaderFleet.clearAssignments();
            this.leaderFleet.addAssignment(FleetAssignment.GO_TO_LOCATION_AND_DESPAWN, this.home, 1000f);
        }
        for (CampaignFleetAPI escortFleet1 : this.escortFleet) {
            if (escortFleet1 != null) {
                escortFleet1.clearAssignments();
                escortFleet1.addAssignment(FleetAssignment.GO_TO_LOCATION_AND_DESPAWN, this.home, 1000f);
            }
        }
    }

    public boolean isEscortAlive(int index) {
        if (index >= this.escortFleet.length) {
            return true;
        }

        return this.escortFleet[index].isAlive();
    }
}
