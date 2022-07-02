package UC.KirchePlus.AutomaticActivity.KirchePlusIMG;

import UC.KirchePlus.Config.KircheConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Iterator;
import java.util.List;

@Mod.EventBusSubscriber
@SideOnly(Side.CLIENT)
public class ServerTokenHandler {

        @SubscribeEvent
        public static void onChatReceived(ClientChatReceivedEvent e){

                //Abfragen ob es auf KirchePlus Server ist!
                String message = e.getMessage().getUnformattedText();
                if(message.startsWith("Token:")){
                        String token = message.replace("Token: ", "");
                        KircheConfig.token = token;
                        ConfigManager.sync("kirche+", Config.Type.INSTANCE);
                        e.setCanceled(true);
                }
        }
}
