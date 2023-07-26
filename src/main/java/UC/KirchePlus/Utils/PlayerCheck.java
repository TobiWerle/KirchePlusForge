package UC.KirchePlus.Utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.stream.Collectors;

public class PlayerCheck {

    public static boolean checkName(String name){
        try {
            URL Fetcher = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            InputStreamReader reader = new InputStreamReader(Fetcher.openStream());
            BufferedReader getName = new BufferedReader(reader);
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

    public static String NameFromUUID(String uuid) throws IOException {
        URL Fetcher = new URL("https://sessionserver.mojang.com/session/minecraft/profile/"+uuid.replace("-", ""));
        InputStreamReader reader = new InputStreamReader(Fetcher.openStream());
        BufferedReader getName = new BufferedReader(reader);
        String json = getName.lines().collect(Collectors.joining());
        System.out.println(json);
        Gson gson = new Gson();
        return getNameFromJSON(json);
    }
}
