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
    public static final String VERSION = "3.0";
    public static ArrayList<SpenderInfo> spender = new ArrayList<>();

    public static HashMap<Activity_User, Integer> totalActivity = new HashMap<>();
    public static HashMap<Activity_User, Integer> donationActivity = new HashMap<>();
    public static HashMap<Activity_User, Integer> roleplayActivity = new HashMap<>();

    @EventHandler
    public void init(FMLInitializationEvent event)  {
    	TabellenMethoden.init();
    	MarryFile.load();
        RegistryHandler.initRegistries();
        UpdateCheck.updateCheck();
		try {
			TabellenMethoden.getHVList();
			TabellenMethoden.getAllMemberSheets();
		} catch (Exception e) {e.printStackTrace();}
    }
    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
    	ClientCommandHandler.instance.registerCommand(new HV_Command());
    	ClientCommandHandler.instance.registerCommand(new MarryRP_Command());
        ClientCommandHandler.instance.registerCommand(new checkDonations_Command());
        ClientCommandHandler.instance.registerCommand(new aEquip());
        ClientCommandHandler.instance.registerCommand(new SaveActivity_Command());
        ClientCommandHandler.instance.registerCommand(new topActivity_Command());
    }


    //DONE
        //Check Upload Server down
        //
        //ADD UPDATE MESSAGE
        //fix updateNames
        //fix HV namecheck
        //fix Sheet namechange error.



    //TODO
    // /HV ADD | GUI öffnet sich wo man alles eintragen kann. (Ab R3 wegen Tabellen Rechte)
    // BETTER TABCOMPLETION | Halt wenn man was eintippt, bekommt er das, was besser passt. Wie bei Namen
    // /aSegen <Name> = /segen name + say "Gott segne dich <Name>" | Schauen wie man das macht wegen GD.... Sollen die sich abwechseln?
    // /announceevent <Eventtext> - Gibt eine Discord Nachricht aus. Mit DC Bot (IDK KANN ICH NICHT!)



/*

        Top-List Akti:
        1. Name RP Spenden



        _________________-
        Deine Akti punktzahl beträgt:
        Name: Player.getname
        Roleplay-Aktis:
        Spenden-Aktis:



 */

}
