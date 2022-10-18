package UC.KirchePlus.Commands;

import UC.KirchePlus.Config.KircheConfig;
import UC.KirchePlus.Utils.*;
import UC.KirchePlus.main.main;
import net.minecraft.client.Minecraft;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class checkDonations_Command extends CommandBase implements IClientCommand {
    boolean inTask = false;
    @Override
    public int compareTo(ICommand arg0) {
        return 0;
    }

    @Override
    public String getName() {
        return "checkdonations";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/checkdonations";
    }

    @Override
    public List<String> getAliases() {
        List<String> aliases = new ArrayList<String>();
        aliases.add("checkspenden");
        return aliases;
    }


    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length == 1){
            if(args[0].equalsIgnoreCase("updateNames")){

                if(!KircheConfig.ownGMail){
                    Utils.displayMessage(new TextComponentString(TextFormatting.RED + "Für diese Funktion musst du in der Config ownGmail aktiviert haben!"));
                    return;
                }
                Utils.displayMessage(new TextComponentString(TextFormatting.GREEN + "Die Spendenliste wird auf Fehler überprüft. Dies kann einige Zeit brauchen."));
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            TabellenMethoden.getDonations();
                            Map<String, String> wrongNames = new HashMap<>();
                            for(publicDonators info : SpenderUtils.publicDonations){
                                String currentName = PlayerCheck.NameFromUUID(info.getUUID());
                                if(!info.getName().equals(currentName)){
                                    wrongNames.put(currentName, info.getName());
                                    TabellenMethoden.updateName(currentName, info.getUUID());
                                }
                            }

                            for(String s : wrongNames.keySet()){

                                Utils.displayMessage(new TextComponentString(TextFormatting.YELLOW + wrongNames.get(s) + TextFormatting.RED +" wurde durch " + TextFormatting.GREEN + s + " ersetzt."));
                            }
                            if(wrongNames.isEmpty()){
                                Utils.displayMessage(new TextComponentString(TextFormatting.GREEN + "Es wurden keine veränderten Namen entdeckt."));
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();


            }
        }
        if (args.length == 0) {
            if (inTask) {
                Utils.displayMessage(new TextComponentString(TextFormatting.RED + "Bitte warte noch einen Moment!"));
                return;
            }
            inTask = true;
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.GRAY + " Die Donations werden geladen. Dies kann einige Zeit in anspruch nehmen!"));
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        TabellenMethoden.memberSheets.clear();
                        TabellenMethoden.getAllMemberSheets();
                        TabellenMethoden.getDonations();
                        TabellenMethoden.checkDonations();
                        HashMap<String, Integer> inPublic = new HashMap<>();
                        HashMap<String, Integer> notPublic = new HashMap<>();

                        for (SpenderInfo spender : main.spender) {
                            if (isInPublic(spender.getName())) {
                                inPublic.put(spender.getName(), spender.getAmount() + SpenderUtils.getAmountByName(spender.getName()));
                                Utils.displayMessage(new TextComponentString(TextFormatting.GRAY + " - " + TextFormatting.GOLD + spender.getName() + TextFormatting.AQUA + " Steht im Öffentlich-Channel : "
                                        + TextFormatting.GOLD + (spender.getAmount() + SpenderUtils.getAmountByName(spender.getName())) + TextFormatting.GREEN + "(+" + spender.getAmount() + ")"));
                            }
                            if (spender.getAmount() >= 5000) {
                                if (!isInPublic(spender.getName())) {
                                    notPublic.put(spender.getName(), spender.getAmount());
                                    Utils.displayMessage(new TextComponentString(TextFormatting.GRAY + " - " + TextFormatting.GOLD + spender.getName() + TextFormatting.AQUA + " muss eingetragen werden : "
                                            + TextFormatting.GOLD + spender.getAmount()));
                                }
                            }
                        }
                        if(inPublic.size() == 0 && notPublic.size() == 0){
                            Utils.displayMessage(new TextComponentString(TextFormatting.RED + "Es wurden keine Eintrage gefunden, die eingetragen werden müssen!"));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    inTask = false;
                }
            };
            thread.start();
        }

    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
        ArrayList<String> tabs = new ArrayList<>();
        tabs.add("updateNames");
        return tabs;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    private boolean isInPublic(String name){
        for(publicDonators info : SpenderUtils.publicDonations){
            if(info.getName().equalsIgnoreCase(name)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean allowUsageWithoutPrefix(ICommandSender sender, String message) {
        return false;
    }

}
