package fr.harmonia.tsclientquery.query;

public class TokenUseQuery extends NoAnswerQuery {

	public TokenUseQuery(String token) {
		super("tokenuse");
		addArgument("token", token);
	}

}
