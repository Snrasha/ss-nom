package data.missions.test6;

import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.mission.MissionDefinitionAPI;
import com.fs.starfarer.api.mission.MissionDefinitionPlugin;

public class MissionDefinition implements MissionDefinitionPlugin {

	public void defineMission(MissionDefinitionAPI api) {

		// Set up the fleets
		api.initFleet(FleetSide.PLAYER, "HSS", FleetGoal.DEFEND, false);
		api.initFleet(FleetSide.ENEMY, "TTS", FleetGoal.DEFEND, true);

		// Set up the player's fleet
		api.addToFleet(FleetSide.PLAYER, "nom_scarab_wing", FleetMemberType.FIGHTER_WING, false); // 3
		api.addToFleet(FleetSide.PLAYER, "nom_scorpion", FleetMemberType.SHIP, false); // 7

		// Set up the enemy fleet
		api.addToFleet(FleetSide.ENEMY, "talon_wing", FleetMemberType.FIGHTER_WING, false); // 3
		api.addToFleet(FleetSide.ENEMY, "talon_wing", FleetMemberType.FIGHTER_WING, false); // 3
		api.addToFleet(FleetSide.ENEMY, "talon_wing", FleetMemberType.FIGHTER_WING, false); // 3
		api.addToFleet(FleetSide.ENEMY, "hound_Assault", FleetMemberType.SHIP, false); // 3
		api.addToFleet(FleetSide.ENEMY, "wolf_Assault", FleetMemberType.SHIP, false); // 5

		// Set up the map.
		float width = 6500f;
		float height = 4000f;
		api.initMap((float)-width/2f, (float)width/2f, (float)-height/2f, (float)height/2f);
	}

}





