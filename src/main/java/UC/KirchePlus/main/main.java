package UC.KirchePlus.main;

import UC.KirchePlus.AutomaticActivity.SaveActivity_Command;
import UC.KirchePlus.Commands.*;
import UC.KirchePlus.Utils.*;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.ArrayList;
import java.util.HashMap;

@Mod(modid = main.MODID, version = main.VERSION)
public class main {
	
    public static final String MODID = "kirche+";
    public static final String VERSION = "3.1b";
    public static ArrayList<SpenderInfo> spender = new ArrayList<>();
    public static HashMap<Activity_User, Integer> totalActivity = new HashMap<>();
    public static ArrayList<FactionContract> FactionContracs = new ArrayList<>();

    @EventHandler
    public void init(FMLInitializationEvent event) {
        VertragInfo_Command.loadFactionInfoJSON();
        TabellenMethoden.init();
    	MarryFile.load();
        RegistryHandler.initRegistries();
        UpdateCheck.updateCheck();
		try {
			TabellenMethoden.getHVList();
			TabellenMethoden.getAllMemberSheets();
		} catch (Exception ignored) {}
    }
    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
    	ClientCommandHandler.instance.registerCommand(new HV_Command());
    	ClientCommandHandler.instance.registerCommand(new MarryRP_Command());
        ClientCommandHandler.instance.registerCommand(new checkDonations_Command());
        ClientCommandHandler.instance.registerCommand(new aEquip());
        ClientCommandHandler.instance.registerCommand(new SaveActivity_Command());
        ClientCommandHandler.instance.registerCommand(new topActivity_Command());
        ClientCommandHandler.instance.registerCommand(new VertragInfo_Command());
        ClientCommandHandler.instance.registerCommand(new GDEinteilung_Command());
    }
}
