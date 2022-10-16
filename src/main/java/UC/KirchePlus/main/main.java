package UC.KirchePlus.main;

import UC.KirchePlus.AutomaticActivity.SaveActivity_Command;
import UC.KirchePlus.Commands.HV_Command;
import UC.KirchePlus.Commands.MarryRP_Command;
import UC.KirchePlus.Commands.aEquip;
import UC.KirchePlus.Commands.checkDonations;
import UC.KirchePlus.Utils.*;
import akka.actor.ActorKilledException;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import scala.Array;
import scala.Int;
import scala.tools.nsc.interpreter.Power;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.stream.Collectors;

@Mod(modid = main.MODID, version = main.VERSION)
public class main {
	
    public static final String MODID = "kirche+";
    public static final String VERSION = "3.0";
    public static ArrayList<SpenderInfo> spender = new ArrayList<>();

    public static HashMap<Akti_User, Integer> gesamt = new HashMap<Akti_User, Integer>();
    public static HashMap<Akti_User, Integer> spenden = new HashMap<Akti_User, Integer>();
    public static HashMap<Akti_User, Integer> roleplay = new HashMap<Akti_User, Integer>();
    @EventHandler
    public void init(FMLInitializationEvent event)  {
    	TabellenMethoden.init();
    	MarryFile.load();
        RegistryHandler.initRegistries();
        UpdateCheck.updateCheck();
		try {
			TabellenMethoden.getHVList();
			TabellenMethoden.getAllMemberSheets();
            TabellenMethoden.getAktis();

            Akti_User user1 = (Akti_User) gesamt.keySet().toArray()[0];

            for(Akti_User user : gesamt.keySet()) {
                System.out.println(user.getGesamt_aktis());
            }


		} catch (Exception e) {e.printStackTrace();}


    }
    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
    	ClientCommandHandler.instance.registerCommand(new HV_Command());
    	ClientCommandHandler.instance.registerCommand(new MarryRP_Command());
        ClientCommandHandler.instance.registerCommand(new checkDonations());
        ClientCommandHandler.instance.registerCommand(new aEquip());
        ClientCommandHandler.instance.registerCommand(new SaveActivity_Command());

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
