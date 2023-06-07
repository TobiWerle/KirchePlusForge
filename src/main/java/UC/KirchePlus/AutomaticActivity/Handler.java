package UC.KirchePlus.AutomaticActivity;

import UC.KirchePlus.AutomaticActivity.Imgur.Imgur;
import UC.KirchePlus.AutomaticActivity.KirchePlusIMG.KirchePlusIMG_API;
import UC.KirchePlus.Config.KircheConfig;
import UC.KirchePlus.Config.uploadTypes;
import UC.KirchePlus.CustomGUI.HVADD_GUI;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ScreenShotHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(Side.CLIENT)
public class Handler {

    public static String screenshotLink = "";
    public static String topic = "";
    public static int amount = 0;
    public static boolean isDonation = false;
    static boolean churchdonation = false;
    public static SheetHandler.activityTypes activityType;

    static String[] ranks = {
            "Theologe","Theologin",
            "Diakon", "Diakonin",
            "Priester", "Priesterin",
            "Dekan", "Dekanin",
            "Bischof", "Bischöfin",
            "Kardinal", "Kardinälin",
            "Papst", "Päpstin"
    };

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onChat(ClientChatReceivedEvent e) {
        if(!KircheConfig.ownGMail){
            return;
        }
        ITextComponent message = e.getMessage();
        String unformattedText = message.getUnformattedText().replace(":", "");
        String[] arr = unformattedText.split(" ");
        for(String rank : ranks){
            if(arr[0].equals(rank)){
                if(arr[1].equals(Minecraft.getMinecraft().player.getName())){
                    if(arr[2].equals(".") || message.getUnformattedText().contains("Begrüßung:") && message.getUnformattedText().contains("Texte:") && message.getUnformattedText().contains("GBK:")){
                        Style style = new Style().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString(TextFormatting.DARK_AQUA + "Aktivität eintragen"))).
                                setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/saveactivity event"));
                        message.setStyle(style);
                        message.appendSibling(new TextComponentString(TextFormatting.BLUE + " {⬆}"));
                        e.setMessage(message);
                        return;
                    }
                }else{
                    if(message.getUnformattedText().contains("Begrüßung:") && message.getUnformattedText().contains("Texte:") && message.getUnformattedText().contains("GBK:")){
                        Style style = new Style().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString(TextFormatting.DARK_AQUA + "Aktivität eintragen"))).
                                setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/saveactivity event"));
                        message.setStyle(style);
                        message.appendSibling(new TextComponentString(TextFormatting.BLUE + " {⬆}"));
                        e.setMessage(message);
                        return;
                    }
                }
            }
        }
        if(unformattedText.contains("[F-Bank]") && unformattedText.contains("in die Fraktionsbank") && unformattedText.contains(Minecraft.getMinecraft().player.getName())){
            Pattern pattern = Pattern.compile("\\w+\\$");
            Matcher matcher = pattern.matcher(message.getUnformattedText());
            while(matcher.find()){
                amount = Integer.parseInt(matcher.group().replace("$",""));
            }
            ITextComponent newMessage = new TextComponentString(message.getFormattedText().replace("[⬆]", ""));
            Style style = new Style().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString(TextFormatting.DARK_AQUA + "Aktivität eintragen"))).
                    setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/saveactivity money"));
            newMessage.setStyle(style);
            newMessage.appendSibling(new TextComponentString(TextFormatting.BLUE + " {⬆}"));

            e.setMessage(newMessage);
            return;
        }

        if(unformattedText.startsWith("[Ablass]") && unformattedText.contains("hat einen Ablassbrief gekauft.")){
            Style style = new Style().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString(TextFormatting.DARK_AQUA + "Aktivität eintragen"))).
                    setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/saveactivity ablass"));
            message.setStyle(style);
            message.appendSibling(new TextComponentString(TextFormatting.BLUE + " {⬆}"));
            e.setMessage(message);
            topic = unformattedText.split(" ")[1];
            return;
        }

        if(unformattedText.startsWith("[Segen]") && unformattedText.contains("Du hast")){
            Style style = new Style().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString(TextFormatting.DARK_AQUA + "Aktivität eintragen"))).
                    setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/saveactivity segen"));
            message.setStyle(style);
            message.appendSibling(new TextComponentString(TextFormatting.BLUE + " {⬆}"));
            e.setMessage(message);
            topic = unformattedText.split(" ")[3];
            return;
        }

        if(unformattedText.startsWith("News") && unformattedText.contains("sind nun verheiratet!")){
            Style style = new Style().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString(TextFormatting.DARK_AQUA + "Aktivität eintragen"))).
                    setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/saveactivity marry"));
            message.setStyle(style);
            message.appendSibling(new TextComponentString(TextFormatting.BLUE + " {⬆}"));
            e.setMessage(message);
            topic = arr[1] + " & " + arr[3];
            return;
        }

        if(unformattedText.startsWith("Danke") && unformattedText.contains("die Spende!")){
            churchdonation = true;
            System.out.println("Es wurde eine Spende lokalisiert!");
            return;
        }
        if(churchdonation) {
            if(unformattedText.contains("$") && unformattedText.contains("-")){
                int donation = Integer.parseInt(unformattedText.replace("  -", "").replace("$", ""));
                System.out.println("Es wurde gespendet: " + donation);
                amount = donation;
                churchdonation = false;
                ITextComponent newMessage = new TextComponentString(message.getFormattedText().replace("[⬆]", ""));
                Style style = new Style().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString(TextFormatting.DARK_AQUA + "Aktivität eintragen"))).
                        setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/saveactivity money"));
                newMessage.setStyle(style);
                newMessage.appendSibling(new TextComponentString(TextFormatting.BLUE + " {⬆}"));
                e.setMessage(newMessage);
                return;
            }
        }

    }


    public BufferedImage makeScreen() throws IOException {
        File file1 = new File(Minecraft.getMinecraft().mcDataDir, "Kirche+");
        file1.mkdir();
        BufferedImage bufferedimage = ScreenShotHelper.createScreenshot(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight, Minecraft.getMinecraft().getFramebuffer());
        File file2 = new File(file1, "lastActivity.jpg");
        file2 = file2.getCanonicalFile();
        net.minecraftforge.client.event.ScreenshotEvent event = net.minecraftforge.client.ForgeHooksClient.onScreenshot(bufferedimage, file2);
        ImageIO.write(bufferedimage, "jpg", file2);

        return bufferedimage;
    }

    public static String screenshot(BufferedImage image) throws IOException {
        File file = new File(System.getenv("APPDATA") + "/.minecraft/Kirche+/lastActivity.jpg");
        ImageIO.write(addTextWatermark(image), "jpg", file);

        if(KircheConfig.uploadType == uploadTypes.KIRCHEPLUSIMG){
            return KirchePlusIMG_API.uploadIMG(file);
        }

        return Imgur.uploadToLink(file);
    }


    static BufferedImage addTextWatermark(BufferedImage image) {
        DateTimeFormatter date = DateTimeFormatter.ofPattern("dd.MM.yyy HH:mm");
        LocalDateTime now = LocalDateTime.now();
        Graphics2D g2d = (Graphics2D) image.getGraphics();
        AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
        g2d.setComposite(alphaChannel);
        g2d.setColor(Color.MAGENTA);
        g2d.setFont(new Font("Arial", Font.BOLD, 32));
        FontMetrics fontMetrics = g2d.getFontMetrics();
        Rectangle2D rect = fontMetrics.getStringBounds(date.format(now), g2d);

        int centerX = (image.getWidth() - (int) rect.getWidth()) / 2;
        int centerY = image.getHeight() / 2;

        g2d.drawString(date.format(now), centerX, centerY-100);
        g2d.drawString(Minecraft.getMinecraft().player.getName(), centerX, centerY-50);
        if(isDonation){
            g2d.drawString(+amount + "$", centerX, centerY);
        }
        g2d.dispose();
        return image;
        //thanks to codejava.net
    }

    public static boolean openGUI = false;
    public static boolean eventPage = false;
    public static boolean moneyPage = false;
    public static boolean blessPage = false;
    public static boolean marryPage = false;
    static int ticks=3;
    public static boolean openHVGUI = false;
    @SubscribeEvent
    public static void onTickEvent(TickEvent e){
        if(openGUI){
            if(ticks==3){
                ticks=0;
                return;
            }
            openGUI = false;
            ticks=3;
            ActivityGUI gui = new ActivityGUI();
            gui.eventPage = eventPage;
            gui.moneyPage = moneyPage;
            gui.blessPage = blessPage;
            gui.marryPage = marryPage;
            Minecraft.getMinecraft().displayGuiScreen(gui);

            eventPage = false;
            moneyPage = false;
            blessPage = false;
            marryPage = false;
            isDonation = false;
        }

        if(openHVGUI){
            HVADD_GUI gui = new HVADD_GUI();
            Minecraft.getMinecraft().displayGuiScreen(gui);
            openHVGUI = false;
        }



    }
}