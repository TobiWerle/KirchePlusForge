package fr.harmonia.tsclientquery.answer;

import fr.harmonia.tsclientquery.objects.ParsedObject;

public class BanAnswer extends Answer {

	public BanAnswer(ParsedObject obj) {
		super(obj);
	}

	public int getBanId() {
		return getInteger("banid");
	}

}
