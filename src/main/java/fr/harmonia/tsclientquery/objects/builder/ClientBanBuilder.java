package fr.harmonia.tsclientquery.objects.builder;

import fr.harmonia.tsclientquery.objects.ClientBan;

public class ClientBanBuilder {
	public static class ClientBanBuilderNoTargetException extends IllegalArgumentException {
		private static final long serialVersionUID = -420909279135279327L;

	}

	private String banreason;
	private int cldbid;
	private int clid;
	private long timeInSeconds = -1;
	private String uid;

	public ClientBan build() {
		if (clid < 0 && cldbid < 0 && uid == null)
			throw new ClientBanBuilderNoTargetException();

		return new ClientBan(banreason, cldbid, clid, timeInSeconds, uid);
	}

	public ClientBanBuilder withCldbid(int cldbid) {
		this.cldbid = cldbid;
		return this;
	}

	public ClientBanBuilder withClid(int clid) {
		this.clid = clid;
		return this;
	}

	public ClientBanBuilder withReason(String banreason) {
		this.banreason = banreason;
		return this;
	}

	public ClientBanBuilder withTime(long timeInSeconds) {
		this.timeInSeconds = timeInSeconds;
		return this;
	}

	public ClientBanBuilder withUID(String uid) {
		this.uid = uid;
		return this;
	}
}
