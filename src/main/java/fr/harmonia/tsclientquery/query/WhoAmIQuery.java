package fr.harmonia.tsclientquery.query;

import fr.harmonia.tsclientquery.answer.WhoAmIAnswer;
import fr.harmonia.tsclientquery.objects.ParsedObject;

public class WhoAmIQuery extends Query<WhoAmIAnswer> {

	public WhoAmIQuery() {
		super("whoami");
	}

	@Override
	public void buildAnswer(ParsedObject obj) {
		answer = new WhoAmIAnswer(obj);
	}

}
