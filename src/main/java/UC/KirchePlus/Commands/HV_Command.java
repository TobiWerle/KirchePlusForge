package UC.KirchePlus.Commands;

import UC.KirchePlus.AutomaticActivity.Handler;
import UC.KirchePlus.Events.Displayname;
import UC.KirchePlus.Utils.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.IClientCommand;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HV_Command extends CommandBase implements IClientCommand {																																																																				

	@Override
	public int compareTo(ICommand arg0) {
		return 0;
	}

	@Override
	public String getName() {
		return "hv";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/hv";
	}

	@Override
	public List<String> getAliases() {
		List<String> aliases = new ArrayList<String>();
		aliases.add("hausverbot");
		return aliases;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length == 0) {
			Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.GRAY + " Die Hausverbote werden synchronisiert! Dies könnte paar Sekunden dauern!"));
			Thread thread = new Thread(){
				@Override
				public void run() {
					try {
						TabellenMethoden.getHVList();
					} catch (IOException | GeneralSecurityException e1) {}
					Displayname.refreshAll();
					for(Entity e : Minecraft.getMinecraft().world.loadedEntityList) {
						if(e instanceof EntityPlayer) {
							EntityPlayer p = (EntityPlayer) e;
							((EntityPlayer) e).refreshDisplayName();
						}
					}
					Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.AQUA + "Die Hausverbote wurden synchronisiert!"));
				}
			};
			thread.start();
		}
		if(args.length == 1) {
			if(args[0].equalsIgnoreCase("help")) {
				Utils.displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " - " + TextFormatting.AQUA + "/hv " + TextFormatting.DARK_GRAY + "-> " + TextFormatting.GRAY + "synchronisiere die Hausverbote mit dem Client."));
				Utils.displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " - " + TextFormatting.AQUA + "/hv list" + TextFormatting.DARK_GRAY + "-> " + TextFormatting.GRAY + "Gibt eine Liste mit allen Spielern aus, die Hausverbot haben."));
				Utils.displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " - " + TextFormatting.AQUA + "/hv namecheck" + TextFormatting.DARK_GRAY + "-> " + TextFormatting.GRAY + "Überprüft ob es Fehler bei den eingetragenen Spielernamen gibt."));
				Utils.displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " - " + TextFormatting.AQUA + "/hv info <User>" + TextFormatting.DARK_GRAY + "-> " + TextFormatting.GRAY + "Zeigt die Hausverbot-Infos zum Spieler."));
				Utils.displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " - " + TextFormatting.AQUA + "/hv add <User>" + TextFormatting.DARK_GRAY + "-> " + TextFormatting.GRAY + "Trägt ein Hausverbot ein."));

			}else
			if(args[0].equalsIgnoreCase("list")) {
				Utils.displayMessage(new TextComponentString(
						TextFormatting.DARK_BLUE + "[]"+
						TextFormatting.DARK_AQUA + "========"+
						TextFormatting.GRAY + "["+
						TextFormatting.AQUA + "Hausverbot"+
						TextFormatting.GRAY + "]"+
						TextFormatting.DARK_AQUA + "========"+
						TextFormatting.DARK_BLUE + "[]"));
				ArrayList<String> online = new ArrayList<String>();
				for(String name : Displayname.HVs.keySet()) {
					if(!isOnline(name)) {
						if(!TabellenMethoden.isDayOver(Displayname.HVs.get(name).getUtilDate())) {
							Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.DARK_GRAY + " - "+name ));
						}else {
							Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.DARK_GRAY + " - "+ TextFormatting.RED +name));
						}
					}else online.add(name);
				}
				for(String name : online) {
					if(!TabellenMethoden.isDayOver(Displayname.HVs.get(name).getUtilDate())) {
						Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.GREEN + " - " +name));
					}else {
						Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.GREEN + " - " +TextFormatting.RED +name));
					}
				}
				return;
			}else
				if(args[0].equalsIgnoreCase("namecheck")){
					Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.GRAY + " NameCheck wird ausgeführt! Dies könnte paar Sekunden dauern!"));
					Thread thread = new Thread(){
						@Override
						public void run() {
							ArrayList<String> nameError = new ArrayList<>();
							for(String name : Displayname.HVs.keySet()) {
								if(!isOnline(name)) {
									if(PlayerCheck.checkName(name) == false) nameError.add(name);
								}
							}
							if(nameError.size() == 0) {
								Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.BLUE + " - Es wurden keine Spieler mit fehlerhaften Namen gefunden!"));
							}else Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.DARK_GRAY + " - Spieler mit Fehler im Namen:"));
							for(String player : nameError){
								Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.DARK_GRAY + " - "+ TextFormatting.RED +player));
							}
						}
					};
					thread.start();
			}else
			if(args[0].equalsIgnoreCase("info")) {
				Utils.displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " - " + TextFormatting.AQUA + "/hv info <User>" + TextFormatting.DARK_GRAY + "-> " + TextFormatting.GRAY + "Zeigt die Hausverbot-Infos zum Spieler."));
				return;
			}else {
				Utils.displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " - " + TextFormatting.AQUA + "/hv " + TextFormatting.DARK_GRAY + "-> " + TextFormatting.GRAY + "synchronisiere die Hausverbote mit dem Client."));
				Utils.displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " - " + TextFormatting.AQUA + "/hv list" + TextFormatting.DARK_GRAY + "-> " + TextFormatting.GRAY + "Gibt eine Liste mit allen Spielern aus, die Hausverbot haben."));
				Utils.displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " - " + TextFormatting.AQUA + "/hv info <User>" + TextFormatting.DARK_GRAY + "-> " + TextFormatting.GRAY + "Zeigt die Hausverbot-Infos zum Spieler."));
				Utils.displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " - " + TextFormatting.AQUA + "/hv add <User>" + TextFormatting.DARK_GRAY + "-> " + TextFormatting.GRAY + "Trägt ein Hausverbot ein."));
			}
		}
		if(args.length == 2) {
			if(args[0].equalsIgnoreCase("info")) {
				for(HV_User users : Displayname.HVs.values()) {
					if(args[1].equalsIgnoreCase(users.getName())) {
						Utils.displayMessage(new TextComponentString(
								TextFormatting.DARK_BLUE + "[]"+
								TextFormatting.DARK_AQUA + "========"+
								TextFormatting.GRAY + "["+
								TextFormatting.AQUA + "Hausverbot"+
								TextFormatting.GRAY + "]"+
								TextFormatting.DARK_AQUA + "========"+
								TextFormatting.DARK_BLUE + "[]"));
						Utils.displayMessage(new TextComponentString(""));
						Utils.displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " -" + TextFormatting.GRAY +" Wer: " + TextFormatting.RED + users.getName()));
						Utils.displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " -" + TextFormatting.GRAY +" Von: " + TextFormatting.RED + users.getFromMember()));
						Utils.displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " -" + TextFormatting.GRAY +" Grund: " + TextFormatting.RED + users.getReason()));
						Utils.displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " -" + TextFormatting.GRAY +" Wann: " + TextFormatting.RED + users.getFromDate()));
						Utils.displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " -" + TextFormatting.GRAY +" Bis: " + TextFormatting.RED + users.getUtilDate()));
						String str = ""; if(!users.getWeeks().equals("Permanent")) str = " Wochen";
						Utils.displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " -" + TextFormatting.GRAY +" Dauer: " + TextFormatting.RED + users.getWeeks() + str));
						Utils.displayMessage(new TextComponentString(""));
						return;
					}
				}	
			}

			if(args[0].equalsIgnoreCase("add")){
				if(args[1].length() > 16){
					Utils.displayMessage(new TextComponentString(TextFormatting.RED + "Der Name darf nicht länger als 16 Zeichen lang sein!"));
					return;
				}
				DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyy");
				LocalDateTime now = LocalDateTime.now();
				HV_ADD.temp_fromMember = Minecraft.getMinecraft().player.getName();
				HV_ADD.temp_fromDate = dateFormatter.format(now);
				HV_ADD.temp_Who = args[1];
				Handler.openHVGUI = true;
			}
		}

	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos targetPos) {
		List<String> sub = new ArrayList<>();
		List<String> tabs = new ArrayList<>();
		if(args.length == 1) {
			sub.add("list");
			sub.add("help");
			sub.add("info");
			sub.add("namecheck");
			sub.add("add");

			String start = args[0].toLowerCase();
			for(String subs : sub) {
				if(subs.toLowerCase().startsWith(start)) {
					tabs.add(subs);
				}
			}
			return tabs;
		}	
		if(args[0].equalsIgnoreCase("info")) {
			tabs.clear();
			
			if(args.length == 2) {
				String start = args[1].toLowerCase();
				for(String names : Displayname.HVs.keySet()) {
					if(names.toLowerCase().startsWith(start)) {
						tabs.add(names);	
					}
				}
				return tabs;
			}
			tabs.addAll(Displayname.HVs.keySet());
			
		}
		if(args[0].equalsIgnoreCase("add")) {
			if(!args[1].isEmpty()) {
				ArrayList<String> playerList = new ArrayList<>();
				Collection<NetworkPlayerInfo> playersC = Minecraft.getMinecraft().getConnection().getPlayerInfoMap();
				playersC.forEach((loadedPlayer) -> {
					String name = loadedPlayer.getGameProfile().getName();
					playerList.add(name);
				});

				for(String players : playerList) {
					if(players.toLowerCase().startsWith(args[1].toLowerCase())) {
						tabs.add(players);
					}
				}
				return tabs;
			}
		}
		return tabs;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}

	private boolean isOnline(String playerName) {
		if(Minecraft.getMinecraft().getConnection().getPlayerInfo(playerName) != null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean allowUsageWithoutPrefix(ICommandSender sender, String message) {
		return false;
	}

}
