package fr.harmonia.tsclientquery.objects;

public class Channel extends ParsedObject {

	public Channel(ParsedObject p) {
		super(p);
	}

	public Channel(String line) {
		super(line);
	}

	public int getChannelCodec() {
		return getInteger("channel_codec");
	}

	public int getChannelCodecLatencyFactor() {
		return getInteger("channel_codec_latency_factor");
	}

	public int getChannelCodecQuality() {
		return getInteger("channel_codec_quality");
	}

	public String getChannelDescription() {
		return get("channel_description");
	}

	public int getChannelIconId() {
		return getInteger("channel_icon_id");
	}

	public int getChannelId() {
		return getInteger("cid");
	}

	public int getChannelMaxClients() {
		return getInteger("channel_maxclients");
	}

	public int getChannelMaxFamilyClients() {
		return getInteger("channel_maxfamilyclients");
	}

	public String getChannelName() {
		return get("channel_name");
	}

	public String getChannelNamePhonetic() {
		return get("channel_name_phonetic");
	}

	public int getChannelNeededTalkPower() {
		return getInteger("channel_needed_talk_power");
	}

	public int getChannelOrder() {
		return getInteger("channel_order");
	}

	public int getChannelParentId() {
		return getInteger("cpid");
	}

	public String getChannelTopic() {
		return get("channel_topic");
	}

	public boolean hasChannelForcedSilence() {
		return getBoolean("channel_forced_silence");
	}

	public boolean hasChannelPassword() {
		return getBoolean("channel_flag_password");
	}

	public boolean hasChannelUnlimitedMaxClients() {
		return getBoolean("channel_flag_maxclients_unlimited");
	}

	public boolean hasChannelUnlimitedMaxFamilyClients() {
		return getBoolean("channel_flag_maxfamilyclients_unlimited");
	}

	public boolean isChannelAreSubscribed() {
		return getBoolean("channel_flag_are_subscribed");
	}

	public boolean isChannelCodecIsUnencrypted() {
		return getBoolean("channel_codec_is_unencrypted");
	}

	public boolean isChannelDefault() {
		return getBoolean("channel_flag_default");
	}

	public boolean isChannelMaxFamilyClientsInherited() {
		return getBoolean("channel_flag_maxfamilyclients_inherited");
	}

	public boolean isChannelPermanent() {
		return getBoolean("channel_flag_permanent");
	}

	public boolean isChannelSemiPermanent() {
		return getBoolean("channel_flag_semi_permanent");
	}
}
