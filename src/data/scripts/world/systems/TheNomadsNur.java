package src.data.scripts.world.systems;

import com.fs.starfarer.api.FactoryAPI;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.JumpPointAPI;
import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.campaign.OrbitAPI;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.impl.campaign.ids.StarTypes;

import java.awt.Color;

public class TheNomadsNur {


    public void generate(SectorAPI sector) {
  
 
        StarSystemAPI system =  sector.createStarSystem("Nur");
        system.setBackgroundTextureFilename("graphics/backgrounds/background4.jpg");

        system.setLightColor(new Color(185, 185, 240)); // light color in entire system, affects all entities
        system.getLocation().set(18000f, -900f);
        LocationAPI hyper = Global.getSector().getHyperspace();

       //SectorEntityToken system_center_of_mass = system.createToken(0f, 0f);
        PlanetAPI star = system.initStar("nur", StarTypes.NEUTRON_STAR, 50f, 40f);
         system.addPlanet("nur_A", star, "Nur-A", StarTypes.BLUE_GIANT, 90f, 1000f, 1500f, 30f);
         system.addPlanet("nur_B", star, "Nur-B", StarTypes.RED_GIANT, 270f, 300f, 600f, 30f);
        PlanetAPI planet_I = system.addPlanet("nur_C", star, "Naera", "desert", 45f, 300f, 8000f, 199f);
        system.addPlanet("nur_D", planet_I, "Ixaith", "rocky_unstable", 0f, 60f, 800f, 67f);
       system.addPlanet("nur_E", planet_I, "Ushaise", "rocky_ice", 45f, 45f, 1000f, 120f);
        system.addPlanet("nur_F", planet_I, "Riaze", "barren", 90f, 100f, 1200f, 130f);
       system.addPlanet("nur_G", planet_I, "Riaze-Tremn", "frozen", 135f, 35f, 1500f, 132f);
       PlanetAPI planet_I__moon_e = system.addPlanet("nur_H", planet_I, "Eufariz", "frozen", 180f, 65f, 1750f, 200f);
        system.addPlanet("nur_L", planet_I, "Thumn", "rocky_ice", 225f, 100f, 2000f, 362f);

        // specs
        planet_I.getSpec().setAtmosphereColor(new Color(160, 110, 45, 140));
        planet_I.getSpec().setCloudColor(new Color(255, 255, 255, 23));
        planet_I.getSpec().setTilt(15);
        planet_I.applySpecChanges();

        // stations
        SectorEntityToken station = system.addOrbitalStation("stationNom1", planet_I__moon_e, 180f, 300f, 50, "Naeran Orbital Storage & Resupply", "nomads");
       station.setCircularOrbitPointingDown(system.getEntityById("nur_H"), 45, 300, 50);

        // rings & bands
        system.addRingBand(planet_I, "misc", "rings_asteroids0", 256f, 0, Color.white, 256f, 630f, 30f);

        JumpPointAPI jumpPoint = Global.getFactory().createJumpPoint("jump_point_alpha", "Jump Point Alpha");
        OrbitAPI orbit = Global.getFactory().createCircularOrbit(star, 0f, 500f, 30f);
        jumpPoint.setOrbit(orbit);
        jumpPoint.setRelatedPlanet(planet_I);
        jumpPoint.setStandardWormholeToHyperspaceVisual();
        system.addEntity(jumpPoint);

        planet_I.setCustomDescriptionId("nom_planet_naera");
       
        system.autogenerateHyperspaceJumpPoints(true,true);
    }

}
