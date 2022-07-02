package UC.KirchePlus.Commands;

import UC.KirchePlus.Utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.IClientCommand;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Mod.EventBusSubscriber
public class aEquip extends CommandBase implements IClientCommand {

    static int slot = 0;
    static boolean enabled = false;

    @Override
    public int compareTo(ICommand arg0) {
        return 0;
    }

    @Override
    public String getName() {
        return "aequip";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/aequip";
    }

    @Override
    public List<String> getAliases() {
        List<String> aliases = new ArrayList<String>();
        aliases.add("/aequip");
        return aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length < 1){
            Utils.displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " - " + TextFormatting.AQUA + "/aequip <Wasser/Brot>" + TextFormatting.DARK_GRAY + "-> " + TextFormatting.GRAY + "Equipe dir automatisch Brot oder Wasser"));
            return;
        }
        if(args.length == 1){
            if(args[0].equalsIgnoreCase("wasser")){
                slot = 1;
                enabled = true;
                Minecraft.getMinecraft().player.sendChatMessage("/equip");
                return;
            }
            if(args[0].equalsIgnoreCase("brot")){
                slot = 0;
                enabled = true;
                Minecraft.getMinecraft().player.sendChatMessage("/equip");
                return;
            }
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
            if(args[0].isEmpty()) {
                tabs.add("wasser");
                tabs.add("brot");
                return tabs;
            }
            String water = "wasser";
            String bread = "brot";
            if(bread.toLowerCase().startsWith(args[0])){
                tabs.add("brot");
            }
            if(water.toLowerCase().startsWith(args[0])){
                tabs.add("wasser");
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
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onGuiOpen(GuiOpenEvent e) {
        if(!enabled)return;
        if (e.getGui() instanceof GuiContainer){
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Container container = Minecraft.getMinecraft().player.openContainer;
                    Minecraft.getMinecraft().playerController.windowClick(container.windowId, slot, 0, ClickType.THROW, Minecraft.getMinecraft().player);
                    container.detectAndSendChanges();
                    Minecraft.getMinecraft().player.openContainer.detectAndSendChanges();
                    enabled = false;
                }
            }, 20);
        }
    }
    @SubscribeEvent
    public static void onChatReceived(ClientChatReceivedEvent e) {
        if(!enabled)return;
        String msg = e.getMessage().getUnformattedText();
        if(msg.contains("Du bist nicht am Equip-Punkt deiner Fraktion.")){
            enabled = false;
        }
    }
}
