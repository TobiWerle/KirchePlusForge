package UC.KirchePlus.Events;


import java.util.ArrayList;
import java.util.HashMap;

import UC.KirchePlus.Config.KircheConfig;
import UC.KirchePlus.Utils.Brot_User;
import UC.KirchePlus.Utils.HV_User;
import UC.KirchePlus.Utils.TabellenMethoden;
import UC.KirchePlus.main.main;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class Displayname {
	

	public static ArrayList<EntityPlayer> players = new ArrayList<EntityPlayer>();
	
	public static HashMap<String, HV_User> HVs = new HashMap<String, HV_User>();
	public static HashMap<String, Brot_User> BrotUser = new HashMap<String, Brot_User>();
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onNameFormat(PlayerEvent.NameFormat e) {
		try {
			EntityPlayer p = e.getEntityPlayer();
			if(!players.contains(p)) {
				players.add(p);	
			}
			String prefix = "";
			String suffix = "";
			
			if(HVs.containsKey(p.getName())) {
				if(KircheConfig.HV == true) {
					if(!TabellenMethoden.isDayOver(HVs.get(p.getName()).getBis())){
						prefix = KircheConfig.prefixHV.replace("&", "§") + "§f ";	
					}
				}
			}
			if(BrotUser.containsKey(p.getName())) {
				if(KircheConfig.Brot == true) {
					if(TabellenMethoden.isSameDay(BrotUser.get(p.getName()).getDatum())){
						suffix = "§r " + KircheConfig.prefixBrot.replace("&", "§");
					}
				}
			}
			if(!isMasked(p)) {
				e.setDisplayname(prefix + p.getName() + suffix);
			}
			
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}
	
	
	static int time = 10*60;
	@SubscribeEvent
	public static void onTick(TickEvent.ClientTickEvent e) {
		if(time != 0) {
			time--;
			return;
		}
		if(time == 0) {
			for(EntityPlayer p : players) {
				p.refreshDisplayName();
			}
			try {
				for(Entity e1 : Minecraft.getMinecraft().world.loadedEntityList) {
					if(e1 instanceof EntityPlayer) {
						EntityPlayer p = (EntityPlayer) e1;
						((EntityPlayer) e1).refreshDisplayName();
					}
				}
			} catch (Exception e2) {}
			time = 10*60;
		}
	}
	
	private static boolean isMasked(EntityPlayer ep){
		if(ScorePlayerTeam.formatPlayerName(ep.getTeam(), ep.getName()).contains("§k")){
			return true;
		}
		return false;
	}
	

}
