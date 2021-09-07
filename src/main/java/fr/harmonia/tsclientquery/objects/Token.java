package fr.harmonia.tsclientquery.objects;

public class Token extends ParsedObject {
	public Token(ParsedObject p) {
		super(p);
	}

	public Token(String line) {
		super(line);
	}

	public int getChannelId() {
		return getInteger("token_id2");
	}

	public String getDescription() {
		return get("token_description");
	}

	public int getGroupId() {
		return getInteger("token_id1");
	}

	public String getToken() {
		return get("token");
	}

	public long getTokenCreatedDate() {
		return getLong("token_created");
	}

	public boolean isChannelToken() {
		return getBoolean("token_type");
	}

	public boolean isServerToken() {
		return !isChannelToken();
	}
}
