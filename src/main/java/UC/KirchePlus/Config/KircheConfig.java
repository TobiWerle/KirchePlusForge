package UC.KirchePlus.Config;

import UC.KirchePlus.Events.Displayname;
import UC.KirchePlus.Utils.TabellenMethoden;
import UC.KirchePlus.main.main;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Config(modid = main.MODID, name = main.MODID)
@Mod.EventBusSubscriber(Side.CLIENT)
public class KircheConfig {

	@Config.Name("Kirchen Hausverbot")
@Config.Comment("Stelle ein, ob die Hausverbote der Kirche angezeigt werden sollen.")
    public static boolean HV = true;

	@Config.Name("Hausverbot Prefix")
@Config.Comment("Stelle ein, wie die Spieler mit Hausverbot gekennzeichnet werden sollen")
	public static String prefixHV = "&8[&cHV&8]";

@Config.Name("ownGMail")
@Config.Comment("Stelle eine Verbindung zu deiner eigenen GMail her, um Schreibrechte auf eine Tabelle zu erhalten.")
public static boolean ownGMail = false;

@Config.Name("Upload Site")
@Config.Comment("Lege fest, wo deine Screenshots für dein Aktinachweis hochgeladen werden soll. (KirchePlusIMG wird empfohlen!)")
public static uploadTypes uploadType = uploadTypes.KIRCHEPLUSIMG;

@Config.Name("KirchePlusIMG-Token")
@Config.Comment("Wird automatisch gesetzt, wenn du den Server: KirchePlus-Mod.de joinst! Dies ist für unsere eigene Upload Seite erforderlich!")
public static String token = "";

@Config.Name("Trink Benachichtigung")
@Config.Comment("Hier kannst du Einstellen ob du die Warnung bekommen willst wenn du Durstig oder Verdurstest")
public static boolean Drink = true;

	@Config.Name("Trink Sound")
	@Config.Comment("Hier kannst du Einstellen ob du den Sound bekommen willst wenn du Durstig oder Verdurstest")
	public static boolean DrinkSound = true;


	@SubscribeEvent
	public static void configChanged(ConfigChangedEvent e) {
		ConfigManager.sync(main.MODID, Config.Type.INSTANCE);

		try {
			for(EntityPlayer p : Displayname.players) {
				p.refreshDisplayName();
			}
			for(Entity entity : Minecraft.getMinecraft().world.loadedEntityList) {
				if(entity instanceof EntityPlayer) {
					EntityPlayer p = (EntityPlayer) entity;
					((EntityPlayer) entity).refreshDisplayName();
				}
			}
		}catch (Exception ignored){}

		if(TabellenMethoden.ownLoaded && !ownGMail){
			TabellenMethoden.deleteOwn();
			TabellenMethoden.sheetsService = null;
			TabellenMethoden.ownLoaded = false;
			TabellenMethoden.init();
		}
		if(!TabellenMethoden.ownLoaded && ownGMail){
			TabellenMethoden.ownLoaded = true;
			TabellenMethoden.init();
		}
	}
}