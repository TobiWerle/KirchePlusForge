package UC.KirchePlus.Commands;

import UC.KirchePlus.Utils.Activity_User;
import UC.KirchePlus.Utils.Utils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.IClientCommand;

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
            Utils.displayMessage(new TextComponentString(" ===========Top-Aktivität============="));


            if(getTotalPlace(1) == null){
                Utils.displayMessage(new TextComponentString(TextFormatting.RED + "Kein Member hat bisher Aktivitäten eingetragen!"));
                Utils.displayMessage(new TextComponentString(" ===========Top-Aktivität============="));
                return;
            }
            Utils.displayMessage(new TextComponentString(TextFormatting.GOLD + getTotalPlace(1)));
            if(getTotalPlace(2) != null) Utils.displayMessage(new TextComponentString(TextFormatting.YELLOW + getTotalPlace(2)));
            if(getTotalPlace(3) != null) Utils.displayMessage(new TextComponentString(TextFormatting.GREEN + getTotalPlace(3)));

            Utils.displayMessage(new TextComponentString(" "));
            Utils.displayMessage(new TextComponentString(" ===========Deine Aktivität=============="));
            Activity_User self = Activity_User.getSelf();
            Utils.displayMessage(new TextComponentString(TextFormatting.AQUA + "Name: "+ self.getName()));
            Utils.displayMessage(new TextComponentString(TextFormatting.AQUA + "Gesamt: "+ self.getTotalActivity()));
            Utils.displayMessage(new TextComponentString(TextFormatting.AQUA + "Roleplay: "+ self.getRpActivity()));
            Utils.displayMessage(new TextComponentString(TextFormatting.AQUA + "Spenden: "+ self.getDonationActivity()));
            Utils.displayMessage(new TextComponentString(" ===================================="));

        }
    }



    private String getTotalPlace(int placeID){
        ArrayList<Activity_User> totalActivity = Activity_User.getTotalActivityUsers(placeID);
        String place = "";
        int size = totalActivity.size();
        for(Activity_User user : totalActivity){
            if(user.getTotalActivity() == "0"){
                return null;
            }
            if(size == 1){
                place = place + user.getName() + " | " + user.getTotalActivity();
            }else{
                place = place + user.getName() + " / ";
            }
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
