package src.data.scripts.campaign;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.FleetDataAPI;
import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.impl.campaign.events.OfficerManagerEvent;
import com.fs.starfarer.api.impl.campaign.fleets.FleetFactory;
import java.util.Random;

public class Nomad_SpecialFactory {
    
    

    public CampaignFleetAPI spawnRoyalCommandFleet(CampaignFleetAPI chief) {
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
        
        this.putLocation(chief, fleet);
        return fleet;
    }

    public CampaignFleetAPI spawnScoutFleet(CampaignFleetAPI chief) {
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
              
        this.putLocation(chief, fleet);
        return fleet;
    }

    public CampaignFleetAPI spawnAssassinFleet(CampaignFleetAPI chief) {
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
              
        this.putLocation(chief, fleet);
        return fleet;
    }

    public CampaignFleetAPI spawnRoyalGuardFleet(CampaignFleetAPI chief) {
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
              
        this.putLocation(chief, fleet);
        return fleet;
    }

    private void putLocation(CampaignFleetAPI chief,CampaignFleetAPI newFleet) {
        LocationAPI location = chief.getContainingLocation();
        
        location.addEntity(newFleet);
        newFleet.setLocation(chief.getLocation().x, chief.getLocation().y);
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
