package fr.harmonia.tsclientquery.objects.builder;

import fr.harmonia.tsclientquery.objects.Ban;

public class BanBuilder {

	public static class BanBuilderNoTargetException extends IllegalArgumentException {
		private static final long serialVersionUID = 5425099683145168929L;
	}

	private String banreason;
	private String ip;
	private String regex;
	private long timeInSeconds = -1L;
	private String uid;

	public Ban build() {
		if (ip == null && regex == null && uid == null)
			throw new BanBuilderNoTargetException();
		return new Ban(banreason, ip, regex, timeInSeconds, uid);
	}

	public BanBuilder withIp(String ip) {
		this.ip = ip;
		return this;
	}

	public BanBuilder withName(String regex) {
		this.regex = regex;
		return this;
	}

	public BanBuilder withReason(String banreason) {
		this.banreason = banreason;
		return this;
	}

	public BanBuilder withTime(long timeInSeconds) {
		this.timeInSeconds = timeInSeconds;
		return this;
	}

	public BanBuilder withUID(String uid) {
		this.uid = uid;
		return this;
	}

}
