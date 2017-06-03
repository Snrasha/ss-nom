package src.data.scripts.campaign;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.GameState;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.FleetDataAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.impl.campaign.events.OfficerManagerEvent;
import com.fs.starfarer.api.impl.campaign.fleets.FleetFactory;
import com.fs.starfarer.api.util.IntervalUtil;
import java.util.Iterator;
import src.data.scripts.campaign.AI.Nomad_SpecialCampaignFleetAI;

public class Nomad_CampaignSpawnSpecialFleet implements EveryFrameScript {

    private final CampaignArmada armada = new CampaignArmada(3);
    private final IntervalUtil timerrespawn;
    private PersonAPI person;
    private boolean oneTime = false;
    private int cooldown = 0;

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

            if (armada.isDespawn()) {
                if (cooldown > 3) {
                    cooldown = 0;
                    retireOasis();
                    spawnFleet();
                }

                cooldown++;
            } else if (armada.getLeaderFleet() == null || !armada.getLeaderFleet().isAlive() || !armada.getLeaderFleet().getFlagship().getHullId().equals("nom_oasis")) {
                insertOasis();
                armada.despawn();
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
        armada.setLeaderFleet(fleet);
        Nomad_SpecialCampaignFleetAI nomad_scriptFleet = new Nomad_SpecialCampaignFleetAI(armada);
        nomad_scriptFleet.init();

    }

    private void insertOasis() {
        SectorEntityToken station = Global.getSector().getEntityById("stationNom1");
        if (station == null) {
            return;
        }

        // add no more than one Oasis
        int count = 0; // first count oasis ships (player could have bought one previously and sold it back)
        FleetDataAPI station_ships = station.getCargo().getMothballedShips();
        for (Iterator i = station_ships.getMembersInPriorityOrder().iterator(); i.hasNext();) {
            FleetMemberAPI ship = (FleetMemberAPI) i.next();
            if ("nom_oasis".equals(ship.getHullId())) {
                ++count;
            }
        }
        if (count == 0) {
            station_ships.addFleetMember(Global.getFactory().createFleetMember(FleetMemberType.SHIP, "nom_oasis_standard"));
        }

    }

    private void retireOasis() {
        SectorEntityToken station = Global.getSector().getEntityById("stationNom1");
        if (station == null) {
            return;
        }
        // remove all Oasis hulls, there's only supposed to be one, and it's cruising around.
        FleetDataAPI station_ships = station.getCargo().getMothballedShips();
        for (Iterator i = station_ships.getMembersInPriorityOrder().iterator(); i.hasNext();) {
            FleetMemberAPI ship = (FleetMemberAPI) i.next();
            if ("nom_oasis".equals(ship.getHullId())) {
                station_ships.removeFleetMember(ship);

            }
        }

    }
}
