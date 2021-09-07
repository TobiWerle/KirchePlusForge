package fr.harmonia.tsclientquery.query;

import fr.harmonia.tsclientquery.event.EnumEvent;

/*
 * help clientnotifyregister Usage: clientnotifyregister
 * schandlerid=<serverConnectionHandlerID> event=<event>
 * 
 * This command allows you to listen to events that the client encounters.
 * Events are things like people starting or stopping to talk, people joining or
 * leaving, new channels being created and many more. It registers for client
 * notifications for the specified serverConnectionHandlerID. If the
 * serverConnectionHandlerID is set to zero it applies to all server connection
 * handlers. Possible event values are listed below, additionally the special
 * string "any" can be used to subscribe to all events.
 * 
 * Possible values for event: notifytalkstatuschange notifymessage
 * notifymessagelist notifycomplainlist notifybanlist notifyclientmoved
 * notifyclientleftview notifycliententerview notifyclientpoke
 * notifyclientchatclosed notifyclientchatcomposing notifyclientupdated
 * notifyclientids notifyclientdbidfromuid notifyclientnamefromuid
 * notifyclientnamefromdbid notifyclientuidfromclid notifyconnectioninfo
 * notifychannelcreated notifychanneledited notifychanneldeleted
 * notifychannelmoved notifyserveredited notifyserverupdated channellist
 * channellistfinished notifytextmessage notifycurrentserverconnectionchanged
 * notifyconnectstatuschange
 * 
 * Example: clientnotifyregister schandlerid=0 event=any error id=0 msg=ok
 * 
 * 
 * error id=0 msg=ok
 * 
 */
public class QueryClientNotifyRegister extends NoAnswerQuery {

	public QueryClientNotifyRegister(int schandlerid, EnumEvent event) {
		super("clientnotifyregister");
		addArgument("schandlerid", schandlerid);
		addArgument("event", event);
	}

}
