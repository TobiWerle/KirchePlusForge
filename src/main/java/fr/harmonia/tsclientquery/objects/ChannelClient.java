package fr.harmonia.tsclientquery.objects;

public class ChannelClient extends ParsedObject {
	public ChannelClient(ParsedObject object) {
		super(object);
	}

	public ChannelClient(String line) {
		super(line);
	}

	public int getChannelID() {
		return getInteger("cid");
	}

	public String getClientAwayMessage() {
		return get("client_away_message");
	}

	public int getClientChannelGroupID() {
		return getInteger("client_channel_group_id");
	}

	public String getClientCountry() {
		return get("client_country");
	}

	public int getClientDatabaseID() {
		return getInteger("client_database_id");
	}

	public int getClientIconId() {
		return getInteger("client_icon_id");
	}

	public int getClientID() {
		return getInteger("clid");
	}

	public String getClientNickname() {
		return get("client_nickname");
	}

	public String[] getClientServerGroups() {
		return get("client_servergroups").split("[,]");
	}

	public int getClientTalkPower() {
		return getInteger("client_talk_power");
	}

	public String getClientUID() {
		return get("client_unique_identifier");
	}

	public SpeakIcon getSpeakIcon() {
		return getSpeakIcon(true);
	}

	public SpeakIcon getSpeakIcon(boolean isInOurChannel) {
		if (!isClientOutputHardware())
			return SpeakIcon.SPEAKER_DISABLE;
		if (isClientOutputMuted())
			return SpeakIcon.SPEAKER_MUTED;
		if (!isClientInputHardware())
			return SpeakIcon.MICROPHONE_DISABLED;
		if (isClientMuted())
			return SpeakIcon.MICROPHONE_MUTED;
		if (isInOurChannel && isClientTalking()) {
			if (isClientChannelCommander())
				return SpeakIcon.CHANNEL_COMMANDER_SPEAKING;
			else
				return SpeakIcon.SPEAKING;
		}
		if (isClientChannelCommander())
			return SpeakIcon.CHANNEL_COMMANDER;
		else
			return SpeakIcon.IDLE;

	}

	public boolean isClientAway() {
		return getBoolean("client_away");
	}

	public boolean isClientChannelCommander() {
		return getBoolean("client_is_channel_commander");
	}

	public boolean isClientInputHardware() {
		return getBoolean("client_input_hardware");
	}

	public boolean isClientInputMuted() {
		return getBoolean("client_input_muted");
	}

	public boolean isClientMuted() {
		return getBoolean("client_is_muted");
	}

	public boolean isClientOutputHardware() {
		return getBoolean("client_output_hardware");
	}

	public boolean isClientOutputMuted() {
		return getBoolean("client_output_muted");
	}

	public boolean isClientPrioritySpeaker() {
		return getBoolean("client_is_priority_speaker");
	}

	public boolean isClientRecording() {
		return getBoolean("client_is_recording");
	}

	public boolean isClientTalker() {
		return getBoolean("client_is_talker");
	}

	public boolean isClientTalking() {
		return getBoolean("client_flag_talking");
	}

	public boolean isServerQuery() {
		return getBoolean("client_type");
	}
}
