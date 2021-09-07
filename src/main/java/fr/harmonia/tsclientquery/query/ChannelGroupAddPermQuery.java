package fr.harmonia.tsclientquery.query;

/*
help channelgroupaddperm
Usage: channelgroupaddperm cgid={groupID} ( permid={permID}|permsid={permName}
       permvalue={permValue} )...

Adds a set of specified permissions to a channel group. Multiple permissions
can be added by providing the two parameters of each permission. A permission
can be specified by permid or permsid.

Example:
   channelgroupaddperm cgid=78 permsid=b_icon_manage permvalue=1
   error id=0 msg=ok


error id=0 msg=ok
 */

public class ChannelGroupAddPermQuery extends AddPermQuery<ChannelGroupAddPermQuery> {

	public ChannelGroupAddPermQuery(int channelGroupID, int permid, int permvalue) {
		super("channelgroupaddperm", permid, permvalue);
		addArgument("cgid", channelGroupID);
	}

	public ChannelGroupAddPermQuery(int channelGroupID, String permsid, int permvalue) {
		super("channelgroupaddperm", permsid, permvalue);
		addArgument("cgid", channelGroupID);
	}

}
