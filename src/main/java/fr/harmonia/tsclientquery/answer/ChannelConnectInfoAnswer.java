package fr.harmonia.tsclientquery.answer;

import fr.harmonia.tsclientquery.objects.ParsedObject;

public class ChannelConnectInfoAnswer extends Answer {

	public ChannelConnectInfoAnswer(ParsedObject obj) {
		super(obj);
	}

	public String getPassword() {
		return get("password");
	}

	public String getPath() {
		return get("path");
	}
}
