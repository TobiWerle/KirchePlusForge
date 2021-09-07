package fr.harmonia.tsclientquery.objects;

/*
 * banid=2 ip name=Le\struc uid mytsid lastnickname created=1565268333
 * duration=0
 * 
 * invokername=ATE47 invokercldbid=2 invokeruid=5H1C8xKIFE3TQqp7i7P3IE7Jtgk=
 * reason=Michel enforcements=0
 */
public class DataBaseBan {
	private int banid;
	private long created;
	private long duration;
	private int enforcements;
	private int invokercldbid;
	private String invokername;
	private String invokeruid;
	private String ip;
	private String lastnickname;
	private String name;
	private String reason;
	private String uid;

	public DataBaseBan(int banid, String ip, String name, String uid, String lastnickname, long created, long duration,
			String invokername, int invokercldbid, String invokeruid, String reason, int enforcements) {
		this.banid = banid;
		this.ip = ip;
		this.name = name;
		this.uid = uid;
		this.lastnickname = lastnickname;
		this.created = created;
		this.duration = duration;
		this.invokername = invokername;
		this.invokercldbid = invokercldbid;
		this.invokeruid = invokeruid;
		this.reason = reason;
		this.enforcements = enforcements;
	}

	public int getBanId() {
		return banid;
	}

	public long getCreateDate() {
		return created;
	}

	public long getDuration() {
		return duration;
	}

	public int getEnforcements() {
		return enforcements;
	}

	public int getInvokerClientDatabaseID() {
		return invokercldbid;
	}

	public String getInvokerNickname() {
		return invokername;
	}

	public String getInvokerUID() {
		return invokeruid;
	}

	public String getReason() {
		return reason;
	}

	public String getTargetIP() {
		return ip;
	}

	public String getTargetLastNickname() {
		return lastnickname;
	}

	public String getTargetName() {
		return name;
	}

	public String getTargetUID() {
		return uid;
	}

	@Override
	public String toString() {
		return "DataBaseBan [banid=" + banid + ", created=" + created + ", duration=" + duration + ", enforcements="
				+ enforcements + ", invokercldbid=" + invokercldbid + ", invokername=" + invokername + ", invokeruid="
				+ invokeruid + ", lastnickname=" + lastnickname + ", name=" + name + ", reason=" + reason + ", uid="
				+ uid + "]";
	}

}
