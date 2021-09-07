package UC.KirchePlus.Utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class PlayerCheck {

    public static boolean checkName(String name){
        try {
            URL Fetcher = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            InputStreamReader eader = new InputStreamReader(Fetcher.openStream());
            BufferedReader getName = new BufferedReader(eader);
            String line = getName.readLine();
            if(line != null){
                if(getNameFromJSON(line).equals(name)) {
                    return true;
                }
                return false;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    private static String getNameFromJSON(String json){
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
        return jsonObject.get("name").toString().replace("\"","");
    }


}
