package UC.KirchePlus.AutomaticActivity;

import UC.KirchePlus.Utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityGUI extends GuiScreen {

    public boolean eventPage = false;
    public boolean moneyPage = false;
    public boolean blessPage = false;
    public boolean marryPage = false;

    private boolean topicPage = false;
    private boolean bibelPage = false;
    private boolean donationPage = false;
    private boolean taufePage = false;

    private GuiTextField textField;
    private GuiTextField textFieldAmount;

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void initGui() {
        addButtons();
        textField = new GuiTextField(100, fontRenderer, width / 2 - 50, height / 2 - 66, 100, 20);
        textField.setText("");
        textField.setFocused(true);
        textField.setEnabled(true);
        textField.setCanLoseFocus(true);
        textField.setVisible(false);

        textFieldAmount = new GuiTextField(101, fontRenderer, width / 2 - 50, height / 2 - 33, 100, 20);
        textFieldAmount.setText("");
        textFieldAmount.setFocused(false);
        textFieldAmount.setEnabled(true);
        textFieldAmount.setCanLoseFocus(true);
        textFieldAmount.setMaxStringLength(2);
        textFieldAmount.setVisible(false);
        super.initGui();
    }

    @Override
    public void onGuiClosed() {
        Handler.screenshotLink = "";
        textFieldAmount.setText("");
        textField.setText("");
        super.onGuiClosed();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        //drawDefaultBackground();
        drawRect(width / 2 - 58 , height / 2 - 103, width / 2 + 58,height / 2 + 59, 0xa6c0c0c0);
        drawRect(width / 2 - 55 , height / 2 - 100, width / 2 + 55,height / 2 + 56, 0x4ddcf527);
        fontRenderer.drawString("Aktivitäten", width / 2 - 25, height / 2 - 112, 0xd943ff64);

        textField.drawTextBox();
        textFieldAmount.drawTextBox();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        textField.mouseClicked(mouseX, mouseY, mouseButton);
        textFieldAmount.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if(SheetHandler.MemberSheet == null){
            Utils.displayMessage(new TextComponentString(TextFormatting.RED + "Deine Tabelle wurde noch nicht geladen. Bitte warte etwas!"));
            return;
        }
        switch (button.id){
            case 0:
                //Button für SHG
                SheetHandler.saveActivity(SheetHandler.activityTypes.SHG);
                Minecraft.getMinecraft().player.closeScreen();
                break;
            case 1:
                //Button für Tafel
                SheetHandler.saveActivity(SheetHandler.activityTypes.TAFEL);
                Minecraft.getMinecraft().player.closeScreen();
                break;
            case 2:
                //Button für Spendenevent
                SheetHandler.saveActivity(SheetHandler.activityTypes.SPENDEEVENT);
                Minecraft.getMinecraft().player.closeScreen();
                break;
            case 3:
                //Button für Beichtevent
                SheetHandler.saveActivity(SheetHandler.activityTypes.BEICHTEVENT);
                Minecraft.getMinecraft().player.closeScreen();
                break;
            case 4:
                //Button für JGA
                buttonList.clear();
                Handler.activityType = SheetHandler.activityTypes.JGA;
                eventPage = false;
                topicPage = true;
                addButtons();
                break;
            case 5:
                //Button für Kaffe und Kuchen
                buttonList.clear();
                Handler.activityType = SheetHandler.activityTypes.KAFFEKUCHEN;
                eventPage = false;
                topicPage = true;
                addButtons();
                break;
            case 6:
                //Button für Gottesdienst
                buttonList.clear();
                Handler.activityType = SheetHandler.activityTypes.GOTTESDIENST;
                eventPage = false;
                topicPage = true;
                addButtons();
                break;
            case 7:
                //Thema/Ort bestätigen Button
                if(textField.getText().length() <= 2){
                    Utils.displayMessage(new TextComponentString(TextFormatting.RED + "Der eingegebene Text für Thema/Ort darf nicht kürzer als 3 Zeichen sein!"));
                    return;
                }
                Handler.topic = textField.getText();
                SheetHandler.saveActivity(Handler.activityType);
                Minecraft.getMinecraft().player.closeScreen();
                break;
            case 8:
                //Thema/Ort Page zurück Button
                buttonList.clear();
                labelList.clear();
                textField.setVisible(false);
                eventPage = true;
                topicPage = false;
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(15);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        addButtons();
                    }
                };
                thread.start();
                break;
            case 9:
                //GUI für Spenden
                Handler.activityType = SheetHandler.activityTypes.SPENDE;
                buttonList.clear();
                labelList.clear();
                moneyPage = false;
                donationPage = true;
                bibelPage = false;
                addButtons();
                break;
            case 10:
                //GUI für Bibeln
                Handler.activityType = SheetHandler.activityTypes.BIBEL;
                buttonList.clear();
                labelList.clear();
                moneyPage = false;
                donationPage = false;
                bibelPage = true;
                addButtons();
                break;
            case 11:
                //Spende bestätigen Button
                if(textField.getText().length() <= 2){
                    Utils.displayMessage(new TextComponentString(TextFormatting.RED + "Der eingegebene Spielername darf nicht kürzer als 3 Zeichen sein!"));
                    return;
                }
                if(Handler.amount == 0){
                    Utils.displayMessage(new TextComponentString(TextFormatting.RED + "Das eingezahlte Geld darf nicht 0$ sein!"));
                    return;
                }
                Handler.topic = textField.getText();
                Handler.isDonation = true;
                SheetHandler.saveActivity(Handler.activityType);
                Minecraft.getMinecraft().player.closeScreen();
                break;
            case 12:
                //Zurück Button von Spende GUI
                buttonList.clear();
                labelList.clear();
                textField.setVisible(false);
                donationPage = false;
                moneyPage = true;
                Thread thread2 = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(15);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        addButtons();
                    }
                };
                thread2.start();
                break;
            case 13:
                //Bibel bestätigen Button
                if(textField.getText().length() <= 2){
                    Utils.displayMessage(new TextComponentString(TextFormatting.RED + "Der eingegebene Spielername darf nicht kürzer als 3 Zeichen sein!"));
                    return;
                }
                if(textFieldAmount.getText().length() == 0 && Integer.parseInt(textFieldAmount.getText()) == 0){
                    Utils.displayMessage(new TextComponentString(TextFormatting.RED + "Die angegebene Anzahl darf nicht 0 sein!"));
                    return;
                }
                Handler.topic = textField.getText();
                Handler.amount = Integer.parseInt(textFieldAmount.getText());
                SheetHandler.saveActivity(Handler.activityType);
                Minecraft.getMinecraft().player.closeScreen();
                break;
            case 14:
                //Zurück Button von Bibel GUI
                buttonList.clear();
                labelList.clear();
                textField.setVisible(false);
                textFieldAmount.setVisible(false);
                bibelPage = false;
                moneyPage = true;
                Thread thread3 = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(15);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        addButtons();
                    }
                };
                thread3.start();
                break;
            case 15:
                //Segen bestätigen
                Handler.activityType = SheetHandler.activityTypes.SEGEN;
                Handler.topic = textField.getText();
                SheetHandler.saveActivity(Handler.activityType);
                Minecraft.getMinecraft().player.closeScreen();
                break;
            case 16:
                //GUI für Taufe
                buttonList.clear();
                labelList.clear();
                textField.setVisible(false);
                blessPage = false;
                taufePage = true;
                Thread thread5 = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(15);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        addButtons();
                    }
                };
                thread5.start();
                break;
            case 17:
                //Taufe bestätigen
                if(textField.getText().length() <= 2){
                    Utils.displayMessage(new TextComponentString(TextFormatting.RED + "Der eingegebene Spielername darf nicht kürzer als 3 Zeichen sein!"));
                    return;
                }
                Handler.activityType = SheetHandler.activityTypes.TAUFE;
                Handler.topic = textField.getText();
                SheetHandler.saveActivity(Handler.activityType);
                Minecraft.getMinecraft().player.closeScreen();
                break;
            case 18:
                //Zurück Button von Taufe GUI
                buttonList.clear();
                labelList.clear();
                textField.setVisible(false);
                textFieldAmount.setVisible(false);
                taufePage = false;
                blessPage = true;
                addButtons();
                break;
            case 19:
                //Hochzeit bestätigen
                Handler.activityType = SheetHandler.activityTypes.HOCHZEIT;
                SheetHandler.saveActivity(Handler.activityType);
                Minecraft.getMinecraft().player.closeScreen();
                break;
            case 20:
                //Befehl /marry bestätigen
                Handler.activityType = SheetHandler.activityTypes.CMDMARRY;
                SheetHandler.saveActivity(Handler.activityType);
                Minecraft.getMinecraft().player.closeScreen();
                break;
        }
    }

    @Override
    public void updateScreen() {
        textField.updateCursorCounter();
        textFieldAmount.updateCursorCounter();
        super.updateScreen();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        textField.textboxKeyTyped(typedChar, keyCode);

        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(typedChar+"");
        while(matcher.find()){
            textFieldAmount.textboxKeyTyped(typedChar, keyCode);
        }
        if(keyCode == 14){
            textFieldAmount.textboxKeyTyped(typedChar, keyCode);
        }
        super.keyTyped(typedChar, keyCode);
    }

    private void addButtons(){
        if(eventPage){
            buttonList.add(new GuiButton(0,width / 2 - 50, height / 2 - 98, 100, 20, "SHG"));
            buttonList.add(new GuiButton(1,width / 2 - 50, height / 2 - 76, 100, 20, "Tafel"));
            buttonList.add(new GuiButton(2,width / 2 - 50, height / 2 - 54, 100, 20, "SpendeEvent"));
            buttonList.add(new GuiButton(3,width / 2 - 50, height / 2 - 32, 100, 20, "Beichtevent"));

            buttonList.add(new GuiButton(4,width / 2 - 50, height / 2 - 10, 100, 20, "JGA"));
            buttonList.add(new GuiButton(5,width / 2 - 50, height / 2 + 12, 100, 20, "KK"));
            buttonList.add(new GuiButton(6,width / 2 - 50, height / 2 + 34, 100, 20, "Gottesdienst"));
        }else
        if(topicPage){
            GuiLabel label = new GuiLabel(fontRenderer, 999,width / 2 - 50, height / 2 - 90,50,20,0xd943ff64);
            label.addLine("Thema/Ort eingeben:");
            labelList.add(label);
            textField.setVisible(true);

            buttonList.add(new GuiButton(7,width / 2 - 50, height / 2 - 43, 100, 20, "Thema Bestätigen"));
            buttonList.add(new GuiButton(8,width / 2 - 50, height / 2 + 34, 100, 20, "Zurück"));
        }
        if(moneyPage){
            buttonList.add(new GuiButton(9,width / 2 - 50, height / 2 - 43, 100, 20, "Spenden"));
            buttonList.add(new GuiButton(10,width / 2 - 50, height / 2 - 20, 100, 20, "Bibeln"));
        }

        if(donationPage){
            GuiLabel label = new GuiLabel(fontRenderer, 888,width / 2 - 50, height / 2 - 90,50,20,0xd943ff64);
            label.addLine("Spielername:");
            labelList.add(label);
            textField.setVisible(true);


            buttonList.add(new GuiButton(11,width / 2 - 50, height / 2 - 43, 100, 20, "Bestätigen"));
            buttonList.add(new GuiButton(12,width / 2 - 50, height / 2 + 34, 100, 20, "Zurück"));
        }
        if(bibelPage){
            GuiLabel label = new GuiLabel(fontRenderer, 888,width / 2 - 50, height / 2 - 85,50,20,0xd943ff64);
            label.addLine("Spielername:");
            labelList.add(label);

            textField.setVisible(true);
            GuiLabel label2 = new GuiLabel(fontRenderer, 889,width / 2 - 50, height / 2 - 48,50,20,0xd943ff64);
            label2.addLine("Anzahl:");
            labelList.add(label2);
            textFieldAmount.setVisible(true);

            buttonList.add(new GuiButton(13,width / 2 - 50, height / 2 + 11, 100, 20, "Bestätigen"));
            buttonList.add(new GuiButton(14,width / 2 - 50, height / 2 + 34, 100, 20, "Zurück"));
        }

        if(blessPage){
            buttonList.add(new GuiButton(15,width / 2 - 50, height / 2 - 43, 100, 20, "Segen"));
            buttonList.add(new GuiButton(16,width / 2 - 50, height / 2 - 20, 100, 20, "Taufe"));
        }

        if(taufePage){
            GuiLabel label = new GuiLabel(fontRenderer, 890,width / 2 - 50, height / 2 - 85,50,20,0xd943ff64);
            label.addLine("Täufling:");
            labelList.add(label);
            textField.setVisible(true);

            buttonList.add(new GuiButton(17,width / 2 - 50, height / 2 + 11, 100, 20, "Bestätigen"));
            buttonList.add(new GuiButton(18,width / 2 - 50, height / 2 + 34, 100, 20, "Zurück"));
        }
        if(marryPage){
            buttonList.add(new GuiButton(19,width / 2 - 50, height / 2 - 43, 100, 20, "Hochzeit"));
            buttonList.add(new GuiButton(20,width / 2 - 50, height / 2 - 20, 100, 20, "Befehl /marry"));
        }
    }
}
