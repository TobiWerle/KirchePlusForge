package UC.KirchePlus.Commands;

import UC.KirchePlus.Config.KircheConfig;
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
import java.util.List;

public class spender extends CommandBase implements IClientCommand {

    @Override
    public int compareTo(ICommand arg0) {
        return 0;
    }

    @Override
    public String getName() {
        return "spender";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/spender";
    }

    @Override
    public List<String> getAliases() {
        List<String> aliases = new ArrayList<String>();
        aliases.add("/spender");
        return aliases;
    }

    // /spender add <Name> <Betrag>
    // /spender update
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(KircheConfig.ownGMail == false){
            displayMessage(new TextComponentString(TextFormatting.RED + "Die Config Einstellung ownGMail ist auf false gesetzt. Diese Funktion funktioniert nur mit einer eigenen EMail, die Rechte auf die Spender Tabelle hat."));
            return;
        }
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
                                          BlockPos targetPos) {
        List<String> tabs = new ArrayList<String>();
        if(args.length == 1) {
            tabs.add("add");
            tabs.add("updatenames");
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

    @Override
    public boolean allowUsageWithoutPrefix(ICommandSender sender, String message) {
        // TODO Auto-generated method stub
        return false;
    }

}
