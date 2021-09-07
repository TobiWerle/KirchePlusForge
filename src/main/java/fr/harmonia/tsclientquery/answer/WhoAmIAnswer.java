package fr.harmonia.tsclientquery.answer;

import fr.harmonia.tsclientquery.objects.ParsedObject;

public class WhoAmIAnswer extends Answer {

	public WhoAmIAnswer(ParsedObject obj) {
		super(obj);
	}

	public int getChannelID() {
		return getInteger("cid");
	}

	public int getClientID() {
		return getInteger("clid");
	}

}
