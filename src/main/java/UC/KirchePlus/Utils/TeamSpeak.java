package UC.KirchePlus.Utils;

import UC.KirchePlus.main.main;
import fr.harmonia.tsclientquery.TSClientQuery;
import fr.harmonia.tsclientquery.answer.ChannelDescriptionAnswer;
import fr.harmonia.tsclientquery.query.ChannelDescription;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TeamSpeak {
    public static HashMap<String, Integer> publicDonations = new HashMap<String, Integer>();

    public static void connect(String queryKey) {
        if(main.client == null){
            main.client = new TSClientQuery(queryKey);
            try {
                main.client.start();
            } catch (IOException e) {System.err.println("Fehler beim verbinden der TeamSpeak Client Query!");}
        }
    }


    public static void loadDescription() {
        publicDonations.clear();
        ChannelDescriptionAnswer test = main.client.sendQuery(new ChannelDescription());
        String desc = test.getChannelDescription();
        String[] split = desc.split("\n");
        for (String s : split) {
            if (s.contains("$")){
                if(!s.contains("ab")){
                    try {
                        String info = s;
                        info = info.replace("[B]", "").replace("[/B]", "").replace("$", "");
                        for(int i = 0; i < info.length(); i++){
                            if(info.startsWith(" ")){
                                info = info.replaceFirst(" ", "");
                            }
                        }

                        String[] splitInfo = info.split(" ");

                        String name = "";
                        int amount = 0;
                        if(splitInfo[1].contains("[UC]")){
                            name = splitInfo[2];
                            amount = Integer.parseInt(splitInfo[4].replace(".", ""));
                        }else{
                            name = splitInfo[1];
                            amount = Integer.parseInt(splitInfo[3].replace(".", ""));
                        }
                        publicDonations.put(name.toLowerCase(Locale.ROOT), amount);
                    }catch (Exception e) {}
                }
            }
        }
        System.out.println("Done TeamSpeak");
    }

    private static boolean isInPublic(String name){
        return publicDonations.containsKey(name.toLowerCase(Locale.ROOT));
    }

    public static Integer getAmountByName(String name){
        for(Map.Entry map : publicDonations.entrySet()){
            if(map.getKey().toString().equalsIgnoreCase(name)){
                return Integer.valueOf(map.getValue().toString());
            }
        }
        return 0;
    }

}
