package src.data.scripts.world;

import com.fs.starfarer.api.BaseModPlugin;  
import com.fs.starfarer.api.Global;  
import java.lang.reflect.InvocationTargetException;  
import java.lang.reflect.Method;  

public class NAModPlugin extends BaseModPlugin  
{  
   private static void initNA()  
   {  
      try {  
         //Got Exerelin, so load Exerelin    
         Class<?> def = Global.getSettings().getScriptClassLoader().loadClass("exerelin.campaign.SectorManager");    
         Method method;    
         try {      
                method = def.getMethod("getCorvusMode");      
                Object result = method.invoke(def);      
                if ((Boolean)result == true)      
                {  
                    // Exerelin running in Corvus mode, go ahead and generate our sector      
                    new NomadGen().generate(Global.getSector()); 
                }  
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {    // check failed, do nothing      
            }    
             
      } catch (ClassNotFoundException ex) {  
         // Exerelin not found so continue and run normal generation code    
         new NomadGen().generate(Global.getSector());
      }    
   }  
  
   @Override  
   public void onNewGame()    
   {  
      initNA();  
   }  
}  
