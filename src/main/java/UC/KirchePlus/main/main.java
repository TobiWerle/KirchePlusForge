package UC.KirchePlus.main;

import UC.KirchePlus.Commands.HV_Command;
import UC.KirchePlus.Commands.MarryRP_Command;
import UC.KirchePlus.Commands.checkDonations;
import UC.KirchePlus.Config.KircheConfig;
import UC.KirchePlus.Utils.MarryFile;
import UC.KirchePlus.Utils.SpenderInfo;
import UC.KirchePlus.Utils.TabellenMethoden;
import fr.harmonia.tsclientquery.TSClientQuery;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.ArrayList;

@Mod(modid = main.MODID, version = main.VERSION)
public class main{
	
    public static final String MODID = "kirche+";
    public static final String VERSION = "1.2";
    public static ArrayList<SpenderInfo> spender = new ArrayList<>();
    public static TSClientQuery client = null;
    //Öffi: 227
    @EventHandler
    public void init(FMLInitializationEvent event) {
    	TabellenMethoden.init();
    	MarryFile.load();
		try {
			TabellenMethoden.getHVList();
			TabellenMethoden.getAllMemberSheets();
		} catch (Exception e) {e.printStackTrace();}

		try {
		    client = new TSClientQuery(KircheConfig.Query);
		    client.start();
        }catch (Exception e){}

    }
    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
    	ClientCommandHandler.instance.registerCommand(new HV_Command());
    	ClientCommandHandler.instance.registerCommand(new MarryRP_Command());
        ClientCommandHandler.instance.registerCommand(new checkDonations());
    }
}
