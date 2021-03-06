package data.scripts.plugins;

import java.util.ArrayList;
import java.util.List;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CargoAPI.CrewXPLevel;
import com.fs.starfarer.api.characters.CharacterCreationPlugin;
import com.fs.starfarer.api.characters.MutableCharacterStatsAPI;
import com.fs.starfarer.api.fleet.FleetMemberType;
import data.scripts.trylobot._;

public class TheNomadsCharacterCreationPlugin implements CharacterCreationPlugin
{
	CharacterCreationPlugin handoff_plugin = null;
	
	public TheNomadsCharacterCreationPlugin()
	{
		// automatic yield of control pass-through in case of conflict: 
		//   Exerelin
		if( _.can_be_loaded( "data.scripts.world.ExerelinGen" ))
		{
			try
			{
				handoff_plugin = (CharacterCreationPlugin) Global.getSettings().getScriptClassLoader()
				  .loadClass( "data.scripts.plugins.ExerelinCharacterCreationPluginImpl" )
				  .newInstance();
			}
			catch( ClassNotFoundException e ) {}
			catch( InstantiationException e ) {}
			catch( IllegalAccessException e ) {}
		}
		//   Uomoz's Sector: Journey
		if( _.can_be_loaded( "data.scripts.UsSModPlugin" ))
		{
			try
			{
				handoff_plugin = (CharacterCreationPlugin) Global.getSettings().getScriptClassLoader()
				  .loadClass( "data.scripts.plugins.CharacterCreationPluginImpl" )
				  .newInstance();
			}
			catch( ClassNotFoundException e ) {}
			catch( InstantiationException e ) {}
			catch( IllegalAccessException e ) {}
		}
	}
	
	public static class ResponseImpl implements Response
	{
		private String text;
		public ResponseImpl(String text)
		{
			this.text = text;
		}
		public String getText()
		{
			return text;
		}
	}
	
	// Not using an enum for this because Janino doesn't support it.
	// It does, apparently, support using enums defined elsewhere - just can't compile new ones.
	private ResponseImpl SUPPLY_OFFICER = new ResponseImpl("Served as a junior supply officer in an independent system's navy");
	private ResponseImpl GUNNER = new ResponseImpl("Hired out as a gunner on a mercenary ship");
	private ResponseImpl ENGINEER = new ResponseImpl("Found employment as an assistant engineer on an exploration vessel");
	private ResponseImpl COPILOT = new ResponseImpl("Spent time as a co-pilot on a patrol ship in an independent system");
	private ResponseImpl NOMADS1 = new ResponseImpl("Wandered the deserts of Naera, seeking inner peace");
	private ResponseImpl SOMETHING_ELSE_1 = new ResponseImpl("Did something else");
	
	private ResponseImpl ADJUTANT = new ResponseImpl("Served as an adjutant in the Hegemony Navy");
	private ResponseImpl QUARTERMASTER = new ResponseImpl("Performed the duties of a quartermaster on an independent warship");
	private ResponseImpl HELM = new ResponseImpl("Helmed a patrol ship operating in a backwater system");
	private ResponseImpl COMBAT_ENGINEER = new ResponseImpl("Took over the duties of chief combat engineer during a lengthy campaign");
	private ResponseImpl NOMADS2 = new ResponseImpl("Jury-rigged a rusty hulk and set out at random");
	private ResponseImpl SOMETHING_ELSE_2 = new ResponseImpl("Did something else");
	
	private int stage = 0;
	private String [] prompts = new String [] {
		"Early in your career, you...",
		"More recently, you...",
	};
	
	public String getPrompt()
	{
		if( handoff_plugin != null )
		{
			return handoff_plugin.getPrompt();
		}
		/////////////
		if (stage < prompts.length)
			return prompts[stage];
		return null;
	}

	@SuppressWarnings("unchecked")
	public List getResponses()
	{
		if( handoff_plugin != null )
		{
			return handoff_plugin.getResponses();
		}
		/////////////
		List result = new ArrayList();
		if (stage == 0) {
			result.add(SUPPLY_OFFICER);
			result.add(GUNNER);
			result.add(ENGINEER);
			result.add(COPILOT);
			result.add(NOMADS1);
			result.add(SOMETHING_ELSE_1);
		}
		else if (stage == 1)
		{
			result.add(ADJUTANT);
			result.add(QUARTERMASTER);
			result.add(HELM);
			result.add(COMBAT_ENGINEER);
			result.add(NOMADS2);
			result.add(SOMETHING_ELSE_2);
		}
		return result;
	}
	
