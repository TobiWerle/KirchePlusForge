package fr.harmonia.tsclientquery.query;

/*
 * help channeladdperm Usage: channeladdperm cid={channelID} (
 * permid={permID}|permsid={permName} permvalue={permValue} )...
 * 
 * Adds a set of specified permissions to a channel. Multiple permissions can be
 * added by providing the two parameters of each permission. A permission can be
 * specified by permid or permsid.
 * 
 * Example: channeladdperm cid=16 permsid=i_client_needed_join_power
 * permvalue=50 error id=0 msg=ok
 * 
 * 
 * error id=0 msg=ok
 */
public class ChannelAddPermQuery extends AddPermQuery<ChannelAddPermQuery> {

	public ChannelAddPermQuery(int channelId, int permid, int permvalue) {
		super("channeladdperm", permid, permvalue);
		addArgument("cid", channelId);
	}

	public ChannelAddPermQuery(int channelId, String permsid, int permvalue) {
		super("channeladdperm", permsid, permvalue);
		addArgument("cid", channelId);
	}

}
