package UC.KirchePlus.Commands;

import UC.KirchePlus.Utils.FactionContract;
import UC.KirchePlus.Utils.Utils;
import UC.KirchePlus.main.main;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import org.apache.commons.io.IOUtils;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class VertragInfo_Command extends CommandBase implements IClientCommand {


    @Override
    public int compareTo(ICommand arg0) {
        return 0;
    }

    @Override
    public String getName() {
        return "vertraginfo";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/vertraginfo";
    }

    @Override
    public List<String> getAliases() {
        List<String> aliases = new ArrayList<String>();
        aliases.add("vinfo");
        return aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length < 1){
            Utils.displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " - " + TextFormatting.AQUA + "/vertraginfo info" + TextFormatting.DARK_GRAY + "-> " + TextFormatting.GRAY + "Zeigt dir an, mit wem die Kirche ein Vertrag hat."));
            Utils.displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " - " + TextFormatting.AQUA + "/vertraginfo <Fraktion>" + TextFormatting.DARK_GRAY + "-> " + TextFormatting.GRAY + "Zeigt dir genaue Infos zum jeweiligen Vertrag an."));
            Utils.displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " - " + TextFormatting.AQUA + "/vertraginfo" + TextFormatting.DARK_GRAY + "-> " + TextFormatting.GRAY + "Zeigt dir diese Liste an und aktualisiert die Liste."));
            loadFactionInfoJSON();

            return;
        }
        if(args.length == 1){
            if(args[0].equalsIgnoreCase("info")){

                Utils.displayMessage(new TextComponentString(TextFormatting.DARK_AQUA + " =============Verträge============="));
                for(FactionContract factionContract : main.FactionContracs){
                    String name = factionContract.getFaction();
                    TextComponentString contract = new TextComponentString(TextFormatting.DARK_RED+"✖");
                    if(factionContract.isContract()) contract = new TextComponentString(TextFormatting.GREEN+"✔");
                    String[] conditions = factionContract.getConditions();

                    Utils.displayMessage(new TextComponentString(  " "+ name));
                    Utils.displayMessage(new TextComponentString(TextFormatting.LIGHT_PURPLE + "   Vertrag: "+contract.getFormattedText()));
                }
                Utils.displayMessage(new TextComponentString(TextFormatting.DARK_AQUA + " ===================================="));
                return;
            }

            for(FactionContract factionContract : main.FactionContracs){
                String name = factionContract.getFaction();
                if(name.toLowerCase().startsWith(args[0].toLowerCase())){
                    TextComponentString contract = new TextComponentString(TextFormatting.DARK_RED+"✖");
                    if(factionContract.isContract()) contract = new TextComponentString(TextFormatting.GREEN+"✔");
                    String[] conditions = factionContract.getConditions();

                    Utils.displayMessage(new TextComponentString(TextFormatting.DARK_AQUA + " =============Verträge============="));
                    Utils.displayMessage(new TextComponentString(  " "+ name));
                    Utils.displayMessage(new TextComponentString(TextFormatting.LIGHT_PURPLE + "   Vertrag: "+contract.getFormattedText()));
                    if(conditions == null ) Utils.displayMessage(new TextComponentString(TextFormatting.WHITE + "   Konditionen: " + TextFormatting.RED + "Keine"));
                    if(conditions != null ) for(String conditionsString : conditions) Utils.displayMessage(new TextComponentString(TextFormatting.WHITE + "    "+conditionsString));
                    Utils.displayMessage(new TextComponentString(TextFormatting.DARK_AQUA + " ===================================="));
                    return;
                }
            }

            Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.RED + "Es wurde keine Fraktion mit diesem Namen gefunden!"));

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
            for(String faction : getFactions()){
                if(faction.toLowerCase().startsWith(args[0].toLowerCase())){
                    tabs.add(faction);
                }
            }
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

    public static void loadFactionInfoJSON() {
        try {
            main.FactionContracs.clear();
            URL url = new URL("https://kircheplus-mod.de/api/factioncontract.json");
            String result = IOUtils.toString(url, StandardCharsets.UTF_8);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);
            JsonObject json = element.getAsJsonObject();
            JsonArray faction = json.getAsJsonArray("factions");

            for(int i = 0; i < faction.size(); i++){
                JsonObject factionjson = (JsonObject) faction.get(i);
                String name = factionjson.get("Name").getAsString();
                boolean contract = factionjson.get("contract").getAsBoolean();
                JsonArray conditionsArray = factionjson.get("conditions").getAsJsonArray();
                String[] conditions = null;

                if(conditionsArray.size() != 0){
                    conditions = new String[conditionsArray.size()];
                    for (int s = 0; s < conditionsArray.size(); s++) {
                        conditions[s] = conditionsArray.get(s).getAsString();
                    }
                }
                new FactionContract(name, contract, conditions);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public ArrayList<String> getFactions(){
        ArrayList<String> list = new ArrayList<>();
        for(FactionContract faction : main.FactionContracs){
            list.add(faction.getFaction());
        }
        return list;
    }
}