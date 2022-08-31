package UC.KirchePlus.Commands;

import UC.KirchePlus.Utils.MarryFile;
import UC.KirchePlus.Utils.MarryFile.types;
import UC.KirchePlus.Utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.IClientCommand;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MarryRP_Command extends CommandBase implements IClientCommand{

	@Override
	public int compareTo(ICommand arg0) {
		return 0;
	}

	@Override
	public String getName() {
		return "marryrp";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/marryrp";
	}

	@Override
	public List<String> getAliases() {
		List<String> aliases = new ArrayList<String>();
		aliases.add("/marryrp");
		aliases.add("/mrp");
		return aliases;
	}

	// /marryrp mm Tobi unban
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length != 3) {
			Utils.displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " - " + TextFormatting.AQUA + "/marryrp <Type> <Name1> <Name2>" + TextFormatting.DARK_GRAY + "-> " + TextFormatting.GRAY + "Erstelle ein Marry RP Text auf deinen Desktop."));
			Utils.displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " - " + TextFormatting.GRAY + "Type: MM" + TextFormatting.DARK_GRAY + "-> " + TextFormatting.GRAY + "Erstelle ein Mann-Mann RP Text"));
			Utils.displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " - " + TextFormatting.GRAY + "Type: FF" + TextFormatting.DARK_GRAY + "-> " + TextFormatting.GRAY + "Erstelle ein Frau-Frau RP Text"));
			Utils.displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " - " + TextFormatting.GRAY + "Type: MF" + TextFormatting.DARK_GRAY + "-> " + TextFormatting.GRAY + "Erstelle ein Mann-Frau RP Text. (Name1 ist immer Mann)"));
		}
		if(args.length == 3) {
			types type = types.none;
			if(args[0].toLowerCase().equals("mm")) type = types.MM;
			if(args[0].toLowerCase().equals("mf")) type = types.MF;
			if(args[0].toLowerCase().equals("ff")) type = types.FF;
			if(type == types.none) {
				Utils.displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " - " + TextFormatting.RED + "Fehler bei den angegebenen Type. Verfügbare Typen:"));
				Utils.displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " - " + TextFormatting.GRAY + "Type: MM" + TextFormatting.DARK_GRAY + "-> " + TextFormatting.GRAY + "Erstelle ein Mann-Mann RP Text"));
				Utils.displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " - " + TextFormatting.GRAY + "Type: FF" + TextFormatting.DARK_GRAY + "-> " + TextFormatting.GRAY + "Erstelle ein Frau-Frau RP Text"));
				Utils.displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " - " + TextFormatting.GRAY + "Type: MF" + TextFormatting.DARK_GRAY + "-> " + TextFormatting.GRAY + "Erstelle ein Mann-Frau RP Text. (Name1 ist immer Mann)"));
				return;
			}
			String name1 = args[1];
			String name2 = args[2];
			
			MarryFile.createMarryRP(type, name1, name2);
			Utils.displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " - " + TextFormatting.AQUA + "Du hast erfolgreich ein MarryRP Text für "+ TextFormatting.GREEN + name1
					+ TextFormatting.AQUA + " und " +TextFormatting.GREEN + name2 + TextFormatting.AQUA +" erstellt"));
			
		}
		
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos targetPos) {
		List<String> tabs = new ArrayList<String>();
		if(args.length == 1) {
			tabs.add("MM");
			tabs.add("FF");
			tabs.add("MF");
		}
		
		if(args.length == 2) {
			tabs.clear();
			
			if(!args[1].isEmpty()) {
				ArrayList<String> playerList = new ArrayList<String>();			
	        	Collection<NetworkPlayerInfo> playersC =Minecraft.getMinecraft().getConnection().getPlayerInfoMap();   	
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
			
			ArrayList<String> playerList = new ArrayList<String>();			
        	Collection<NetworkPlayerInfo> playersC =Minecraft.getMinecraft().getConnection().getPlayerInfoMap();
        	playersC.forEach((loadedPlayer) -> {
    			String name = loadedPlayer.getGameProfile().getName();
    			playerList.add(name);
    		});
			for(String players : playerList) {
				tabs.add(players);
			}
			return tabs;
		}
		
		if(args.length == 3) {
			tabs.clear();
			
			if(!args[1].isEmpty()) {
				ArrayList<String> playerList = new ArrayList<String>();		
	        	Collection<NetworkPlayerInfo> playersC =Minecraft.getMinecraft().getConnection().getPlayerInfoMap();
	        	playersC.forEach((loadedPlayer) -> {
	    			String name = loadedPlayer.getGameProfile().getName();
	    			playerList.add(name);
	    		});
	        	
				for(String players : playerList) {
					if(players.toLowerCase().startsWith(args[2].toLowerCase())) {
						tabs.add(players);	
					}
				}
				return tabs;
			}
			
			ArrayList<String> playerList = new ArrayList<String>();			
        	Collection<NetworkPlayerInfo> playersC =Minecraft.getMinecraft().getConnection().getPlayerInfoMap();
        	playersC.forEach((loadedPlayer) -> {
    			String name = loadedPlayer.getGameProfile().getName();
    			playerList.add(name);
    		});
			for(String players : playerList) {
				tabs.add(players);
			}
			return tabs;
		}
		
		
		return tabs;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}

	@Override
	public boolean allowUsageWithoutPrefix(ICommandSender sender, String message) {
		return false;
	}
}