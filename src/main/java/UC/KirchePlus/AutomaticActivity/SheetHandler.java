package UC.KirchePlus.AutomaticActivity;

import UC.KirchePlus.Utils.TabellenMethoden;
import UC.KirchePlus.Utils.Utils;
import com.google.api.services.sheets.v4.model.*;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SheetHandler {

    public enum activityTypes{
        SHG,
        JGA,
        TAFEL,
        KAFFEKUCHEN,
        SPENDEEVENT,
        BEICHTEVENT,
        GOTTESDIENST,
        SPENDE,
        BIBEL,
        ABLASSBRIEF,
        SEGEN,
        TAUFE,
        CMDMARRY,
        HOCHZEIT;
    }

    public static Sheet MemberSheet = null;
    private static int fixtrys = 0;
    public static void getMemberOwnSheet() throws IOException {
        Spreadsheet sp = TabellenMethoden.sheetsService.spreadsheets().get(TabellenMethoden.SPREADSHEET_ID).execute();
        List<Sheet> sheets = sp.getSheets();
        for(Sheet sheet : sheets){
            String[] get = {"#0","#1","#2", "#3", "#4", "#5", "#6"};
            String name = sheet.getProperties().getTitle();
            if(Arrays.stream(get).anyMatch(name::contains) && !name.toLowerCase().contains("frei")){
                if(name.contains(Minecraft.getMinecraft().player.getName())){
                    MemberSheet = sheet;
                    System.out.println("Sheet gefunden; " + sheet.getProperties().getTitle());
                }
            }
        }
    }

    public static int getRang() throws IOException {
        if(MemberSheet == null){
            getMemberOwnSheet();
        }
        String rang = String.valueOf(MemberSheet.getProperties().getTitle().charAt(1));
        System.out.println("Dein Rang ist: " + Integer.parseInt(rang));
        return Integer.parseInt(rang);
    }

    public static void saveActivity(activityTypes type) throws IOException {
        Utils.displayMessage(new TextComponentString(TextFormatting.AQUA + "Deine Aktivität wird eingetragen..."));
        Thread thread = new Thread(){
            @Override
            public void run() {

                String screenshot = null;
                try {
                    screenshot = Handler.screenshot(SaveActivity_Command.image);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(screenshot.equals("Nicht erstellt!")){
                    Utils.displayMessage(new TextComponentString(TextFormatting.RED + "Der Screenshot konnte nicht hochgeladen werden! Versuche es erneut!"));
                    return;
                }
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyy");
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                LocalDateTime now = LocalDateTime.now();
                String time = timeFormatter.format(now).toString();
                String date = dateFormatter.format(now).toString();

                if(type == activityTypes.TAFEL || type == activityTypes.SHG || type == activityTypes.SPENDEEVENT || type == activityTypes.BEICHTEVENT ){
                    List<List<Object>> values = Arrays.asList(Arrays.asList(date, time ,screenshot));
                    try {
                        writeEventActivity(type, getRange(type), values);
                    } catch (IOException e) {
                        Utils.displayMessage(new TextComponentString(TextFormatting.RED + "Es ist ein unerwarteter Fehler aufgetreten!"));
                        tryUpdateSheetName(e.toString(), type);
                        e.printStackTrace();
                    }
                }

                if(type == activityTypes.JGA || type == activityTypes.KAFFEKUCHEN || type == activityTypes.GOTTESDIENST){
                    List<List<Object>> values = Arrays.asList(Arrays.asList(date, time, Handler.topic ,screenshot));
                    try {
                        writeEventActivity(type, getRange(type), values);
                    } catch (IOException e) {
                        Utils.displayMessage(new TextComponentString(TextFormatting.RED + "Es ist ein unerwarteter Fehler aufgetreten!"));
                        tryUpdateSheetName(e.toString(), type);
                        e.printStackTrace();
                    }
                }
                if(type == activityTypes.BIBEL || type == activityTypes.SPENDE){
                    List<List<Object>> values = Arrays.asList(Arrays.asList(date, Handler.topic, ""+Handler.amount ,screenshot));
                    try {
                        writeEventActivity(type, getRange(type), values);
                    } catch (IOException e) {
                        Utils.displayMessage(new TextComponentString(TextFormatting.RED + "Es ist ein unerwarteter Fehler aufgetreten!"));
                        tryUpdateSheetName(e.toString(), type);
                        e.printStackTrace();
                    }
                }
                if(type == activityTypes.ABLASSBRIEF){
                    List<List<Object>> values = Arrays.asList(Arrays.asList(date, Handler.topic, screenshot));
                    try {
                        writeEventActivity(type, getRange(type), values);
                    } catch (IOException e) {
                        Utils.displayMessage(new TextComponentString(TextFormatting.RED + "Es ist ein unerwarteter Fehler aufgetreten!"));
                        tryUpdateSheetName(e.toString(), type);
                        e.printStackTrace();
                    }
                }
                if(type == activityTypes.SEGEN){
                    List<List<Object>> values = Arrays.asList(Arrays.asList(date, time, screenshot));
                    try {
                        writeEventActivity(type, getRange(type), values);
                    } catch (IOException e) {
                        Utils.displayMessage(new TextComponentString(TextFormatting.RED + "Es ist ein unerwarteter Fehler aufgetreten!"));
                        tryUpdateSheetName(e.toString(), type);
                        e.printStackTrace();
                    }
                }
                if(type == activityTypes.TAUFE){
                    List<List<Object>> values = Arrays.asList(Arrays.asList(date, Handler.topic, screenshot));
                    try {
                        writeEventActivity(type, getRange(type), values);
                    } catch (IOException e) {
                        Utils.displayMessage(new TextComponentString(TextFormatting.RED + "Es ist ein unerwarteter Fehler aufgetreten!"));
                        tryUpdateSheetName(e.toString(), type);
                        e.printStackTrace();
                    }
                }
                if(type == activityTypes.CMDMARRY || type == activityTypes.HOCHZEIT){
                    List<List<Object>> values = Arrays.asList(Arrays.asList(date, time, Handler.topic, screenshot));
                    try {
                        writeEventActivity(type, getRange(type), values);
                    } catch (IOException e) {
                        Utils.displayMessage(new TextComponentString(TextFormatting.RED + "Es ist ein unerwarteter Fehler aufgetreten!"));
                        tryUpdateSheetName(e.toString(), type);
                        e.printStackTrace();
                    }
                }

                Handler.screenshotLink = "";
                Handler.topic = "";
                Handler.amount = 0;
                fixtrys = 0;
            }
        };
        thread.start();
    }



    private static void tryUpdateSheetName(String error, activityTypes type) {
        if(error.contains("Unable to parse range")){
            if(fixtrys == 1){
                Utils.displayMessage(new TextComponentString(TextFormatting.YELLOW + "Der Fehler wurde nicht gefunden. Ein Neustart des Spiels wird dies beheben."));
                return;
            }
            fixtrys = 1;
            try {
                Utils.displayMessage(new TextComponentString(TextFormatting.YELLOW + "Es wird versucht die Aktivität erneut einzutragen..."));
                getMemberOwnSheet();
                saveActivity(type);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static List<String> getRange(activityTypes type){
        if(type == activityTypes.JGA){
            return Arrays.asList("F35:I35","F36:I36","F37:I37","F38:I38","F39:I39","F40:I40","F41:I41","F42:I42");
        }
        if(type == activityTypes.SHG){
            return Arrays.asList("B48:D48","B49:D49","B50:D50","B51:D51","B52:D52","B53:D53","B54:D54","B55:D55");
        }
        if(type == activityTypes.TAFEL){
            return Arrays.asList("B35:D35","B36:D36","B37:D37","B38:D38","B39:D39","B40:D40","B41:D41","B42:D42");
        }
        if(type == activityTypes.KAFFEKUCHEN){
            return Arrays.asList("F48:I48","F49:I49","F50:I50","F51:I51","F52:I52","F53:I53","F54:I54","F55:I55");
        }
        if(type == activityTypes.SPENDEEVENT){
            return Arrays.asList("B61:D61","B62:D62","B63:D63","B64:D64","B65:D65","B66:D66","B67:D67","B68:D68");
        }
        if(type == activityTypes.BEICHTEVENT){
            return Arrays.asList("G61:I61","G62:I62","G63:I63","G64:I64","G65:I65","G66:I66","G67:I67","G68:I68");
        }
        if(type == activityTypes.GOTTESDIENST){
            return Arrays.asList("F24:I24","F25:I25","F26:I26","F27:I27","F28:I28","F29:I29");
        }
        if(type == activityTypes.SPENDE){
            return Arrays.asList("B76:E76","B77:E77","B78:E78","B79:E79","B80:E80","B81:E81","B82:E82","B83:E83", "B84:E84", "B85:E85", "B86:E86", "B87:E87", "B88:E88", "B89:E89", "B90:E90", "B91:E91"
            ,"G76:J76","G77:J77","G78:J78","G79:J79","G80:J80","G81:J81","G82:J82","G83:J83", "G84:J84", "G85:J85", "G86:J86", "G87:J87", "G88:J88", "G89:J89", "G90:J90", "G91:J91");
        }
        if(type == activityTypes.BIBEL){
            return Arrays.asList("I168:L168", "I169:L169", "I170:L170", "I171:L171", "I172:L172", "I173:L173",
                    "I174:L174", "I175:L175", "I176:L176", "I177:L177", "I178:L178", "I179:L179", "I180:L180", "I181:L181", "I182:L182", "I183:L183");
        }
        if(type == activityTypes.ABLASSBRIEF){
            return Arrays.asList("I190:K190", "I191:K191", "I192:K192", "I193:K193", "I194:K194", "I195:K195",
                    "I196:K196", "I197:K197", "I198:K198", "I199:K199", "I200:K200", "I201:K201", "I202:K202", "I203:K203", "I204:K204", "I205:K205");
        }
        if(type == activityTypes.SEGEN){
            return Arrays.asList("B136:D136", "B137:D137", "B138:D138", "B139:D139", "B140:D140", "B141:D141",
                    "B142:D142", "B143:D143", "B144:D144", "B145:D145", "B146:D146", "B147:D147", "B148:D148", "B149:D149");
        }
        if(type == activityTypes.TAUFE){
            return Arrays.asList("H119:J119", "H120:J120", "H121:J121", "H122:J122", "H123:J123", "H124:J124",
                    "H125:J125", "H126:J126", "H127:J127", "H128:J128", "H129:J129", "H130:J130");
        }

        if(type == activityTypes.HOCHZEIT){
            return Arrays.asList("B98:E98","B99:E99", "B100:E100", "B101:E101", "B102:E102", "B103:E103", "B104:E104", "B105:E105",
                    "B106:E106", "B107:E107", "B108:E108", "B109:E109", "B110:E110", "B111:E111", "B112:E112", "B113:E113");
        }
        if(type == activityTypes.CMDMARRY){
            return Arrays.asList("B119:E119", "B120:E120", "B121:E121", "B122:E122", "B123:E123", "B124:E124", "B125:E125", "B126:E126",
                    "B127:E127", "B128:E128", "B129:E129", "B130:E130");
        }

        return null;
    }

    private static void writeEventActivity(activityTypes type, List<String> ranges, List<List<Object>> values) throws IOException {
        ValueRange valueRange = getSpace(ranges);
        if(valueRange == null){
            Utils.displayMessage(new TextComponentString(TextFormatting.RED + "Es wurde nicht genug Platz in deinem Aktinachweis gefunden!"));
            return;
        }
        String range = valueRange.getRange();
        ValueRange body = new ValueRange().setValues(values);

        UpdateValuesResponse result = TabellenMethoden.sheetsService.spreadsheets().values().update( TabellenMethoden.SPREADSHEET_ID, range, body).setValueInputOption("USER_ENTERED").execute();
        Utils.displayMessage(new TextComponentString(TextFormatting.AQUA + "Deine Aktivität wurde eingetragen!"));
    }

    private static ValueRange getSpace(List<String> ranges) throws IOException {

        List<String> range = new ArrayList<String>();
        for(String s : ranges){
            range.add(MemberSheet.getProperties().getTitle()+ "!" +s);
        }

        BatchGetValuesResponse result = TabellenMethoden.sheetsService.spreadsheets().values().batchGet(TabellenMethoden.SPREADSHEET_ID)
                .setRanges(range).execute();
        for(ValueRange valueRanges : result.getValueRanges()){
            if(valueRanges.getValues() == null || valueRanges.getValues().isEmpty()) {
                System.out.println(valueRanges.getRange());
                return valueRanges;
            }
        }
        return null;
    }
}