package fr.harmonia.tsclientquery.query;

import fr.harmonia.tsclientquery.answer.ChannelClientPermListAnswer;
import fr.harmonia.tsclientquery.event.EnumEvent;
import fr.harmonia.tsclientquery.objects.ParsedObject;

/*
 * help channelclientpermlist Usage: channelclientpermlist cid={channelID}
 * cldbid={clientDBID} [-permsid]
 * 
 * Displays a list of permissions defined for a client in a specific channel.
 * 
 * Example: channelclientpermlist cid=12 cldbid=3 cid=12 cldbid=3 permid=4353
 * permvalue=1 permnegated=0 permskip=0|permid=17276 permvalue=50 permnegated=0
 * permskip=0|permid=21415 ... error id=0 msg=ok
 * 
 * error id=0 msg=ok
 * 
 */
public class ChannelClientPermListQuery extends EventAnswerQuery<ChannelClientPermListAnswer> {

	public ChannelClientPermListQuery(int cid, int cldbid) {
		super("channelclientpermlist", EnumEvent.notifychannelclientpermlist);
		addArgument("cid", cid);
		addArgument("cldbid", cldbid);
		answer = new ChannelClientPermListAnswer(cid, cldbid);
	}

	@Override
	public void buildAnswer(ParsedObject obj) {
		answer.addLine(obj);
	}

}
