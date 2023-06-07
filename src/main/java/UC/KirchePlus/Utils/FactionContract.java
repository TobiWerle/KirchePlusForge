package UC.KirchePlus.Utils;

import UC.KirchePlus.main.main;

public class FactionContract {
    String faction;
    boolean contract;
    String[] conditions;

    public boolean isContract() {
        return contract;
    }

    public String[] getConditions() {
        return conditions;
    }

    public String getFaction() {
        return faction;
    }

    public FactionContract(String faction, boolean contract, String[] conditions){
        this.faction = faction;
        this.contract = contract;
        this.conditions = conditions;

        main.FactionContracs.add(this);
    }
}
