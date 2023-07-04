package UC.KirchePlus.Commands;



import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

public class Brot_Command extends CommandBase implements IClientCommand {

    @Override
    public int compareTo(ICommand arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getName() {
        return "brot";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/brot";
    }

    @Override
    public List<String> getAliases() {
        List<String> aliases = new ArrayList<String>();
        aliases.add("bread");
        aliases.add("breadlist");
        aliases.add("brotlist");
        aliases.add("durchfütterung");
        aliases.add("fütterungszeit");
        return aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length == 0) {
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.GRAY + " Die Brotliste wird synchronisiert! Dies könnte paar Sekunden dauern!"));
            Thread thread = new Thread(){
                @Override
                public void run() {
                    try {
                        TabellenMethoden.getBrotList();
                    } catch (IOException | GeneralSecurityException e1) {}
                    Displayname.refreshAll();
                    for(Entity e : Minecraft.getMinecraft().world.loadedEntityList) {
                        if(e instanceof EntityPlayer) {
                            EntityPlayer p = (EntityPlayer) e;
                            ((EntityPlayer) e).refreshDisplayName();
                        }
                    }
                    Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.AQUA + "Die Brotliste wurde synchronisiert!"));
                }
            };
            thread.start();
        }

        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("help")) {
                displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " - " + TextFormatting.AQUA + "/brot " + TextFormatting.DARK_GRAY + "-> " + TextFormatting.GRAY + "synchronisiere die Brotliste mit dem Client."));
                displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " - " + TextFormatting.AQUA + "/brot list" + TextFormatting.DARK_GRAY + "-> " + TextFormatting.GRAY + "Gibt eine Liste mit allen Spielern aus, die Brot bekommen haben."));
                displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " - " + TextFormatting.AQUA + "/brot info <User>" + TextFormatting.DARK_GRAY + "-> " + TextFormatting.GRAY + "Zeigt die Brot-Infos zum Spieler."));
                displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " - " + TextFormatting.AQUA + "/brot add <User>" + TextFormatting.DARK_GRAY + "-> " + TextFormatting.GRAY + "Füge einen Spieler zur Brotliste hinzu."));
            }else
            if(args[0].equalsIgnoreCase("list")) {
                displayMessage(new TextComponentString(
                        TextFormatting.DARK_BLUE + "[]"+
                                TextFormatting.GREEN + "========"+
                                TextFormatting.GRAY + "["+
                                TextFormatting.GOLD + "Brotliste"+
                                TextFormatting.GRAY + "]"+
                                TextFormatting.GREEN + "========"+
                                TextFormatting.DARK_BLUE + "[]"));

                ArrayList<String> online = new ArrayList<String>();

                for(String name : Displayname.BrotUser.keySet()) {
                    if(!isOnline(name)) {
                        Brot_User user = Displayname.BrotUser.get(name);
                        String color = " §a";
                        if(!TabellenMethoden.isSameDay(user.getDatum())) color = " §c";
                        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.DARK_GRAY + " - "+name + color + user.getDatum()));
                    }else online.add(name);
                }
                for(String name : online) {
                    Brot_User user = Displayname.BrotUser.get(name);
                    String color = " §a";
                    if(!TabellenMethoden.isSameDay(user.getDatum())) color = " §c";
                    Minecraft.getMinecraft().player.sendMessage(new TextComponentString(color+ " - "+name + color + user.getDatum()));
                }
            }
        }
        if(args.length == 2) {
            if(args[0].equalsIgnoreCase("info")) {
                for(Brot_User users : Displayname.BrotUser.values()) {
                    if(args[1].equalsIgnoreCase(users.getEmpfänger())) {
                        displayMessage(new TextComponentString(
                                TextFormatting.DARK_BLUE + "[]"+
                                        TextFormatting.GREEN + "========"+
                                        TextFormatting.GRAY + "["+
                                        TextFormatting.GOLD + "Brotliste"+
                                        TextFormatting.GRAY + "]"+
                                        TextFormatting.GREEN + "========"+
                                        TextFormatting.DARK_BLUE + "[]"));
                        displayMessage(new TextComponentString(""));
                        displayMessage(new TextComponentString(TextFormatting.DARK_AQUA + " -" + TextFormatting.GOLD +" Empfänger: " + TextFormatting.GREEN + users.getEmpfänger()));
                        displayMessage(new TextComponentString(TextFormatting.DARK_AQUA + " -" + TextFormatting.GOLD +" Member: " + TextFormatting.GREEN + users.getMember()));
                        String color = "§a";
                        if(!TabellenMethoden.isSameDay(users.getDatum())) color = " §c";
                        displayMessage(new TextComponentString(TextFormatting.DARK_AQUA + " -" + TextFormatting.GOLD +" Datum: " + color + users.getDatum()));
                        displayMessage(new TextComponentString(""));
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
                Bread_ADD.Member = Minecraft.getMinecraft().player.getName();
                Bread_ADD.Date = dateFormatter.format(now);
                Bread_ADD.Who = args[1];
                Bread_ADD.writeBread();
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
        List<String> tabs = new ArrayList<String>();
        if(args.length == 1) {
            tabs.add("list");
            tabs.add("help");
            tabs.add("info");
            tabs.add("namecheck");
        }
        if(args[0].equalsIgnoreCase("info")) {
            tabs.clear();

            if(args.length == 2) {
                String start = args[1].toLowerCase();
                for(String names : Displayname.BrotUser.keySet()) {
                    if(names.toLowerCase().startsWith(start)) {
                        tabs.add(names);
                    }
                }
                return tabs;
            }


            for(String names : Displayname.BrotUser.keySet()) {
                tabs.add(names);
            }
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


    private void displayMessage(TextComponentString text) {
        Minecraft.getMinecraft().player.sendMessage(text);
    }
    private boolean isOnline(String Playername) {
        if(Minecraft.getMinecraft().getConnection().getPlayerInfo(Playername) != null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean allowUsageWithoutPrefix(ICommandSender sender, String message) {
        // TODO Auto-generated method stub
        return false;
    }

}