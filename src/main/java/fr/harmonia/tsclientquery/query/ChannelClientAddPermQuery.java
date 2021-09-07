package fr.harmonia.tsclientquery.query;

/*
 * help channelclientaddperm Usage: channelclientaddperm cid={channelID}
 * cldbid={clientDBID} ( permid={permID}|permsid={permName}
 * permvalue={permValue} )...
 * 
 * Adds a set of specified permissions to a client in a specific channel.
 * Multiple permissions can be added by providing the two parameters of each
 * permission. A permission can be specified by permid or permsid.
 * 
 * Example: channelclientaddperm cid=12 cldbid=3 permsid=i_icon_id permvalue=100
 * error id=0 msg=ok
 * 
 * 
 * error id=0 msg=ok
 * 
 */
public class ChannelClientAddPermQuery extends AddPermQuery<ChannelClientAddPermQuery> {

	public ChannelClientAddPermQuery(int cid, int cldbid, int permid, int permvalue) {
		super("channelclientaddperm", permid, permvalue);
		addArgument("cid", cid);
		addArgument("cldbid", cldbid);
	}

	public ChannelClientAddPermQuery(int cid, int cldbid, String permsid, int permvalue) {
		super("channelclientaddperm", permsid, permvalue);
		addArgument("cid", cid);
		addArgument("cldbid", cldbid);
	}

}
