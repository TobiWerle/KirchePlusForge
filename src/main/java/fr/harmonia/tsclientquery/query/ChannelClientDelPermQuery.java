package fr.harmonia.tsclientquery.query;

/*
 * help channelclientdelperm Usage: channelclientdelperm cid={channelID}
 * cldbid={clientDBID} permid={permID}|permsid={permName}...
 * 
 * Removes a set of specified permissions from a client in a specific channel.
 * Multiple permissions can be removed at once. A permission can be specified by
 * permid or permsid.
 * 
 * Example: channelclientdelperm cid=12 cldbid=3
 * permsid=i_icon_id|permsid=b_icon_manage error id=0 msg=ok
 * 
 * 
 */
public class ChannelClientDelPermQuery extends DelPermQuery<ChannelClientDelPermQuery> {

	public ChannelClientDelPermQuery(int cid, int cldbid, String permsid) {
		super("channelclientdelperm", permsid);
		addArgument("cid", cid);
		addArgument("cldbid", cldbid);
	}

}
