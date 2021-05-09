package UC.KirchePlus.Config;

import UC.KirchePlus.Events.Displayname;
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
	
@Config.Name("Kirchen Brotliste")
@Config.Comment("Stelle ein, ob die Spieler die Brot bekommen haben angezeigt werden sollen.")
    public static boolean Brot = true;
	
@Config.Name("Brot Prefix")
@Config.Comment("Stelle ein, wie die Spieler mit Brot gekennzeichnet werden sollen.")
    public static String prefixBrot = "&8[&2✔&8]";
	
	
	
	@SubscribeEvent
	public static void configChanged(ConfigChangedEvent e) {
		ConfigManager.sync(main.MODID, Config.Type.INSTANCE);
		for(EntityPlayer p : Displayname.players) {
			p.refreshDisplayName();
		}
		for(Entity entity : Minecraft.getMinecraft().world.loadedEntityList) {
			if(entity instanceof EntityPlayer) {
				EntityPlayer p = (EntityPlayer) entity;
				((EntityPlayer) entity).refreshDisplayName();
			}
		}
	}
}
