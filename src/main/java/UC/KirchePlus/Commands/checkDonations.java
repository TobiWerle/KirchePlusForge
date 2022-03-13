package UC.KirchePlus.Commands;

import UC.KirchePlus.Utils.SpenderInfo;
import UC.KirchePlus.Utils.SpenderUtils;
import UC.KirchePlus.Utils.TabellenMethoden;
import UC.KirchePlus.Utils.Utils;
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

public class checkDonations extends CommandBase implements IClientCommand {
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
                        TabellenMethoden.getDonations();
                        TabellenMethoden.checkDonations();
                        HashMap<String, Integer> inPublic = new HashMap<String, Integer>();
                        HashMap<String, Integer> notPublic = new HashMap<String, Integer>();
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
        List<String> tabs = new ArrayList<String>();
        return tabs;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    private boolean isInPublic(String name){
        for(Map.Entry map : SpenderUtils.publicDonations.entrySet()){
            if(map.getKey().toString().equalsIgnoreCase(name)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean allowUsageWithoutPrefix(ICommandSender sender, String message) {
        // TODO Auto-generated method stub
        return false;
    }

}
