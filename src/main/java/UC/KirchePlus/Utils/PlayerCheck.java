package UC.KirchePlus.Utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;

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


    public static String NameFromUUID(String uuid) throws IOException {
        URL Fetcher = new URL("https://api.mojang.com/user/profiles/"+uuid.replace("-", "")+"/names");
        InputStreamReader reader = new InputStreamReader(Fetcher.openStream());
        BufferedReader getName = new BufferedReader(reader);
        String json = getName.readLine();
        Gson gson = new Gson();
        User[] userArray = gson.fromJson(json, User[].class);
        ArrayList<String> names = new ArrayList<>();
        for(User user : userArray){
           names.add(user.name);
        }
        return names.get(names.size()-1);
    }


    public class User {
        private long id;
        private String name;

        public long getId() {
            return id;
        }
        public void setId(long id) {
            this.id = id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
    }

}
