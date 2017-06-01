package src.data.scripts.campaign.AI;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.Script;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.FleetAssignment;
import com.fs.starfarer.api.campaign.FleetDataAPI;
import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.impl.campaign.events.OfficerManagerEvent;
import com.fs.starfarer.api.impl.campaign.fleets.FleetFactory;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import com.fs.starfarer.api.util.WeightedRandomPicker;
import java.util.Random;
import src.data.scripts.campaign.CampaignArmada;

public class Nomad_SpecialCampaignFleetAI implements Script {

    private SectorEntityToken hideoutLocation;
    private SectorEntityToken home;
    private CampaignArmada armada;


    public Nomad_SpecialCampaignFleetAI(CampaignArmada armada) {
        this.armada=armada;
        this.home = Global.getSector().getEntityById("stationNom1");
        if (this.home == null) {
            Global.getSector().getEntityById("nur_C");
        }
    }

    public void init() {
        LocationAPI location = this.home.getContainingLocation();
        location.addEntity(armada.getLeaderFleet());
        armada.getLeaderFleet().setLocation(this.home.getLocation().x - 500, this.home.getLocation().y + 500);
        Global.getSector().getCampaignUI().addMessage("Launch:Go on 2 " + this.home.getId());
        armada.getLeaderFleet().addAssignment(FleetAssignment.ORBIT_PASSIVE, this.home, 5f, this);

        armada.setEscortFleets(spawnRoyalCommandFleet(), 0);
        armada.setEscortFleets(spawnRoyalGuardFleet(), 1);
        armada.setEscortFleets(spawnRoyalGuardFleet(), 2);
        armada.setEscortFleets(spawnAssassinFleet(), 3);
        armada.setEscortFleets(spawnScoutFleet(), 4);
        armada.setEscortFleets(spawnScoutFleet(), 5);

        for (CampaignFleetAPI escortFleet1 : armada.getEscortFleets()) {
            location.addEntity(escortFleet1);
            escortFleet1.setLocation(this.home.getLocation().x - 500, this.home.getLocation().y + 500);
        }

        new Nomad_ScoutFleetAI(0,armada).init();
        new Nomad_ScoutFleetAI(1,armada).init();
        new Nomad_ScoutFleetAI(2,armada).init();
        new Nomad_ScoutFleetAI(3,armada).init();
        new Nomad_ScoutFleetAI(4,armada).init();
        new Nomad_ScoutFleetAI(5,armada).init();

    }

    @Override
    public void run() {
       /* if (armada.getLeaderFleet().getCurrentAssignment() != null) {
            Global.getSector().getCampaignUI().addMessage("Ass " + armada.getLeaderFleet().getCurrentAssignment().getAssignment().name());

            if (armada.getLeaderFleet().getCurrentAssignment().getAssignment() == FleetAssignment.ORBIT_PASSIVE) {
                if (armada.isEscortAlive(4)) {
                    armada.getEscortFleets()[4].getCurrentAssignment().expire();
                    //armada.getEscortFleets()[4].getCurrentAssignment().getOnCompletion().run();
                }
                if (armada.isEscortAlive(5)) {
                    armada.getEscortFleets()[5].getCurrentAssignment().expire();
                    //armada.getEscortFleets()[4].getCurrentAssignment().getOnCompletion().run();
                }
                return;
            }
        }*/
        pickLocation();
        if (hideoutLocation == null) {
              hideoutLocation = Global.getSector().getEntityById("stationNom1");
        }
        Global.getSector().getCampaignUI().addMessage("Run:Go on 2 " + hideoutLocation.getId());
        armada.getLeaderFleet().getAI().doNotAttack(Global.getSector().getPlayerFleet(), 10000f);
        armada.getLeaderFleet().addAssignment(FleetAssignment.GO_TO_LOCATION, hideoutLocation, 10000f, this);
        armada.getLeaderFleet().addAssignment(FleetAssignment.ORBIT_PASSIVE, hideoutLocation, 5f, this);

    }

    private void pickLocation() {
        WeightedRandomPicker<StarSystemAPI> systemPicker = new WeightedRandomPicker<StarSystemAPI>();

        for (StarSystemAPI system : Global.getSector().getStarSystems()) {
            float weight = system.getPlanets().size();
            float mult = 0f;

            if (system.hasPulsar()) {
                continue;
            }

            if (system.hasTag(Tags.THEME_MISC_SKIP)) {
                mult = 1f;
            } else if (system.hasTag(Tags.THEME_MISC)) {
                mult = 3f;
            } else if (system.hasTag(Tags.THEME_RUINS)) {
                mult = 7f;
            } else if (system.hasTag(Tags.THEME_REMNANT_DESTROYED)) {
                mult = 3f;
            }

            if (mult <= 0) {
                continue;
            }
            float dist = system.getLocation().length();
            float distMult = Math.max(0, 50000f - dist);

            systemPicker.add(system, weight * mult * distMult);
        }

        StarSystemAPI system = systemPicker.pick();

        if (system != null) {
            WeightedRandomPicker<SectorEntityToken> picker = new WeightedRandomPicker<SectorEntityToken>();
            for (SectorEntityToken planet : system.getPlanets()) {
                if (planet.isStar()) {
                    continue;
                }
                if (planet.getMarket() != null
                        && !planet.getMarket().isPlanetConditionMarketOnly()) {
                    continue;
                }

                picker.add(planet);
            }
            hideoutLocation = picker.pick();
        }

    }

