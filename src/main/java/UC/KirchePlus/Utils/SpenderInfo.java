package UC.KirchePlus.Utils;

import UC.KirchePlus.main.main;

public class SpenderInfo {

    String name;
    int amount;

    public SpenderInfo(String name, String amount){
        this.name = name;
        this.amount = Integer.parseInt(amount);
    }

    public String getName() {
        return name;
    }
    public void addAmount(int amount){
        this.amount = this.amount+amount;
    }
    public int getAmount() {
        return amount;
    }

    public static boolean exists(String name){
        for(SpenderInfo info : main.spender){
            if(info.name.equalsIgnoreCase(name)){
                return true;
            }
        }
        return false;
    }
    public static SpenderInfo getByName(String name){
        for(SpenderInfo info : main.spender){
            if(info.name.equalsIgnoreCase(name)){
                return info;
            }
        }
        return null;
    }
}
