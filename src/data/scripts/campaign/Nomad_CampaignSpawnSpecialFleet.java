package src.data.scripts.campaign;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.GameState;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.FleetDataAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.impl.campaign.events.OfficerManagerEvent;
import com.fs.starfarer.api.impl.campaign.fleets.FleetFactory;
import com.fs.starfarer.api.util.IntervalUtil;
import src.data.scripts.campaign.AI.Nomad_SpecialCampaignFleetAI;

public class Nomad_CampaignSpawnSpecialFleet implements EveryFrameScript {

    public static final CampaignArmada armada = new CampaignArmada(6);
    private final IntervalUtil timerrespawn;
    private PersonAPI person;
    private boolean oneTime = false;

    public Nomad_CampaignSpawnSpecialFleet() {

        this.person = OfficerManagerEvent.createOfficer(Global.getSector().getFaction("nomads"), 20);
        // this.timerMonth = Global.getSector().getClock().getMonth(); // Launched the next month
        timerrespawn = new IntervalUtil(3, 3);
        oneTime = false;

    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public boolean runWhilePaused() {
        return false;
    }

    @Override
    public void advance(float amount) {
        SectorAPI sector = Global.getSector();
        if (Global.getCurrentState() != GameState.CAMPAIGN) {
            return;
        }
        if (sector.isPaused()) {
            return;
        }
        if (!oneTime) {
            oneTime = true;
            spawnFleet();
        }
        this.timerrespawn.advance(Global.getSector().getClock().convertToMonths(amount));

        if (this.timerrespawn.intervalElapsed()) {
            if (Nomad_CampaignSpawnSpecialFleet.armada.getLeaderFleet() == null) {
                
                spawnFleet();
            } else if (!Nomad_CampaignSpawnSpecialFleet.armada.getLeaderFleet().isAlive() || !Nomad_CampaignSpawnSpecialFleet.armada.getLeaderFleet().getFlagship().getHullId().equals("nom_oasis")) {
                Nomad_CampaignSpawnSpecialFleet.armada.despawn();
            }
            if (Nomad_CampaignSpawnSpecialFleet.armada.getLeaderFleet() == null) {
                spawnFleet();
            }

        }

    }

    /**
     * No random ship
     */
    private void spawnFleet() {
        CampaignFleetAPI fleet = Global.getFactory().createEmptyFleet("nomads", "Colony Fleet", true);

        FleetDataAPI data = fleet.getFleetData();

        FleetMemberAPI member = Global.getFactory().createFleetMember(FleetMemberType.SHIP, "nom_oasis_standard");
        member.setShipName("Oasis");
        member.setFlagship(true);

        if (person == null) {
            this.person = OfficerManagerEvent.createOfficer(Global.getSector().getFaction("nomads"), 20);
        }
        member.setCaptain(person);
        data.addFleetMember(member);

        String[] members = {
            "nom_sandstorm_assault",
            "nom_sandstorm_assault",
            "nom_rattlesnake_assault",
            "nom_rattlesnake_assault",
            "nom_komodo_mk2_assault",
            "nom_komodo_mk2_assault",
            "nom_flycatcher_carrier",
            "nom_flycatcher_carrier",};

        for (String ship : members) {
            data.addFleetMember(Global.getFactory().createFleetMember(FleetMemberType.SHIP, ship));
        }

        FleetFactory.finishAndSync(fleet);
        Nomad_CampaignSpawnSpecialFleet.armada.setLeaderFleet(fleet);
        Nomad_SpecialCampaignFleetAI nomad_scriptFleet = new Nomad_SpecialCampaignFleetAI(Nomad_CampaignSpawnSpecialFleet.armada);
        nomad_scriptFleet.init();

    }


}
