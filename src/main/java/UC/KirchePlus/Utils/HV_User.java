package UC.KirchePlus.Utils;

import UC.KirchePlus.Events.Displayname;

public class HV_User {
	
	String name;
	String von;
	String grund;
	String wann;
	String bis;
	String dauer;
	
	
	public HV_User(String s, String s2, String s3, String s4,String s5, String s6) {
		name = s;
		von = s2;
		grund = s3;
		wann = s4;
		bis = s5;
		dauer = s6;
		Displayname.HVs.put(s, this);
	}


	public String getName() {
		return name;
	}


	public String getVon() {
		return von;
	}


	public String getGrund() {
		return grund;
	}


	public String getWann() {
		return wann;
	}


	public String getBis() {
		return bis;
	}


	public String getDauer() {
		return dauer;
	}


}
