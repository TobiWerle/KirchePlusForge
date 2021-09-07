package fr.harmonia.tsclientquery.query;

/*
 * help clientpoke Usage: clientpoke msg={txt} clid={clientID}
 * 
 * Sends a poke message to the client specified with clid.
 * 
 * Example: clientpoke msg=Wake\sup! clid=5 error id=0 msg=ok
 * 
 * 
 * error id=0 msg=ok
 */
public class ClientPokeQuery extends NoAnswerQuery {

	public ClientPokeQuery(String msg, int clid) {
		super("clientpoke");
		addArgument("clid", clid);
		addArgument("msg", msg);
	}
}
