package UC.KirchePlus.Utils;

import UC.KirchePlus.Events.Displayname;

public class Brot_User {

    String member,
            Empfänger,
            Datum;

    public Brot_User(String s, String s2, String s3) {
        member = s;
        Empfänger = s2;
        Datum = s3;
        Displayname.BrotUser.put(s2, this);
    }


    public String getMember() {
        return member;
    }


    public String getEmpfänger() {
        return Empfänger;
    }


    public String getDatum() {
        return Datum;
    }
}