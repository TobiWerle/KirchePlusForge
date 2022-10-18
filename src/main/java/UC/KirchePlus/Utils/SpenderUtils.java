package UC.KirchePlus.Utils;

import java.util.ArrayList;
public class SpenderUtils {
    public static ArrayList<publicDonators> publicDonations = new ArrayList<>();

    private static boolean isInPublic(String name){
        ArrayList<String> names = new ArrayList<>();
        for(publicDonators info : publicDonations){
            names.add(info.getName());
        }
        return names.contains(name);
    }

    public static Integer getAmountByName(String name){
        for(publicDonators info : publicDonations){
            if(info.getName().equalsIgnoreCase(name)){
                return info.getAmount();
            }
        }
        return 0;
    }

}
