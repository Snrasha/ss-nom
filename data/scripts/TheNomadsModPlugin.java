package data.scripts;
import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SectorAPI;
import data.scripts.world.TheNomadsNur;

public class TheNomadsModPlugin extends BaseModPlugin
{
	@Override
	public void onNewGame()
	{
		init();
	}

	private void init()
	{
		SectorAPI sector = Global.getSector();
		new TheNomadsNur().generate( sector );
	}
}