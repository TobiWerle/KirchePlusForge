package UC.KirchePlus.Events;

import UC.KirchePlus.Config.KircheConfig;
import UC.KirchePlus.Utils.Brot_User;
import UC.KirchePlus.Utils.HV_User;
import UC.KirchePlus.Utils.TabellenMethoden;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.HashMap;

@Mod.EventBusSubscriber(Side.CLIENT)
public class Displayname {

	public static HashMap<String, ScorePlayerTeam> teams = new HashMap<>();
	public static HashMap<String, HV_User> HVs = new HashMap<>();
	public static HashMap<String, Brot_User> BrotUser = new HashMap<>();


    @SubscribeEvent
	public static void onNameTagFormatEvent(PlayerEvent.NameFormat e){
		addTeam(e.getEntityPlayer());
	}
	public static void addTeam(EntityPlayer p) {
		if(Minecraft.getMinecraft().gameSettings.hideGUI){
			return;
		}
		try {
			String teamName = p.getName();
			boolean b = p.getWorldScoreboard().getTeam("nopush").getMembershipCollection().stream().anyMatch(name -> name.equalsIgnoreCase(p.getName()));
			if(b)return;

			if (!HVs.containsKey(p.getName()) && !BrotUser.containsKey(p.getName())) {
				if (Minecraft.getMinecraft().player.getWorldScoreboard().getTeams().stream().anyMatch(team -> team.getName().equals(teamName))) {
					Minecraft.getMinecraft().player.getWorldScoreboard().removeTeam(teams.get(p.getName()));
				}
				return;
			}

			if (Minecraft.getMinecraft().player.getWorldScoreboard().getTeams().stream().noneMatch(team -> team.getName().equals(teamName))) {
				ScorePlayerTeam team = Minecraft.getMinecraft().player.getWorldScoreboard().createTeam(teamName);
				teams.put(p.getName(), team);
			}

			if (HVs.containsKey(p.getName())) {
				if (KircheConfig.HV) {
					if (!TabellenMethoden.isDayOver(HVs.get(p.getName()).getUtilDate())) {
						Minecraft.getMinecraft().player.getWorldScoreboard().getTeam(teamName).setPrefix(KircheConfig.prefixHV.replace("&", "§") + "§f ");
					}
				}
			}else{
				Minecraft.getMinecraft().player.getWorldScoreboard().getTeam(teamName).setPrefix("");
			}

			if (BrotUser.containsKey(p.getName())) {
				if (KircheConfig.Brot) {
					if (TabellenMethoden.isSameDay(BrotUser.get(p.getName()).getDatum())) {
						Minecraft.getMinecraft().player.getWorldScoreboard().getTeam(teamName).setSuffix(KircheConfig.prefixBrot.replace("&", "§") + "§f ");
					}
				}
			}else{
				Minecraft.getMinecraft().player.getWorldScoreboard().getTeam(teamName).setSuffix("");
			}

			if (KircheConfig.Brot || KircheConfig.HV) {
				if(!isMasked(p)) {
					Minecraft.getMinecraft().player.getWorldScoreboard().addPlayerToTeam(p.getName(), teamName);
				}
			}

		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}


	static int time = 20*30;
	static int reloadTime = 60*20*2*5;
	static boolean hide = false;

	@SubscribeEvent
	public static void onTick(TickEvent.ClientTickEvent e) {

		if(reloadTime != 0){
			reloadTime--;
		}else{
			TabellenMethoden.reloadLists();
			reloadTime = 60*20*2*5;
		}

		if(Minecraft.getMinecraft().gameSettings.hideGUI){
			for(Entity e1 : Minecraft.getMinecraft().world.loadedEntityList) {
				if(e1 instanceof EntityPlayer) {
					if (Minecraft.getMinecraft().player.getWorldScoreboard().getTeams().stream().anyMatch(team -> team.getName().equals(e1.getName()))) {
						Minecraft.getMinecraft().player.getWorldScoreboard().removeTeam(teams.get(e1.getName()));
					}
				}
			}
			hide = true;
			return;
		}
		if(hide){
			refreshAll();
			System.out.println("Refresh all after dingsens... hier F1 :D");
			hide = false;
		}

		if(time != 0) {
			time--;
			return;
		}
		try {
			refreshAll();
		} catch (Exception s) {
			s.printStackTrace();
		}
		System.out.println("jetzt");
		time = 20*30*2;
	}

	public static void refreshAll(){
		for(Entity e1 : Minecraft.getMinecraft().world.loadedEntityList) {
			if(e1 instanceof EntityPlayer) {
				if (((EntityPlayer) e1).getWorldScoreboard().getTeams().stream().anyMatch(team -> team.getName().equals("nopush"))) {
					return;
				}
				addTeam((EntityPlayer) e1);
			}
		}
	}

	private static boolean isMasked(EntityPlayer ep){
		if(ScorePlayerTeam.formatPlayerName(ep.getTeam(), ep.getName()).contains("§k")){
			return true;
		}
		return false;
	}
}