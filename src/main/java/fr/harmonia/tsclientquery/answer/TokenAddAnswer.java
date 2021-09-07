package fr.harmonia.tsclientquery.answer;

import fr.harmonia.tsclientquery.objects.ParsedObject;

public class TokenAddAnswer extends Answer {

	public TokenAddAnswer(ParsedObject obj) {
		super(obj);
	}

	public String getToken() {
		return get("token");
	}

}
