package UC.KirchePlus.Commands;

import UC.KirchePlus.Utils.Activity_User;
import UC.KirchePlus.Utils.TabellenMethoden;
import UC.KirchePlus.Utils.Utils;
import UC.KirchePlus.main.main;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.IClientCommand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class topActivity_Command extends CommandBase implements IClientCommand {

    @Override
    public String getName() {
        return "topactivity";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/topactivity";
    }

    @Override
    public List<String> getAliases() {
        List<String> aliases = new ArrayList<String>();
        aliases.add("topactivity");
        return aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length == 0) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        TabellenMethoden.getActivitys();
                    } catch (IOException ignored) {}

                    Utils.displayMessage(new TextComponentString(TextFormatting.DARK_AQUA + " =============Top-Aktivität============="));
                    if (getTotalPlace(true) == null) {
                        Utils.displayMessage(new TextComponentString(TextFormatting.RED + "Kein Member hat bisher Aktivitäten eingetragen!"));
                        Utils.displayMessage(new TextComponentString(TextFormatting.DARK_AQUA + " =============Top-Aktivität============="));
                        return;
                    }
                    String arrow = " ➜ ";
                    if (getTotalPlace(true) != null)
                        Utils.displayMessage(new TextComponentString(TextFormatting.GRAY + arrow + TextFormatting.GOLD + " 1." + getTotalPlace(false)));
                    if (getTotalPlace(true) != null)
                        Utils.displayMessage(new TextComponentString(TextFormatting.GRAY + arrow + TextFormatting.GOLD + " 2." + getTotalPlace(false)));
                    if (getTotalPlace(true) != null)
                        Utils.displayMessage(new TextComponentString(TextFormatting.GRAY + arrow + TextFormatting.GOLD + " 3." + getTotalPlace(false)));
                    if (getTotalPlace(true) != null)
                        Utils.displayMessage(new TextComponentString(TextFormatting.GRAY + arrow + TextFormatting.GOLD + " 4." + getTotalPlace(false)));
                    if (getTotalPlace(true) != null)
                        Utils.displayMessage(new TextComponentString(TextFormatting.GRAY + arrow + TextFormatting.GOLD + " 5." + getTotalPlace(false)));
                    Utils.displayMessage(new TextComponentString(TextFormatting.DARK_AQUA + " ===========Deine Aktivität=============="));
                    Activity_User self = Activity_User.getSelf();
                    Utils.displayMessage(new TextComponentString(TextFormatting.GRAY + arrow + TextFormatting.AQUA + "Name: " + self.getName()));
                    Utils.displayMessage(new TextComponentString(TextFormatting.GRAY + arrow + TextFormatting.AQUA + "Gesamt: " + self.getTotalActivity()));
                    Utils.displayMessage(new TextComponentString(TextFormatting.GRAY + arrow + TextFormatting.AQUA + "Roleplay: " + self.getRpActivity()));
                    Utils.displayMessage(new TextComponentString(TextFormatting.GRAY + arrow + TextFormatting.AQUA + "Spenden: " + self.getDonationActivity()));
                    Utils.displayMessage(new TextComponentString(TextFormatting.DARK_AQUA + " ===================================="));


                }
            });

            thread.start();
        }
    }

    public static String getTotalPlace(boolean b){
        ArrayList<Activity_User> totalActivity = Activity_User.getTotalActivityUsers(0);
        String place = "";
        int size = totalActivity.size();
        for(Activity_User user : totalActivity){
            if(user.getTotalActivity().equals("0")){
                return null;
            }
            if(b) return "";
            if(size == 1){
                place = place + user.getName() + " | " + user.getTotalActivity();
            }else {
                place = place + user.getName() + " / ";
            }
            main.totalActivity.remove(user);
            size--;
        }
        return place;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
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