	public void submit(Response response, CharacterCreationData data)
	{
		if( handoff_plugin != null )
		{
			handoff_plugin.submit( response, data );
			return;
		}
		/////////////
		if (stage == 0)
		{ // just in case
			data.addStartingShipChoice("shuttle_Attack");
			data.setStartingLocationName("Corvus");
			data.getStartingCoordinates().set(-2500, -3500);
			/*if (Global.getSettings().isDevMode())
			{
				data.addStartingShipChoice("tempest_Attack");
			}
			if (Global.getSettings().isDevMode() && false)
			{
				data.addStartingFleetMember("odyssey_Balanced", FleetMemberType.SHIP);
				data.addStartingFleetMember("enforcer_Elite", FleetMemberType.SHIP);
				data.addStartingFleetMember("venture_Balanced", FleetMemberType.SHIP);
				data.addStartingFleetMember("crig_Standard", FleetMemberType.SHIP);
				data.addStartingFleetMember("ox_Standard", FleetMemberType.SHIP);
				data.addStartingFleetMember("wasp_wing", FleetMemberType.FIGHTER_WING);
				data.addStartingFleetMember("wasp_wing", FleetMemberType.FIGHTER_WING);
				data.getStartingCargo().getCredits().add(50000f);
				data.getStartingCargo().addFuel(1000);
				data.getStartingCargo().addSupplies(800);
				data.getStartingCargo().addMarines(100);
				data.getStartingCargo().addCrew(CrewXPLevel.REGULAR, 800);
			}*/
		}
		
		stage++;
		
		MutableCharacterStatsAPI stats = data.getPerson().getStats();
		if (response == SUPPLY_OFFICER)
		{
			stats.increaseAptitude("leadership");
			stats.increaseSkill("fleet_logistics");
			stats.increaseSkill("command_experience");
			data.getStartingCargo().getCredits().add(3000f);
		}
		else if (response == GUNNER)
		{
			stats.increaseAptitude("combat");
			stats.increaseSkill("ordnance_expert");
			stats.increaseSkill("target_analysis");
			data.getStartingCargo().getCredits().add(3000f);
		}
		else if (response == ENGINEER)
		{
			stats.increaseAptitude("technology");
			stats.increaseSkill("field_repairs");
			stats.increaseSkill("mechanical_engineering");
			data.getStartingCargo().getCredits().add(3000f);
		}
		else if (response == COPILOT)
		{
			stats.increaseAptitude("combat");
			stats.increaseSkill("helmsmanship");
			stats.increaseSkill("evasive_action");
			data.getStartingCargo().getCredits().add(3000f);
		}
		else if (response == NOMADS1)
		{
			stats.increaseAptitude("technology");
			stats.increaseSkill("construction");
			stats.increaseSkill("field_repairs");
			data.getStartingCargo().getCredits().add(500f);
		}
		else if (response == SOMETHING_ELSE_1)
		{
			stats.addAptitudePoints(1);
			stats.addSkillPoints(2);
			data.getStartingCargo().getCredits().add(1000f);
		} 
		
		else if (response == ADJUTANT)
		{
			stats.increaseAptitude("leadership");
			stats.increaseSkill("fleet_logistics");
			stats.increaseSkill("advanced_tactics");
			data.addStartingShipChoice("lasher_Standard");
			data.getStartingCargo().getCredits().add(3000f);
		}
		else if (response == QUARTERMASTER)
		{
			stats.increaseAptitude("technology");
			stats.increaseSkill("navigation");
			stats.addSkillPoints(1);
			data.addStartingShipChoice("wolf_CS");
			data.getStartingCargo().getCredits().add(3000f);
		}
		else if (response == HELM)
		{
			stats.increaseAptitude("combat");
			stats.increaseSkill("helmsmanship");
			stats.increaseSkill("evasive_action");
			data.addStartingShipChoice("vigilance_Standard");
			data.getStartingCargo().getCredits().add(3000f);
		}
		else if (response == COMBAT_ENGINEER)
		{
			stats.increaseAptitude("combat");
			stats.increaseSkill("damage_control");
			stats.increaseSkill("flux_modulation");
			data.addStartingShipChoice("lasher_Standard");
			data.getStartingCargo().getCredits().add(3000f);
		}
		else if (response == NOMADS2)
		{
			stats.increaseAptitude("technology");
			stats.increaseSkill("construction");
			stats.increaseSkill("field_repairs");
			stats.addSkillPoints(1);
			data.addStartingShipChoice("nom_wurm_assault");
			data.getStartingCargo().getCredits().add(500f);
			data.setStartingLocationName("Hyperspace");
			//data.setStartingLocationName("Nur");
			data.getStartingCoordinates().set(18250, -800);
			//data.getStartingCoordinates().set(4000, 4000);
		}
		else if (response == SOMETHING_ELSE_2)
		{
			stats.addAptitudePoints(1);
			stats.addSkillPoints(2);
			data.addStartingShipChoice("hound_Assault");
			data.getStartingCargo().getCredits().add(1000f);
		}
	}

