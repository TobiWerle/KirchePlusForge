package UC.KirchePlus.AutomaticActivity;

import UC.KirchePlus.AutomaticActivity.KirchePlusIMG.KirchePlusIMG_API;
import UC.KirchePlus.Config.KircheConfig;
import UC.KirchePlus.Config.uploadTypes;
import UC.KirchePlus.Utils.Utils;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class SaveActivity_Command extends CommandBase implements IClientCommand {


    @Override
    public int compareTo(ICommand arg0) {
        return 0;
    }

    @Override
    public String getName() {
        return "saveactivity";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/saveactivity";
    }

    @Override
    public List<String> getAliases() {
        List<String> aliases = new ArrayList<String>();
        return aliases;
    }
    public static BufferedImage image = null;
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(!KircheConfig.ownGMail){
            Utils.displayMessage(new TextComponentString(TextFormatting.RED + "Für diese Funktion musst du in der Config ownGmail aktiviert haben!"));
            return;
        }
        if(KircheConfig.uploadType == uploadTypes.KIRCHEPLUSIMG){
            if(KircheConfig.token.equals("")){
                Utils.displayMessage(new TextComponentString(TextFormatting.RED + "Du brauchst ein KirchePlus Token, für dein jetzigen Upload Typ."));
                Utils.displayMessage(new TextComponentString(TextFormatting.RED + "Um ein Token zu erstellen, joine den" + TextFormatting.BLUE + " KirchePlus-Mod.de"  + TextFormatting.RED + " Server!"));
                return;
            }
            if(!KirchePlusIMG_API.checkConnection()){
                Utils.displayMessage(new TextComponentString(TextFormatting.RED + "Die Verbindung zu upload.KirchePlus-Mod.de kann nicht hergestellt werden!"));
                Utils.displayMessage(new TextComponentString(TextFormatting.RED + "Melde dies einem Leader und wechsel solange in den Mod-Einstellungen zu Imgur."));
                return;
            }
            if(!KirchePlusIMG_API.isTokenValid()){
                Utils.displayMessage(new TextComponentString(TextFormatting.RED + "Dein Token ist abgelaufen oder ungültig."));
                Utils.displayMessage(new TextComponentString(TextFormatting.RED + "Erstelle ein neuen Token auf " + TextFormatting.BLUE + "KirchePlus-Mod.de"));
                return;
            }
        }
        if(args.length != 1){
            return;
        }
        Handler handler = new Handler();
        try {
            image = handler.makeScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    if(SheetHandler.MemberSheet == null) {
                        SheetHandler.getMemberOwnSheet();
                    }
                    if(args[0].equalsIgnoreCase("ablass")){
                        Utils.displayMessage(new TextComponentString(TextFormatting.BLUE + "Screenshot wird hochgeladen dies kann eine Zeit lang dauern!"));
                        SheetHandler.saveActivity(SheetHandler.activityTypes.ABLASSBRIEF);
                        return;
                    }

                    Thread.sleep(50);
                } catch (IOException e) {
                    Utils.displayMessage(new TextComponentString(TextFormatting.RED + "Es ist ein unerwarteter Fehler aufgetreten!"));
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

        if(args[0].equalsIgnoreCase("ablass")){
            return;
        }
        if(args[0].equalsIgnoreCase("event")){
            Handler.eventPage = true;
        }
        if(args[0].equalsIgnoreCase("money")){
            Handler.moneyPage = true;
        }
        if(args[0].equalsIgnoreCase("segen")){
            Handler.blessPage = true;
        }
        if(args[0].equalsIgnoreCase("marry")){
            Handler.marryPage = true;
        }
        Handler.openGUI = true;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
                                          BlockPos targetPos) {
        return new ArrayList<String>();
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
        return false;
    }
}