    private CampaignFleetAPI spawnRoyalCommandFleet() {
        CampaignFleetAPI fleet = Global.getFactory().createEmptyFleet("nomads", "Royal Command Fleet", true);

        FleetDataAPI data = fleet.getFleetData();

        FleetMemberAPI member = Global.getFactory().createFleetMember(FleetMemberType.SHIP, "nom_gila_monster_antibattleship");
        member.setShipName("Royal Commander Ship");
        member.setFlagship(true);

        PersonAPI person = OfficerManagerEvent.createOfficer(Global.getSector().getFaction("nomads"), 20);

        member.setCaptain(person);
        data.addFleetMember(member);

        this.addRandom(data,
                new String[]{
                    "nom_sandstorm_assault",
                    "nom_rattlesnake_assault",
                    "nom_scorpion_assault",
                    "nom_komodo_mk2_assault",
                    "nom_komodo_assault",
                    "nom_roadrunner_pursuit",
                    "nom_flycatcher_carrier",
                    "nom_death_bloom_strike",
                    "nom_yellowjacket_sniper"
                },
                new int[]{
                    3,
                    5,
                    2,
                    2,
                    5,
                    2,
                    2,
                    3,
                    4
                },
                28,
                180);

        FleetFactory.finishAndSync(fleet);
        return fleet;
    }

    private CampaignFleetAPI spawnScoutFleet() {
        CampaignFleetAPI fleet = Global.getFactory().createEmptyFleet("nomads", "Scout Fleet", true);

        FleetDataAPI data = fleet.getFleetData();

        FleetMemberAPI member = Global.getFactory().createFleetMember(FleetMemberType.SHIP, "nom_flycatcher_carrier");
        member.setShipName("Scout Leader Ship");
        member.setFlagship(true);

        PersonAPI person = OfficerManagerEvent.createOfficer(Global.getSector().getFaction("nomads"), 5);

        member.setCaptain(person);
        data.addFleetMember(member);

        this.addRandom(data,
                new String[]{
                    "nom_wurm_assault",
                    "nom_yellowjacket_sniper"
                },
                new int[]{
                    1,
                    1,},
                2,
                20);

        FleetFactory.finishAndSync(fleet);
        return fleet;
    }

    private CampaignFleetAPI spawnAssassinFleet() {
        CampaignFleetAPI fleet = Global.getFactory().createEmptyFleet("nomads", "Assassin Fleet", true);

        FleetDataAPI data = fleet.getFleetData();

        FleetMemberAPI member = Global.getFactory().createFleetMember(FleetMemberType.SHIP, "nom_death_bloom_strike");
        member.setShipName("Assissin Leader Ship");
        member.setFlagship(true);

        PersonAPI person = OfficerManagerEvent.createOfficer(Global.getSector().getFaction("nomads"), 10);

        member.setCaptain(person);
        data.addFleetMember(member);

        this.addRandom(data,
                new String[]{
                    "nom_roadrunner_pursuit",
                    "nom_death_bloom_strike",},
                new int[]{
                    1,
                    1
                },
                2,
                38);

        FleetFactory.finishAndSync(fleet);
        return fleet;
    }

    private CampaignFleetAPI spawnRoyalGuardFleet() {
        CampaignFleetAPI fleet = Global.getFactory().createEmptyFleet("nomads", "Royal Guard Fleet", true);

        FleetDataAPI data = fleet.getFleetData();

        FleetMemberAPI member = Global.getFactory().createFleetMember(FleetMemberType.SHIP, "nom_sandstorm_assault");
        member.setShipName("Royal Guard Ship");
        member.setFlagship(true);

        PersonAPI person = OfficerManagerEvent.createOfficer(Global.getSector().getFaction("nomads"), 15);

        member.setCaptain(person);
        data.addFleetMember(member);

        this.addRandom(data,
                new String[]{
                    "nom_rattlesnake_assault",
                    "nom_scorpion_assault",
                    "nom_komodo_mk2_assault",
                    "nom_komodo_assault",
                    "nom_roadrunner_pursuit",
                    "nom_flycatcher_carrier",
                    "nom_death_bloom_strike",
                    "nom_yellowjacket_sniper",
                    "nom_wurm_assault"
                },
                new int[]{
                    1,
                    2,
                    1,
                    3,
                    1,
                    2,
                    1,
                    4,
                    6
                },
                21,
                75);

        FleetFactory.finishAndSync(fleet);
        return fleet;
    }

    private void addRandom(FleetDataAPI data, String[] members, int[] weight, int weightsize, int fleetPoints) {

        int max = fleetPoints / 6;
        Random rander = new Random();
        int rand;
        int index;
        FleetMemberAPI ship;

        while (fleetPoints > max) {
            rand = rander.nextInt(weightsize);

            index = 0;
            while (rand > weight[index]) {
                rand -= weight[index];
                if (index == (weight.length - 1)) {
                    break;
                }
                index++;
            }
            ship = Global.getFactory().createFleetMember(FleetMemberType.SHIP, members[index]);
            fleetPoints -= ship.getFleetPointCost();
            if (fleetPoints < 0) {
                return;
            }
            data.addFleetMember(ship);
        }
    }
}
