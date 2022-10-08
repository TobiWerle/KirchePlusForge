package UC.KirchePlus.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

import java.util.Timer;
import java.util.TimerTask;

public class Utils {

    public static void displayMessage(TextComponentString text) {
        Minecraft.getMinecraft().player.sendMessage(text);
    }
    public static void displayMessageLater(TextComponentString text, int seconds) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Minecraft.getMinecraft().player.sendMessage(text);
            }
        }, seconds * 1000L);
    }
}
