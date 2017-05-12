package src.data.scripts.world;

import com.fs.starfarer.api.campaign.FactionAPI;

import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorGeneratorPlugin;

import com.fs.starfarer.api.impl.campaign.shared.SharedData;


public class NomadGen implements SectorGeneratorPlugin {
    
    @Override
    public void generate(SectorAPI sector) {
        initFactionRelationships(sector);
	SharedData.getData().getPersonBountyEventData().addParticipatingFaction("nomads");
        
       // new TheNomadsNur().generate(sector);
        
    }
    
    /*
    	public void handle_event( CampaignArmadaController.CampaignArmadaControllerEvent event )
	{
		// Oasis is not in play; put it for sale at the station (yay!)
		if( "NON_EXISTENT".equals( event.controller_state ))
		{
			// add no more than one Oasis
			int count = 0; // first count oasis ships (player could have bought one previously and sold it back)
			FleetDataAPI station_ships = station.getCargo().getMothballedShips();
			for( Iterator i = station_ships.getMembersInPriorityOrder().iterator(); i.hasNext(); )
			{
				FleetMemberAPI ship = (FleetMemberAPI)i.next();
				if( "nom_oasis".equals( ship.getHullId() ))
					++count;
			}
			if( count == 0 )
			{
				station_ships.addFleetMember( factory.createFleetMember( FleetMemberType.SHIP, "nom_oasis_standard" ));
				_.debug("added OASIS to station cargo");
			}
		}
		// Oasis is in play; be patient! T_T
		else if( "JOURNEYING_LIKE_A_BOSS".equals( event.controller_state ))
		{
			// remove all Oasis hulls, there's only supposed to be one, and it's cruising around.
			FleetDataAPI station_ships = station.getCargo().getMothballedShips();
			for( Iterator i = station_ships.getMembersInPriorityOrder().iterator(); i.hasNext(); )
			{
				FleetMemberAPI ship = (FleetMemberAPI)i.next();
				if( "nom_oasis".equals( ship.getHullId() ))
				{
					station_ships.removeFleetMember( ship );
					_.debug("removed OASIS from station cargo");
				}
			}
		}
	}
*/
    private static void initFactionRelationships(SectorAPI sector) {

        FactionAPI nomads_faction = sector.getFaction( "nomads" );
		Object[] all_factions = sector.getAllFactions().toArray();
		for( int i = 0; i < all_factions.length; ++i )
		{
			FactionAPI cur_faction = (FactionAPI) all_factions[i];
			if( cur_faction == nomads_faction )
				continue;
			if( cur_faction.getId() == "neutral" ||  cur_faction.getId() == "independent" )
			{
				nomads_faction.setRelationship( cur_faction.getId(), 1 );
			}
			else
			{
				nomads_faction.setRelationship( cur_faction.getId(), -1 );
			}
		}
		nomads_faction.setRelationship( "player", 0 );

    }
}
