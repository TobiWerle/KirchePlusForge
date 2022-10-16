package UC.KirchePlus.Utils;

import UC.KirchePlus.main.main;

public class Akti_User {


    String name;

    String rp_aktis;

    String spenden_aktis;

    String gesamt_aktis;


    public Akti_User(String s, String s1, String s2, String s3) {


        name = s;

        rp_aktis = s1;

        spenden_aktis = s2;

        gesamt_aktis = s3;

    }



    public String getName() { return name; }

    public String getRp_aktis() { return rp_aktis; }

    public String getSpenden_aktis() { return spenden_aktis; }

    public String getGesamt_aktis() { return gesamt_aktis; }
}
