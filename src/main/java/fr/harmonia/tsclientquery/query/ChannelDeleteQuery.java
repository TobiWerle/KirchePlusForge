package fr.harmonia.tsclientquery.query;

/*
 * Usage: channeldelete cid={channelID} force={1|0}
 * 
 * Deletes an existing channel by ID. If force is set to 1, the channel will be
 * deleted even if there are clients within.
 * 
 * Example: channeldelete cid=16 force=1 error id=0 msg=ok
 * 
 * error id=0 msg=ok
 * 
 */
public class ChannelDeleteQuery extends NoAnswerQuery {

	public ChannelDeleteQuery(int cid, boolean force) {
		super("channeldelete");
		addArgument("cid", cid);
		addArgument("force", force);
	}

}
