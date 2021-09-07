package fr.harmonia.tsclientquery.query;

import java.util.Arrays;
import java.util.stream.Collectors;

/*
 * Usage: clientmove cid={channelID} [cpw={channelPassword}] clid={clientID}...
 * 
 * Moves one or more clients specified with clid to the channel with ID cid. If
 * the target channel has a password, it needs to be specified with cpw. If the
 * channel has no password, the parameter can be omitted.
 * 
 * Example: clientmove cid=3 clid=5|clid=6 error id=0 msg=ok
 * 
 * 
 * error id=0 msg=ok
 * 
 */
public class ClientMoveQuery extends NoAnswerQuery {

	public ClientMoveQuery(int cid, int... clids) {
		super("clientmove");
		addArgument("cid", cid);
		addOption(Arrays.stream(clids).mapToObj(i -> "clid=" + i).collect(Collectors.joining("|")));
	}

	public ClientMoveQuery(int cid, String password, int... clids) {
		this(cid, clids);
		addArgument("cpw", password);
	}

}
