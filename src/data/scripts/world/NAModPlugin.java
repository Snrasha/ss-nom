package src.data.scripts.world;

import com.fs.starfarer.api.BaseModPlugin;  
import com.fs.starfarer.api.Global;  


public class NAModPlugin extends BaseModPlugin  
{  
   private static void initNA()  
   {  
     /*   boolean haveNexerelin = Global.getSettings().getModManager().isModEnabled("nexerelin");  
      if (!haveNexerelin || SectorManager.getCorvusMode())  */
         new NomadGen().generate(Global.getSector());  
   }  
  
   @Override  
   public void onNewGame()    
   {  
      initNA();  
   }  
}  
