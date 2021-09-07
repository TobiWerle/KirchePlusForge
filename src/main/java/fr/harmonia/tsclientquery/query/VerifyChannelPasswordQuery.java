package fr.harmonia.tsclientquery.query;

public class VerifyChannelPasswordQuery extends NoAnswerQuery {

	public VerifyChannelPasswordQuery(int cid, String password) {
		super("verifychannelpassword");
		addArgument("cid", "cid");
		addArgument("password", password);
	}
}