	public void startingShipPicked(String variantId, CharacterCreationData data)
	{
		if( handoff_plugin != null )
		{
			handoff_plugin.startingShipPicked( variantId, data );
			return;
		}
		/////////////
		MutableCharacterStatsAPI stats = data.getPerson().getStats();
		stats.addAptitudePoints(1);
		stats.addSkillPoints(2);
		
		if (variantId.equals("vigilance_Standard"))
		{
			data.getStartingCargo().addFuel(20);
			data.getStartingCargo().addSupplies(30);
			data.getStartingCargo().addCrew(CrewXPLevel.REGULAR, 20);
			data.getStartingCargo().addMarines(5);
		}
		else if (variantId.equals("lasher_Standard"))
		{
			data.getStartingCargo().addFuel(20);
			data.getStartingCargo().addSupplies(20);
			data.getStartingCargo().addCrew(CrewXPLevel.REGULAR, 40);
			data.getStartingCargo().addMarines(10);
		}
		else if (variantId.equals("wolf_CS"))
		{
			data.getStartingCargo().addFuel(20);
			data.getStartingCargo().addSupplies(20);
			data.getStartingCargo().addCrew(CrewXPLevel.REGULAR, 22);
			data.getStartingCargo().addMarines(7);
		} 
		else if (variantId.equals("shuttle_Attack"))
		{
			data.getStartingCargo().addFuel(10);
			data.getStartingCargo().addSupplies(10);
			data.getStartingCargo().addCrew(CrewXPLevel.REGULAR, 10);
			data.getStartingCargo().addMarines(3);
		}
		else if (variantId.equals("hound_Assault"))
		{
			data.getStartingCargo().addFuel(30);
			data.getStartingCargo().addSupplies(40);
			data.getStartingCargo().addCrew(CrewXPLevel.REGULAR, 25);
			data.getStartingCargo().addMarines(15);
		}
		else if (variantId.equals("tempest_Attack"))
		{
			data.getStartingCargo().addFuel(20);
			data.getStartingCargo().addSupplies(30);
			data.getStartingCargo().addCrew(CrewXPLevel.ELITE, 20);
			data.getStartingCargo().addMarines(0);
		}
		else if (variantId.equals("nom_wurm_assault"))
		{
			data.getStartingCargo().addFuel(50);
			data.getStartingCargo().addSupplies(50);
			data.getStartingCargo().addCrew(CrewXPLevel.ELITE, 1);
			data.getStartingCargo().addCrew(CrewXPLevel.VETERAN, 20);
			data.getStartingCargo().addMarines(0);
			/////////////////////////////////////////////////////////////
			if( Global.getSettings().isDevMode() )//&& false )
			{
				data.addStartingFleetMember("nom_oasis_standard", FleetMemberType.SHIP);
				data.addStartingFleetMember("nom_gila_monster_antibattleship", FleetMemberType.SHIP);
				data.addStartingFleetMember("nom_sandstorm_assault", FleetMemberType.SHIP);
				data.addStartingFleetMember("nom_rattlesnake_assault", FleetMemberType.SHIP);
				data.addStartingFleetMember("nom_komodo_mk2_assault", FleetMemberType.SHIP);
				data.addStartingFleetMember("nom_komodo_assault", FleetMemberType.SHIP);
				data.addStartingFleetMember("nom_roadrunner_pursuit", FleetMemberType.SHIP);
				data.addStartingFleetMember("nom_flycatcher_carrier", FleetMemberType.SHIP);
				data.addStartingFleetMember("nom_death_bloom_strike", FleetMemberType.SHIP);
				data.addStartingFleetMember("nom_yellowjacket_sniper", FleetMemberType.SHIP);
				data.addStartingFleetMember("nom_wurm_assault", FleetMemberType.SHIP);
				data.addStartingFleetMember("nom_fang_wing", FleetMemberType.FIGHTER_WING);
				data.addStartingFleetMember("nom_toad_wing", FleetMemberType.FIGHTER_WING);
				data.addStartingFleetMember("nom_iguana_wing", FleetMemberType.FIGHTER_WING);
				data.addStartingFleetMember("nom_scarab_wing", FleetMemberType.FIGHTER_WING);
				data.getStartingCargo().getCredits().add(50000f);
				data.getStartingCargo().addFuel(1000);
				data.getStartingCargo().addSupplies(2000);
				data.getStartingCargo().addMarines(100);
				data.getStartingCargo().addCrew(CrewXPLevel.REGULAR, 3500);
				stats.setAptitudeLevel( "leadership", 10.0f );
				stats.setSkillLevel( "fleet_logistics", 10.0f );
			}
		}
		else
		{
			data.getStartingCargo().addFuel(20);
			data.getStartingCargo().addSupplies(20);
			data.getStartingCargo().addCrew(CrewXPLevel.REGULAR, 20);
			data.getStartingCargo().addMarines(10);
		}
	}

}







