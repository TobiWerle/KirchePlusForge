package UC.KirchePlus.Utils;

import UC.KirchePlus.main.main;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;

public class Activity_User {


    String name,
    rpActivity,
    donationActivity,
    totalActivity;

    public Activity_User(String s, String s1, String s2, String s3) {
        name = s;
        rpActivity = s1;
        donationActivity = s2;
        totalActivity = s3;

    }

    public String getName() { return name; }

    public String getRpActivity() { return rpActivity; }

    public String getDonationActivity() { return donationActivity; }

    public String getTotalActivity() { return totalActivity; }



    public static ArrayList<Activity_User> getTotalActivityUsers(int place){
        int lastCount = Integer.parseInt(((Activity_User) main.totalActivity.keySet().toArray()[place]).getTotalActivity());
        ArrayList<Activity_User> users = new ArrayList<>();
        users.add((Activity_User)main.totalActivity.keySet().toArray()[place]);

        for(Activity_User user : main.totalActivity.keySet()){
            if(!users.contains(user)){
                if(Integer.parseInt(user.getTotalActivity()) == lastCount){
                    users.add(user);
                }
            }
        }
        return users;
    }

    public static Activity_User getSelf(){
        for(Activity_User user : main.totalActivity.keySet()){
            if(user.getName().equals(Minecraft.getMinecraft().player.getName())){
                return user;
            }
        }
        return null;
    }
}
