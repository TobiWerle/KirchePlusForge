package UC.KirchePlus.Utils;

import UC.KirchePlus.main.main;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

@Mod.EventBusSubscriber
public class UpdateCheck {
    private static boolean sended = false;
    private static boolean updateStatus = true;

    @SubscribeEvent
    public static void onWorldJoin(EntityJoinWorldEvent e){
        if(e.getEntity() != null && e.getEntity() instanceof EntityPlayer) {
            if(!sended && !updateStatus){
                sended = true;
                Utils.displayMessageLater(new TextComponentString("§8[§6Kirche-Plus§8] §eEs wurde ein Update gefunden. Lade dir dieses auf der KirchePlus-Mod Website herunter!"), 5);
            }
        }
    }

    public static void updateCheck() {
        try {
            if(!main.VERSION.equals(getVersion())){
                updateStatus = false;
            }
        }catch (Exception e){
            System.err.println("Es ist ein Fehler beim abfragen der neusten Version aufgetreten.");
        }
    }

    private static String getVersion() throws IOException {
        URL url = new URL("https://kircheplus-mod.de/api/version.txt");
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setSSLSocketFactory(Utils.socketFactory());

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
        String input;
        StringBuilder builder = new StringBuilder();
        while ((input = bufferedReader.readLine()) != null) builder.append(input);
        bufferedReader.close();
        return builder.toString();
    }

}
