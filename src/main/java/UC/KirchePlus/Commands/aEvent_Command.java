package UC.KirchePlus.Commands;

import UC.KirchePlus.Config.KircheConfig;
import UC.KirchePlus.Utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.IClientCommand;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class aEvent_Command extends CommandBase implements IClientCommand {

    @Override
    public int compareTo(ICommand arg0) {
        return 0;
    }
    @Override
    public String getName() {
        return "aevent";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/aevent";
    }

    @Override
    public List<String> getAliases() {
        List<String> aliases = new ArrayList<String>();
        aliases.add("/aevent");
        aliases.add("/discordevent");
        aliases.add("/churchevent");
        return aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length != 2){
            Utils.displayMessage(new TextComponentString("§8 - §b /aevent <Event> <Uhrzeit (20:15)> §8 -> §7Sende eine Event Ankündigung in den Discord."));
            return;
        }
        String event = args[0];
        String time = args[1];
        if(!isEvent(event)){
            Utils.displayMessage(new TextComponentString("§cBitte gebe ein gültiges Event an. §7(§8SHG§7, §8KK§7, §8JGA§7, §8Tafel§7, §8Spendenevent§7, §8Beichtevent§7)"));
            return;
        }
        if(!isTimeValid(time)){
            Utils.displayMessage(new TextComponentString("§cBitte gebe die Zeit im richtigen Format an. §8Format§7: §aHH:MM"));
            return;
        }


        String token = KircheConfig.token;
        String member = Minecraft.getMinecraft().player.getName();


        Thread thread = new Thread(() -> {
            String response = sendEventNotifier(event, time, token, member);
            if(response.equals("complete")){
                Utils.displayMessage(new TextComponentString("§aDas Event §7"+event+" §awurde im Discord angekündigt!"));
            }else if(response.equals("auth error")){
                Utils.displayMessage(new TextComponentString("§cDein Upload Token ist abgelaufen oder ungültig."));
            }else if(response.equals("event error")){
                Utils.displayMessage(new TextComponentString("§cDas angegebene Event existiert nicht!"));
            }else if(response.equals("Error")){
                Utils.displayMessage(new TextComponentString("§cEs ist ein unerwarteter Fehler aufgetreten!"));
            }else if(response.equals("cooldown")){
                Utils.displayMessage(new TextComponentString("§cDu kannst nur alle 10 Minuten ein Event ankündigen!"));
            }else if(response.equals("not a member")){
                Utils.displayMessage(new TextComponentString("§cDu bist kein Member der Fraktion!"));
            }
        });
        thread.start();
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
        List<String> tabCompletions = new ArrayList<>();
        String[] events = {"SHG","KK","JGA","Tafel","Spendenevent","Beichtevent"};
        if(args.length == 1) {
            for(String faction : events){
                if(faction.toLowerCase().startsWith(args[0].toLowerCase())){
                    tabCompletions.add(faction);
                }
            }
        }

        return tabCompletions;
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

    private boolean isEvent(String s){
        String[] events = {"SHG", "KK", "JGA", "Tafel","Spendenevent","Beichtevent"};

        for(String event : events){
            if(s.equalsIgnoreCase(event)){
                return true;
            }
        }
        return false;
    }

    private boolean isTimeValid(String s){
        String[] time = s.split(":");
        if(time.length != 2 && time[0].length() != 2 && time[1].length() != 2){
            return false;
        }
        try {
            int hour = Integer.parseInt(time[0]);
            int minute = Integer.parseInt(time[1]);
            if(hour > 24 || hour < 0 || minute > 60 || minute < 0)return false;
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }

    protected String sendEventNotifier(String event, String time, String token, String member){
        try {
            String url = String.format("http://152.89.107.212:8082/serverapi?token=%s&event=%s&member=%s&time=%s",
                    token,
                    event,
                    member,
                    time);

            System.out.println(url);
            StringBuilder result = new StringBuilder();
            URL URL = new URL(url); //url
            HttpURLConnection conn = (HttpURLConnection) URL.openConnection();
            conn.setRequestMethod("GET");

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {
                for (String line; (line = reader.readLine()) != null; ) {
                    result.append(line);
                }
            }

            return result.toString();
        } catch (Exception ignored) {
            return null;
        }
    }
}
