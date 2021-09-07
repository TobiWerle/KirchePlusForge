package fr.harmonia.tsclientquery.objects;

public class ClientBan {
	private String banreason;
	private int cldbid;
	private int clid;
	private long timeInSeconds = -1;
	private String uid;

	public ClientBan(String banreason, int cldbid, int clid, long timeInSeconds, String uid) {
		this.banreason = banreason;
		this.cldbid = cldbid;
		this.clid = clid;
		this.timeInSeconds = timeInSeconds;
		this.uid = uid;
	}

	public String getBanReason() {
		return banreason;
	}

	public int getClientDatabaseID() {
		return cldbid;
	}

	public int getClientID() {
		return clid;
	}

	public long getTimeInSeconds() {
		return timeInSeconds;
	}

	public String getUid() {
		return uid;
	}

	public boolean isInfinite() {
		return timeInSeconds <= 0;
	}
}
