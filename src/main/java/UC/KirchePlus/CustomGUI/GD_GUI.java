package UC.KirchePlus.CustomGUI;

import UC.KirchePlus.AutomaticActivity.SheetHandler;
import UC.KirchePlus.Utils.HV_ADD;
import UC.KirchePlus.Utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GD_GUI extends GuiScreen {

    private GuiTextField brg;
    private GuiTextField text;
    private GuiTextField vu;
    private GuiTextField gbk;
    private GuiTextField vrab;
    private GuiTextField sgn;
    private GuiTextField spdn;
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }


    int y;
    int maxy = 90;
    @Override
    public void initGui() {
        //Begrüßung, Texte, VaterUnser, GBK, Verabschiedung, Segen, Spenden, BESTÄTIGEN
        //Label -> Textfield = -10
        //Textfield -> Label -15

        GuiLabel label = new GuiLabel(fontRenderer, 1,width / 2 - 50 , height / 2 - maxy,50,10,0xd943ff64);
        label.addLine("Begrüßung");
        labelList.add(label);
        y = 10;
        brg = new GuiTextField(2, fontRenderer, width / 2 - 50, height / 2 - maxy + y, 100, 10);
        brg.setText("");
        brg.setMaxStringLength(500);
        brg.setFocused(true);
        brg.setEnabled(true);
        brg.setCanLoseFocus(true);
        brg.setVisible(true);

        y = 25;
        GuiLabel label2 = new GuiLabel(fontRenderer, 3,width / 2 - 50, height / 2 - maxy + y,90,10,0xd943ff64);
        label2.addLine("Texte");
        labelList.add(label2);
        y = 35;
        text = new GuiTextField(4, fontRenderer, width / 2 - 50, height / 2 - maxy + y, 100, 10);
        text.setText("");
        brg.setMaxStringLength(500);
        text.setFocused(false);
        text.setEnabled(true);
        text.setCanLoseFocus(true);
        text.setVisible(true);

        y = 50;
        GuiLabel label3 = new GuiLabel(fontRenderer, 3,width / 2 - 50, height / 2 - maxy + y,90,10,0xd943ff64);
        label3.addLine("Vater Unser");
        labelList.add(label3);
        y = 60;
        vu = new GuiTextField(4, fontRenderer, width / 2 - 50, height / 2 - maxy + y, 100, 10);
        vu.setText("");
        brg.setMaxStringLength(500);
        vu.setFocused(false);
        vu.setEnabled(true);
        vu.setCanLoseFocus(true);
        vu.setVisible(true);

        y = 75;
        GuiLabel label4 = new GuiLabel(fontRenderer, 3,width / 2 - 50, height / 2 - maxy + y,90,10,0xd943ff64);
        label4.addLine("GBK");
        labelList.add(label4);

        y = 85;
        gbk = new GuiTextField(4, fontRenderer, width / 2 - 50, height / 2 - maxy + y, 100, 10);
        gbk.setText("");
        brg.setMaxStringLength(500);
        gbk.setFocused(false);
        gbk.setEnabled(true);
        gbk.setCanLoseFocus(true);
        gbk.setVisible(true);

        y = 100;
        GuiLabel label5 = new GuiLabel(fontRenderer, 3,width / 2 - 50, height / 2 - maxy + y ,90,10,0xd943ff64);
        label5.addLine("Verabschiedung");
        labelList.add(label5);

        y = 110;
        vrab = new GuiTextField(4, fontRenderer, width / 2 - 50, height / 2 - maxy + y, 100, 10);
        vrab.setText("");
        brg.setMaxStringLength(500);
        vrab.setFocused(false);
        vrab.setEnabled(true);
        vrab.setCanLoseFocus(true);
        vrab.setVisible(true);

        y = 125;
        GuiLabel label6 = new GuiLabel(fontRenderer, 3,width / 2 - 50, height / 2 - maxy + y ,90,10,0xd943ff64);
        label6.addLine("Segen");
        labelList.add(label6);

        y = 135;
        sgn = new GuiTextField(4, fontRenderer, width / 2 - 50, height / 2 - maxy + y, 100, 10);
        sgn.setText("");
        brg.setMaxStringLength(500);
        sgn.setFocused(false);
        sgn.setEnabled(true);
        sgn.setCanLoseFocus(true);
        sgn.setVisible(true);

        y = 150;
        GuiLabel label7 = new GuiLabel(fontRenderer, 3,width / 2 - 50, height / 2 - maxy + y ,90,10,0xd943ff64);
        label7.addLine("Spenden");
        labelList.add(label7);

        y = 165;
        spdn = new GuiTextField(4, fontRenderer, width / 2 - 50, height / 2 - maxy + y, 100, 10);
        spdn.setText("");
        brg.setMaxStringLength(500);
        spdn.setFocused(false);
        spdn.setEnabled(true);
        spdn.setCanLoseFocus(true);
        spdn.setVisible(true);

        y = 185;
        buttonList.add(new GuiButton(0,width / 2 - 50, height / 2 - maxy + y, 100, 20, "Bestätigen"));

        super.initGui();
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawRect(width / 2 - 58 , height / 2 - 103, width / 2 + 58,height / 2 + 119, 0xa6c0c0c0);
        drawRect(width / 2 - 55 , height / 2 - 100, width / 2 + 55,height / 2 + 116, 0x4ddcf527);

        fontRenderer.drawString("Gottesdienst Einteilungen", width / 2 - 61, height / 2 - 112, 0xd943ff64);
        brg.drawTextBox();
        text.drawTextBox();
        vu.drawTextBox();
        gbk.drawTextBox();
        vrab.drawTextBox();
        sgn.drawTextBox();
        spdn.drawTextBox();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        brg.mouseClicked(mouseX, mouseY, mouseButton);
        text.mouseClicked(mouseX, mouseY, mouseButton);
        vu.mouseClicked(mouseX, mouseY, mouseButton);
        gbk.mouseClicked(mouseX, mouseY, mouseButton);
        vrab.mouseClicked(mouseX, mouseY, mouseButton);
        sgn.mouseClicked(mouseX, mouseY, mouseButton);
        spdn.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id){
            case 0:
                //Begrüßung: > Texte: > VU: > GBK: > Verabschiedung: > Segen: > Spenden: >
                String begrueßung = brg.getText();
                String texte = text.getText();
                String vaterUnser = vu.getText();
                String GBK = gbk.getText();
                String verabschiedung = vrab.getText();
                String segen = sgn.getText();
                String spenden = spdn.getText();
                String fchat = "Begrüßung: "+begrueßung +
                        "> Texte: " + texte +
                        "> VU: " + vaterUnser +
                        "> GBK: " + GBK +
                        "> Verabschiedung: " + verabschiedung+
                        "> Segen: " + segen +
                        "> Spenden: "+spenden + ">";
                Minecraft.getMinecraft().player.sendChatMessage("/f "+fchat);
                Minecraft.getMinecraft().player.closeScreen();
                break;
        }
    }

    @Override
    public void updateScreen() {
        brg.updateCursorCounter();
        text.updateCursorCounter();
        vu.updateCursorCounter();
        gbk.updateCursorCounter();
        vrab.updateCursorCounter();
        sgn.updateCursorCounter();
        spdn.updateCursorCounter();
        super.updateScreen();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        brg.textboxKeyTyped(typedChar, keyCode);
        text.textboxKeyTyped(typedChar, keyCode);
        vu.textboxKeyTyped(typedChar, keyCode);
        gbk.textboxKeyTyped(typedChar, keyCode);
        vrab.textboxKeyTyped(typedChar, keyCode);
        sgn.textboxKeyTyped(typedChar, keyCode);
        spdn.textboxKeyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }
}
