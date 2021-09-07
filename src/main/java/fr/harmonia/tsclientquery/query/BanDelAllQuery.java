package fr.harmonia.tsclientquery.query;

/*
 * help bandelall Usage: bandelall
 * 
 * Deletes all active ban rules from the server.
 * 
 * Example: bandelall error id=0 msg=ok
 * 
 * error id=0 msg=ok
 * 
 */
public class BanDelAllQuery extends NoAnswerQuery {

	public BanDelAllQuery() {
		super("bandelall");
	}

}
