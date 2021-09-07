package fr.harmonia.tsclientquery.answer;

import fr.harmonia.tsclientquery.objects.ParsedObject;
import fr.harmonia.tsclientquery.objects.Token;

import java.util.List;

public class TokenListAnswer extends Answer {
	private List<Token> list;

	public TokenListAnswer() {
		super("");
	}

	public void addToken(ParsedObject obj) {
		list.add(new Token(obj));
	}

	public List<Token> getTokenList() {
		return list;
	}

}
