package UC.KirchePlus.Utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SpenderUtils {
    public static HashMap<String, Integer> publicDonations = new HashMap<String, Integer>();


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
