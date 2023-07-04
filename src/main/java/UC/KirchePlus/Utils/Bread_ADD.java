package UC.KirchePlus.Utils;

import UC.KirchePlus.Events.Displayname;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bread_ADD {


    public static String Date = null;
    public static String Member = null;
    public static String Who = null;



    static String Bread_prefix = TextFormatting.GRAY + "[" + TextFormatting.GOLD + "Brot" + TextFormatting.GRAY + "]";


    public static void writeBread() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ValueRange valueRange = getSpace(getRanges());
                    if(valueRange == null){
                        Utils.displayMessage(new TextComponentString(Bread_prefix + TextFormatting.RED + "Es wurde nicht genug Platz in der Brot-Tabelle gefunden!"));
                        return;
                    }
                    List<List<Object>> values = Arrays.asList(Arrays.asList(Member, Who, Date));
                    String range = valueRange.getRange();
                    ValueRange body = new ValueRange().setValues(values);
                    UpdateValuesResponse result = TabellenMethoden.sheetsService.spreadsheets().values().update( TabellenMethoden.SPREADSHEET_ID, range, body).setValueInputOption("USER_ENTERED").execute();

                    Utils.displayMessage(new TextComponentString(Bread_prefix + TextFormatting.GREEN + " Der Spieler wurde eingetragen!"));
                    Brot_User user = new Brot_User(Member, Who, Date);
                    Who = null;
                    Date = null;
                    Member = null;
                    Displayname.refreshAll();
                }catch (Exception e){
                    e.printStackTrace();
                    if(e.getMessage().contains("400 Bad Request")){
                        Utils.displayMessage(new TextComponentString(Bread_prefix + TextFormatting.RED + "Du hast keine Rechte auf die Brot-Tabelle"));
                    }
                }
            }
        });
        thread.start();
    }

    private static List<String> getRanges(){
        String[] range = "D6:F6, D7:F7, D8:F8, D9:F9, D10:F10, D11:F11, D12:F12, D13:F13, D14:F14, D15:F15, D16:F16, D17:F17, D18:F18, D19:F19, D20:F20, D21:F21, D22:F22, D23:F23, D24:F24, D25:F25, D26:F26, D27:F27, D28:F28, D29:F29, D30:F30, D31:F31, D32:F32, D33:F33, D34:F34, D35:F35, D36:F36, D37:F37, D38:F38, D39:F39, D40:F40, D41:F41, D42:F42, D43:F43, D44:F44, D45:F45, D46:F46, D47:F47, D48:F48, D49:F49, D50:F50, D51:F51, D52:F52, D53:F53, D54:F54, D55:F55, D56:F56, D57:F57, D58:F58, D59:F59, D60:F60, D61:F61, D62:F62, D63:F63, D64:F64, D65:F65, D66:F66, D67:F67, D68:F68, D69:F69, D70:F70, D71:F71, D72:F72, D73:F73, D74:F74, D75:F75, D76:F76, D77:F77, D78:F78, D79:F79, D80:F80, D81:F81, D82:F82, D83:F83, D84:F84, D85:F85, D86:F86, D87:F87, D88:F88, D89:F89, D90:F90, D91:F91, D92:F92, D93:F93, D94:F94, D95:F95, D96:F96, D97:F97, D98:F98, D99:F99, D100:F100".split(",");
        return Arrays.asList(range);
    }

    private static ValueRange getSpace(List<String> ranges) throws IOException {
        List<String> range = new ArrayList<String>();
        for(String s : ranges){
            range.add("Brotliste!" +s.replace(",","").replace(" ", ""));
        }

        BatchGetValuesResponse result = TabellenMethoden.sheetsService.spreadsheets().values().batchGet(TabellenMethoden.SPREADSHEET_ID)
                .setRanges(range).execute();
        for(ValueRange valueRanges : result.getValueRanges()){
            if(valueRanges.getValues() == null || valueRanges.getValues().isEmpty()) {
                return valueRanges;
            }
        }
        return null;
    }
}
