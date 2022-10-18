package UC.KirchePlus.AutomaticActivity.KirchePlusIMG;

import UC.KirchePlus.Config.KircheConfig;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
@SideOnly(Side.CLIENT)
public class ServerTokenHandler {

        @SubscribeEvent
        public static void onChatReceived(ClientChatReceivedEvent e){
                String message = e.getMessage().getUnformattedText();
                if(message.startsWith("Token:")){
                        String token = message.replace("Token: ", "");
                        KircheConfig.token = token;
                        ConfigManager.sync("kirche+", Config.Type.INSTANCE);
                        e.setCanceled(true);
                }
        }
}
