package UC.KirchePlus.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

public class Utils {

    public static void displayMessage(TextComponentString text) {
        Minecraft.getMinecraft().player.sendMessage(text);
    }
}
