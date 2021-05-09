package UC.KirchePlus.main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;


import UC.KirchePlus.Commands.Brot_Command;
import UC.KirchePlus.Commands.HV_Command;
import UC.KirchePlus.Commands.MarryRP_Command;
import UC.KirchePlus.Utils.MarryFile;
import UC.KirchePlus.Utils.TabellenMethoden;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@Mod(modid = main.MODID, version = main.VERSION)
public class main{
	
    public static final String MODID = "kirche+";
    public static final String VERSION = "1.2";
    
    public static ArrayList<String> HVList = new ArrayList<String>();
    public static ArrayList<String> BrotList = new ArrayList<String>();
    
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
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
