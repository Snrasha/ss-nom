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

    private FactoryAPI factory;

    private SectorAPI sector;
    private LocationAPI hyper;

    private StarSystemAPI system;
    private SectorEntityToken system_center_of_mass;
    private PlanetAPI star_A;
    private PlanetAPI star_B;
    private PlanetAPI planet_I;
    private PlanetAPI planet_I__moon_a;
    private PlanetAPI planet_I__moon_b;
    private PlanetAPI planet_I__moon_c;
    private PlanetAPI planet_I__moon_d;
    private PlanetAPI planet_I__moon_e;
    private PlanetAPI planet_I__moon_f;

    private SectorEntityToken station;

    public void generate(SectorAPI sector) {
        // globals
        this.sector = sector;
        factory = Global.getFactory();
        hyper = sector.getHyperspace();

        system = sector.createStarSystem("Nur");
        system.setBackgroundTextureFilename("graphics/backgrounds/background4.jpg");

        system.setLightColor(new Color(185, 185, 240)); // light color in entire system, affects all entities
        system.getLocation().set(18000f, -900f);

        // stars, planets, moons, jump points
        init_celestial_bodies(system);

        // spawners and other fleet related scripts
        // faction relationships
    }

    private static final float star_jump_dist_factor_min = 0.8f;
    private static final float star_jump_dist_factor_max = 1.2f;

    private void init_celestial_bodies(StarSystemAPI system) {
        // stars, planets and moons
        system_center_of_mass = system.createToken(0f, 0f);
        //PlanetAPI star = system.initStar("nur", StarTypes.NEUTRON_STAR, 50f, 40f);
        star_A = system.addPlanet("nur_A", system_center_of_mass, "Nur-A", StarTypes.BLUE_GIANT, 90f, 1000f, 1500f, 30f);
        star_B = system.addPlanet("nur_B", system_center_of_mass, "Nur-B", StarTypes.RED_GIANT, 270f, 300f, 600f, 30f);
        planet_I = system.addPlanet("nur_C", system_center_of_mass, "Naera", "desert", 45f, 300f, 8000f, 199f);
        planet_I__moon_a = system.addPlanet("nur_D", planet_I, "Ixaith", "rocky_unstable", 0f, 60f, 800f, 67f);
        planet_I__moon_b = system.addPlanet("nur_E", planet_I, "Ushaise", "rocky_ice", 45f, 45f, 1000f, 120f);
        planet_I__moon_c = system.addPlanet("nur_F", planet_I, "Riaze", "barren", 90f, 100f, 1200f, 130f);
        planet_I__moon_d = system.addPlanet("nur_G", planet_I, "Riaze-Tremn", "frozen", 135f, 35f, 1500f, 132f);
        planet_I__moon_e = system.addPlanet("nur_H", planet_I, "Eufariz", "frozen", 180f, 65f, 1750f, 200f);
        planet_I__moon_f = system.addPlanet("nur_L", planet_I, "Thumn", "rocky_ice", 225f, 100f, 2000f, 362f);

        // specs
        planet_I.getSpec().setAtmosphereColor(new Color(160, 110, 45, 140));
        planet_I.getSpec().setCloudColor(new Color(255, 255, 255, 23));
        planet_I.getSpec().setTilt(15);
        planet_I.applySpecChanges();

        // stations
        station = system.addOrbitalStation("stationNom1", planet_I__moon_e, 180f, 300f, 50, "Naeran Orbital Storage & Resupply", "nomads");

        // rings & bands
        system.addRingBand(planet_I, "misc", "rings1", 256f, 0, Color.white, 256f, 630f, 30f);

        JumpPointAPI jumpPoint = Global.getFactory().createJumpPoint("Jump Point Alpha", "Jump Point Alpha");
        OrbitAPI orbit = Global.getFactory().createCircularOrbit(system_center_of_mass, 0f, 500f, 30f);
        jumpPoint.setOrbit(orbit);
        jumpPoint.setRelatedPlanet(planet_I);
        jumpPoint.setStandardWormholeToHyperspaceVisual();
        system.addEntity(jumpPoint);

        planet_I.setCustomDescriptionId("nom_planet_naera");
        system.autogenerateHyperspaceJumpPoints(true, true);

    }

}
