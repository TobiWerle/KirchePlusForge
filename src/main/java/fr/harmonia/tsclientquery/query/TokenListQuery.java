package fr.harmonia.tsclientquery.query;

import fr.harmonia.tsclientquery.answer.TokenListAnswer;
import fr.harmonia.tsclientquery.event.EnumEvent;
import fr.harmonia.tsclientquery.objects.ParsedObject;

public class TokenListQuery extends EventAnswerQuery<TokenListAnswer> {

	public TokenListQuery() {
		super("tokenlist", EnumEvent.notifytokenlist);
		answer = new TokenListAnswer();
	}

	@Override
	public void buildAnswer(ParsedObject obj) {
		answer.addToken(obj);
	}

}
