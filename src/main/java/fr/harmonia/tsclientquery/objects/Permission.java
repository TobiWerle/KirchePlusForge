package fr.harmonia.tsclientquery.objects;

public class Permission {
	private int permid, permvalue;
	private boolean permnegated, permskip;

	public Permission(int permid, int permvalue, boolean negated, boolean skip) {
		this.permid = permid;
		this.permvalue = permvalue;
		this.permnegated = negated;
		this.permskip = skip;
	}

	public int getID() {
		return permid;
	}

	public int getValue() {
		return permvalue;
	}

	public boolean isNegated() {
		return permnegated;
	}

	public boolean isSkip() {
		return permskip;
	}
}
