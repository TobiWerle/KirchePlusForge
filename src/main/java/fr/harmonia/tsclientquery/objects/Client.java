package fr.harmonia.tsclientquery.objects;

public class Client extends ChannelClient {
	public Client(ParsedObject object) {
		super(object);
	}

	public Client(String line) {
		super(line);
	}

	public String getClientBadges() {
		return get("client_badges");
	}

	public int getClientChannelGroupInheritedChannelId() {
		return getInteger("client_channel_group_inherited_channel_id");
	}

	public String getClientDescription() {
		return get("client_description");
	}

	public int getClientIntegrations() {
		return getInteger("client_integrations");
	}

	public String getClientMetaData() {
		return get("client_meta_data");
	}

	public String getClientMyTeamspeakAvatar() {
		return get("client_myteamspeak_avatar");
	}

	public String getClientMyTeamspeakId() {
		return get("client_myteamspeak_id");
	}

	public int getClientNeededServerQueryViewPower() {
		return getInteger("client_needed_serverquery_view_power");
	}

	public String getClientNicknamePhonetic() {
		return get("client_nickname_phonetic");
	}

	public String getClientSignedBadges() {
		return get("client_signed_badges");
	}

	public String getClientTalkRequestMessage() {
		return get("client_talk_request_msg");
	}

	public int getClientUnreadMessages() {
		return getInteger("client_unread_messages");
	}

	public boolean hasClientAvatar() {
		return getBoolean("client_flag_avatar");
	}

	public boolean isClientOutputOnlyMuted() {
		return getBoolean("client_outputonly_muted");
	}

	public boolean isClientRequestTalk() {
		return getBoolean("client_talk_request");
	}

}
