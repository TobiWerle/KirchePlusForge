package fr.harmonia.tsclientquery.query;

public class TokenDeleteQuery extends NoAnswerQuery {

	public TokenDeleteQuery(String token) {
		super("tokendelete");
		addArgument("token", token);
	}

}
