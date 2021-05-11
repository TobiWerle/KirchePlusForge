package UC.KirchePlus.main;

import UC.KirchePlus.Commands.Brot_Command;
import UC.KirchePlus.Commands.HV_Command;
import UC.KirchePlus.Commands.MarryRP_Command;
import UC.KirchePlus.Utils.MarryFile;
import UC.KirchePlus.Utils.TabellenMethoden;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = main.MODID, version = main.VERSION)
public class main{
	
    public static final String MODID = "kirche+";
    public static final String VERSION = "1.2";
    
    @EventHandler
    public void init(FMLInitializationEvent event){
    	TabellenMethoden.init();
    	MarryFile.load();
    	try {
    		TabellenMethoden.getHVList();
    		TabellenMethoden.getBrotList();
		} catch (Exception e) {}
    }
    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
    	ClientCommandHandler.instance.registerCommand(new HV_Command());
    	ClientCommandHandler.instance.registerCommand(new MarryRP_Command());
    	ClientCommandHandler.instance.registerCommand(new Brot_Command());
    }
}
