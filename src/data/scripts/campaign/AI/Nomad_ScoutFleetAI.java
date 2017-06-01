package src.data.scripts.campaign.AI;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.Script;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.FleetAssignment;
import src.data.scripts.campaign.CampaignArmada;

public class Nomad_ScoutFleetAI implements Script {

    private final int identity;
    private CampaignArmada armada;

    public Nomad_ScoutFleetAI(int index, CampaignArmada armada) {
        this.identity = index;
        this.armada = armada;
    }

    public void init() {
        armada.getEscortFleets()[this.identity].addAssignment(FleetAssignment.ORBIT_AGGRESSIVE, armada.getLeaderFleet(), 2f, this);
    }

    @Override
    public void run() {

        CampaignFleetAPI fleet = armada.getEscortFleets()[this.identity];
        CampaignFleetAPI chief = armada.getLeaderFleet();
        if (chief == null) {
            if (!armada.isGoDespawn()) {
                armada.despawn();

            }
            return;
        }
        if (chief.getCurrentAssignment() == null) {
            fleet.addAssignment(FleetAssignment.DEFEND_LOCATION, armada.getLeaderFleet(), 2f, this);
            Global.getSector().getCampaignUI().addMessage("DEFEND_LOCATION" + this.identity);

        }

        if (chief.getCurrentAssignment().getAssignment().equals(FleetAssignment.ORBIT_PASSIVE)) {
            if (identity >= 4) {
                Global.getSector().getCampaignUI().addMessage("PATROL_SYSTEM" + this.identity);
                fleet.addAssignment(FleetAssignment.PATROL_SYSTEM, armada.getLeaderFleet(), 2f, this);
            } else {
                Global.getSector().getCampaignUI().addMessage("ORBIT_AGGRESSIVE" + this.identity);
                fleet.addAssignment(FleetAssignment.ORBIT_AGGRESSIVE, armada.getLeaderFleet(), 2f, this);
            }
        }
        if (chief.getCurrentAssignment().getAssignment().equals(FleetAssignment.PATROL_SYSTEM)) {
            Global.getSector().getCampaignUI().addMessage("DEFEND_LOCATION" + this.identity);
            fleet.addAssignment(FleetAssignment.DEFEND_LOCATION, armada.getLeaderFleet(), 2f, this);
        }
    }

}
