package fr.harmonia.tsclientquery.query;

import fr.harmonia.tsclientquery.objects.EnumChannelGroupType;

/*
 * Usage: channelgroupadd name={groupName} [type={groupDbType}]
 * 
 * Creates a new channel group using a given name and displays its ID. The
 * optional type parameter can be used to create ServerQuery groups and template
 * groups.
 * 
 * Example: channelgroupadd name=Channel\sAdmin cgid=13 error id=0 msg=ok
 * 
 * error id=0 msg=ok
 */
public class ChannelGroupAddQuery extends NoAnswerQuery {

	public ChannelGroupAddQuery(String groupName) {
		super("channelgroupadd");
		addArgument("name", groupName);
	}

	public ChannelGroupAddQuery(String groupName, EnumChannelGroupType type) {
		this(groupName);
		addArgument("type", type.getId());
	}

}
