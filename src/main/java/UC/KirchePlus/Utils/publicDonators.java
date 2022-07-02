package UC.KirchePlus.Utils;

public class publicDonators {

    String name;
    String uuid;
    int amount;

    public publicDonators(String name,String UUID, int amount){
        this.name = name;
        this.amount = amount;
        this.uuid = UUID;
    }

    public String getName() {
        return name;
    }
    public String getUUID() {
        return uuid;
    }
    public int getAmount() {
        return amount;
    }
}
