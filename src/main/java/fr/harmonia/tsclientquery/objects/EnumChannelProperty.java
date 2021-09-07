package fr.harmonia.tsclientquery.objects;

public enum EnumChannelProperty {
	are_subscribed("are_subscribed", true),

	codec("codec"),

	codec_is_unencrypted("codec_is_unencrypted"),

	codec_latency_factor("codec_latency_factor"),

	codec_quality("codec_quality"),

	default_channel("default", true),

	description("description"),

	forced_silence("forced_silence"),

	icon_id("icon_id"),

	maxclients("maxclients"),

	maxclients_unlimited("maxclients_unlimited", true),

	maxfamilyclients("maxfamilyclients"),

	maxfamilyclients_inherited("maxfamilyclients_inherited", true),

	maxfamilyclients_unlimited("maxfamilyclients_unlimited", true),

	name("name"),

	name_phonetic("name_phonetic"),

	needed_talk_power("needed_talk_power"),

	order("order"),

	password("password", true),

	permanent("permanent", true),

	semi_permanent("semi_permanent", true),

	topic("topic");

	private boolean flag;
	private String propName;

	EnumChannelProperty(String name) {
		this(name, false);
	}

	EnumChannelProperty(String name, boolean flag) {
		this.propName = name;
		this.flag = flag;
	}

	public String getPropertyName(boolean addFlag) {
		if (flag && addFlag)
			return "channel_flag_" + propName;
		else
			return "channel_" + propName;
	}

	public boolean isFlag() {
		return flag;
	}
}
