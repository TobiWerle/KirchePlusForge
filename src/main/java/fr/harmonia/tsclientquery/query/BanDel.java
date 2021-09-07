package fr.harmonia.tsclientquery.query;

/*
 * help bandel Usage: bandel banid={banID}
 * 
 * Deletes the ban rule with ID banid from the server.
 * 
 * Example: bandel banid=3 error id=0 msg=ok
 * 
 * error id=0 msg=ok
 * 
 */
public class BanDel extends NoAnswerQuery {

	public BanDel(int banid) {
		super("bandel");
		addArgument("banid", banid);
	}

}
