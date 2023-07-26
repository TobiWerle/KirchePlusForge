package UC.KirchePlus.Events;


import UC.KirchePlus.Config.KircheConfig;
import UC.KirchePlus.Utils.SoundsHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(Side.CLIENT)
public class DrinkNotification {
static boolean cooldown = false;

private static void cooldown() {
    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            cooldown = false;

        }
    });
    thread.start();
}
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onChat(ClientChatReceivedEvent e) {
        ITextComponent message = e.getMessage();
        String unformattedText = message.getFormattedText();
        if(cooldown) return;
        if (KircheConfig.Drink) {
            EntityPlayer p = Minecraft.getMinecraft().player;
            if (unformattedText.contains("§bDu bist durstig")) {

                Minecraft.getMinecraft().ingameGUI.displayTitle("§bDu bist durstig!", "", 1, 3 * 20, 4);
                Minecraft.getMinecraft().ingameGUI.displayTitle(null, "§cDu solltest etwas trinken", 1, 3 * 20, 4);
                if(KircheConfig.DrinkSound) {
                    p.getEntityWorld().playSound(p, p.getPosition(), SoundsHandler.ENTITY_DRINK_MASTER, SoundCategory.MASTER, 2F, 1.0F);
                    p.getEntityWorld().playSound(p, p.getPosition(), SoundEvents.BLOCK_NOTE_BELL, SoundCategory.MASTER, 2F, 1.3F);
                }
                cooldown = true;
                cooldown();
            } else if (unformattedText.contains("§cDu verdurstest...")) {
                Minecraft.getMinecraft().ingameGUI.displayTitle("§cDU VERDURSTEST!", "", 1, 5 * 20, 4);
                Minecraft.getMinecraft().ingameGUI.displayTitle(null, "§cTRINK SCHNELL ETWAS!!!!!", 1, 5 * 20, 4);
                if(KircheConfig.DrinkSound) {
                    p.getEntityWorld().playSound(p, p.getPosition(), SoundsHandler.ENTITY_DRYOUT_MASTER, SoundCategory.MASTER, 2F, 1.0F);
                    p.getEntityWorld().playSound(p, p.getPosition(), SoundEvents.BLOCK_NOTE_BELL, SoundCategory.MASTER, 2F, 1.3F);
                }
                cooldown = true;
                cooldown();
            }
        }
    }
}
