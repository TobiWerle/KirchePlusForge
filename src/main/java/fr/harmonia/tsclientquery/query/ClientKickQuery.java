package fr.harmonia.tsclientquery.query;

import java.util.Arrays;
import java.util.stream.Collectors;

/*
 * help clientkick Usage: clientkick reasonid={4|5} [reasonmsg={text}]
 * clid={clientID}...
 * 
 * Kicks one or more clients specified with clid from their currently joined
 * channel or from the server, depending on reasonid. The reasonmsg parameter
 * specifies a text message sent to the kicked clients. This parameter is
 * optional and may only have a maximum of 40 characters.
 * 
 * Available reasonid values are: 4: Kick the client from his current channel
 * into the default channel 5: Kick the client from the server
 * 
 * Example: clientkick reasonid=4 reasonmsg=Go\saway! clid=5|clid=6 error id=0
 * msg=ok
 * 
 * 
 * error id=0 msg=ok
 */
public class ClientKickQuery extends NoAnswerQuery {
	public enum Reason {
		CHANNEL(4), SERVER(5);

		private int id;

		Reason(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}
	}

	public ClientKickQuery(Reason reasonid, int... clids) {
		super("clientkick");
		addArgument("reasonid", reasonid.getId());
		if (clids.length == 0)
			throw new IllegalArgumentException("clids can't be empty");
		addOption(Arrays.stream(clids).mapToObj(i -> "clid=" + i).collect(Collectors.joining("|")));
	}

	public ClientKickQuery(Reason reasonid, String reasonmsg, int... clids) {
		this(reasonid, clids);
		addArgument("reasonmsg", reasonmsg);
	}

}
