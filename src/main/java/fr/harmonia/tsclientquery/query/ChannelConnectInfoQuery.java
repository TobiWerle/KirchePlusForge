package fr.harmonia.tsclientquery.query;

import fr.harmonia.tsclientquery.answer.ChannelConnectInfoAnswer;
import fr.harmonia.tsclientquery.objects.ParsedObject;

/*
 * Usage: channelconnectinfo [cid={channelid}]
 * 
 * Get channel connection information for specified channelid from the currently
 * selected server connection handler. If no channelid is provided, information
 * for the current channel will be received.
 * 
 * Example: channelconnectinfo path=test\/subtest password=secret error id=0
 * msg=ok
 * 
 * 
 * error id=0 msg=ok
 * 
 */
public class ChannelConnectInfoQuery extends Query<ChannelConnectInfoAnswer> {

	public ChannelConnectInfoQuery() {
		super("channelconnectinfo");
	}

	public ChannelConnectInfoQuery(int cid) {
		this();
		addArgument("cid", cid);
	}

	@Override
	public void buildAnswer(ParsedObject obj) {
		answer = new ChannelConnectInfoAnswer(obj);
	}

}
