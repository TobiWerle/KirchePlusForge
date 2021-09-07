package fr.harmonia.tsclientquery.query;

public class VerifyServerPasswordQuery extends NoAnswerQuery {

	public VerifyServerPasswordQuery(String password) {
		super("verifyserverpassword");
		addArgument("password", password);
	}

}
