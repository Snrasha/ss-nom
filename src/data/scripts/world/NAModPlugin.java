package src.data.scripts.world;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import src.data.scripts.campaign.Nomad_CampaignSpawnSpecialFleet;

public class NAModPlugin extends BaseModPlugin {

    private static void initNA() {
      boolean haveNexerelin = Global.getSettings().getModManager().isModEnabled("nexerelin");  
      if (!haveNexerelin)  
        new NomadGen().generate(Global.getSector());
    }

    public static void scripts() {
        if (!Global.getSector().hasScript(Nomad_CampaignSpawnSpecialFleet.class)) {
            Nomad_CampaignSpawnSpecialFleet eventStarter = new Nomad_CampaignSpawnSpecialFleet();
            Global.getSector().addScript(eventStarter);
        }
    }

    @Override
    public void onNewGame() {
        initNA();
        scripts();
    }
}
