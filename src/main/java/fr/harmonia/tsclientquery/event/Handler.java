package fr.harmonia.tsclientquery.event;

import fr.harmonia.tsclientquery.TSClientQuery;
import fr.harmonia.tsclientquery.objects.Channel;
import fr.harmonia.tsclientquery.objects.Client;

/**
 * an handler to handle registered event
 * 
 * @author ATE47
 *
 * @see    TSClientQuery#registerHandler(Handler)
 * @see    TSClientQuery#clientNotifyRegister(EnumEvent)
 * @see    TSClientQuery#clientNotifyRegister(int, EnumEvent)
 * @see    TSClientQuery#clientNotifyRegisterAll()
 * @see    TSClientQuery#clientNotifyRegisterAll(int)
 */
public interface Handler {
	/**
	 * targetmode CHANNEL for the
	 * {@link Handler#onMessage(int, int, String, int, String, String)} event
	 */
	public static final int MESSAGE_TARGET_MODE_CHANNEL = 2;
	/**
	 * targetmode SERVER for the
	 * {@link Handler#onMessage(int, int, String, int, String, String)} event
	 */
	public static final int MESSAGE_TARGET_MODE_SERVER = 3;

	/**
	 * call when the client change the current server connection, require
	 * {@link EnumEvent#notifycurrentserverconnectionchanged}
	 * 
	 * @param schandlerid the server connection ID
	 */
	default void onChangeCurrentServerConnection(int schandlerid) {
	}

	/**
	 * call when a client change the channel parent, require
	 * {@link EnumEvent#notifychannelmoved}
	 * 
	 * @param schandlerid the server connection ID
	 * @param invokerid   client ID of who edited the channel
	 * @param invokerName client name of who edited the channel
	 * @param invokerUID  client UID of who edited the channel
	 * @param channel     a channel object with only new information (see
	 *                    {@link Channel#update(fr.harmonia.tsclientquery.objects.ParsedObject, boolean)})
	 */
	default void onChannelChangeParentId(int schandlerid, int invokerid, String invokerName, String invokerUID,
			Channel channel) {
	}

	/**
	 * call when a channel is create, require {@link EnumEvent#notifychannelcreated}
	 * 
	 * @param schandlerid the server connection handler ID
	 * @param invokerID   client ID of who create the channel
	 * @param invokerName client name of who create the channel
	 * @param invokerUID  client UID of who create the channel
	 * @param channel     the channel object
	 */
	default void onChannelCreate(int schandlerid, int invokerID, String invokerName, String invokerUID,
			Channel channel) {
	}

	/**
	 * call when a channel is delete, require {@link EnumEvent#notifychanneldeleted}
	 * 
	 * @param schandlerid the server connection handler ID
	 * @param channelID   the channel ID
	 * @param invokerID   client ID of who deleted the channel (can be 0 if
	 *                    invokerName=Server)
	 * @param invokerName client name of who deleted the channel
	 * @param invokerUID  client UID of who deleted the channel (can be empty if
	 *                    invokerName=Server)
	 */
	default void onChannelDeleted(int schandlerid, int channelID, int invokerID, String invokerName,
			String invokerUID) {
	}

	/**
	 * call when a channel is edited, require {@link EnumEvent#notifychanneledited}
	 * 
	 * @param schandlerid the server connection handler ID
	 * @param invokerID   client ID of who edited the channel (can be 0 after
	 *                    channel creation)
	 * @param invokerName client name of who edited the channel (can be empty after
	 *                    channel creation)
	 * @param invokerUID  client UID of who edited the channel (can be 0 after
	 *                    channel creation)
	 * @param channel     a channel object with only new information (see
	 *                    {@link Channel#update(fr.harmonia.tsclientquery.objects.ParsedObject, boolean)})
	 */
	default void onChannelEdited(int schandlerid, int invokerID, String invokerName, String invokerUID,
			Channel channel) {
	}

	/**
	 * call when a client connect to the server, require
	 * {@link EnumEvent#notifycliententerview}
	 * 
	 * @param schandlerid the server connection ID
	 * @param channelToID the new channel
	 * @param client      the connected client
	 * @see               Handler#onClientMove(int, int, int)
	 * @see               Handler#onClientDisconnect(int, int, int)
	 * @see               Handler#onClientKickFromChannel(int, int, int, String,
	 *                    String, String, int)
	 * @see               Handler#onClientKickFromServer(int, int, int, String,
	 *                    String, String, int)
	 * @see               Handler#onClientLeftView(int, int, int)
	 * @see               Handler#onClientMovedByOther(int, int, int, String,
	 *                    String, int)
	 * @see               Handler#onClientEnterView(int, int, int, Client)
	 * @see               Handler#onClientEnterViewMoved(int, int, int, int, String,
	 *                    String, Client)
	 * @see               Handler#onClientKickFromChannelOutOfView(int, int, int,
	 *                    int, String, String, String, Client)
	 * @see               Handler#onClientConnect(int, int, Client)
	 */
	default void onClientConnect(int schandlerid, int channelToID, Client client) {
	}

	/**
	 * call when a client disconnect from the server, require
	 * {@link EnumEvent#notifyclientleftview}
	 * 
	 * @param schandlerid   the server connection ID
	 * @param channelFromID channel where the client were
	 * @param clientID      the client ID
	 * @see                 Handler#onClientMove(int, int, int)
	 * @see                 Handler#onClientMovedByOther(int, int, int, String,
	 *                      String, int)
	 * @see                 Handler#onClientKickFromChannel(int, int, int, String,
	 *                      String, String, int)
	 * @see                 Handler#onClientKickFromServer(int, int, int, String,
	 *                      String, String, int)
	 * @see                 Handler#onClientLeftView(int, int, int)
	 * @see                 Handler#onClientEnterView(int, int, int, Client)
	 * @see                 Handler#onClientEnterViewMoved(int, int, int, int,
	 *                      String, String, Client)
	 * @see                 Handler#onClientKickFromChannelOutOfView(int, int, int,
	 *                      int, String, String, String, Client)
	 * @see                 Handler#onClientConnect(int, int, Client)
	 */
	default void onClientDisconnect(int schandlerid, int channelFromID, int clientID) {
	}

	/**
	 * call when a client move to another channel and enter view, require
	 * {@link EnumEvent#notifycliententerview}
	 * 
	 * @param schandlerid   the server connection ID
	 * @param channelFromID the old channel
	 * @param channelToID   the new channel
	 * @param client        the client
	 * @see                 Handler#onClientMove(int, int, int)
	 * @see                 Handler#onClientDisconnect(int, int, int)
	 * @see                 Handler#onClientKickFromChannel(int, int, int, String,
	 *                      String, String, int)
	 * @see                 Handler#onClientKickFromServer(int, int, int, String,
	 *                      String, String, int)
	 * @see                 Handler#onClientLeftView(int, int, int)
	 * @see                 Handler#onClientMovedByOther(int, int, int, String,
	 *                      String, int)
	 * @see                 Handler#onClientEnterView(int, int, int, Client)
	 * @see                 Handler#onClientEnterViewMoved(int, int, int, int,
	 *                      String, String, Client)
	 * @see                 Handler#onClientKickFromChannelOutOfView(int, int, int,
	 *                      int, String, String, String, Client)
	 * @see                 Handler#onClientConnect(int, int, Client)
	 */
	default void onClientEnterView(int schandlerid, int channelFromID, int channelToID, Client client) {
	}

	/**
	 * call when a client move another client to another channel in our view,
	 * require {@link EnumEvent#notifycliententerview}
	 * 
	 * @param schandlerid   the server connection ID
	 * @param channelFromID the old channel
	 * @param channelToID   the new channel
	 * @param invokerid     client ID of who move the client (can be 0 if
	 *                      invokerName=Server)
	 * @param invokername   client name of who move the client
	 * @param invokeruid    client UID of who move the client (can be empty if
	 *                      invokerName=Server)
	 * @param client        the client
	 * @see                 Handler#onClientMove(int, int, int)
	 * @see                 Handler#onClientDisconnect(int, int, int)
	 * @see                 Handler#onClientKickFromChannel(int, int, int, String,
	 *                      String, String, int)
	 * @see                 Handler#onClientKickFromServer(int, int, int, String,
	 *                      String, String, int)
	 * @see                 Handler#onClientLeftView(int, int, int)
	 * @see                 Handler#onClientMovedByOther(int, int, int, String,
	 *                      String, int)
	 * @see                 Handler#onClientEnterView(int, int, int, Client)
	 * @see                 Handler#onClientEnterViewMoved(int, int, int, int,
	 *                      String, String, Client)
	 * @see                 Handler#onClientKickFromChannelOutOfView(int, int, int,
	 *                      int, String, String, String, Client)
	 * @see                 Handler#onClientConnect(int, int, Client)
	 */
	default void onClientEnterViewMoved(int schandlerid, int channelFromID, int channelToID, int invokerid,
			String invokername, String invokeruid, Client client) {
	}

	/**
	 * call when a client kick another client, require
	 * {@link EnumEvent#notifyclientmoved}
	 * 
	 * @param schandlerid     the server connection ID
	 * @param channelTargetID the new channel
	 * @param invokerClientID client ID of who kick the client (can be 0 if
	 *                        invokerName=Server)
	 * @param invokerName     client name of who kick the client
	 * @param invokerUID      client UID of who kick the client (can be empty if
	 *                        invokerName=Server)
	 * @param reasonmsg       the reason of the kick
	 * @param clientID        ID of the kicked client
	 * @see                   Handler#onClientMove(int, int, int)
	 * @see                   Handler#onClientMovedByOther(int, int, int, String,
	 *                        String, int)
	 * @see                   Handler#onClientDisconnect(int, int, int)
	 * @see                   Handler#onClientKickFromServer(int, int, int, String,
	 *                        String, String, int)
	 * @see                   Handler#onClientLeftView(int, int, int)
	 * @see                   Handler#onClientEnterView(int, int, int, Client)
	 * @see                   Handler#onClientEnterViewMoved(int, int, int, int,
	 *                        String, String, Client)
	 * @see                   Handler#onClientKickFromChannelOutOfView(int, int,
	 *                        int, int, String, String, String, Client)
	 * @see                   Handler#onClientConnect(int, int, Client)
	 */
	default void onClientKickFromChannel(int schandlerid, int channelTargetID, int invokerClientID, String invokerName,
			String invokerUID, String reasonmsg, int clientID) {
	}

	/**
	 * call when a client kick another client out of our view, require
	 * {@link EnumEvent#notifycliententerview}
	 * 
	 * @param schandlerid   the server connection ID
	 * @param channelFromID the old channel
	 * @param channelToID   the new channel
	 * @param invokerid     client ID of who kick the client (can be 0 if
	 *                      invokerName=Server)
	 * @param invokername   client name of who kick the client
	 * @param invokeruid    client UID of who kick the client (can be empty if
	 *                      invokerName=Server)
	 * @param reasonmsg     the kick reason
	 * @param client        the kicked client
	 * @see                 Handler#onClientMove(int, int, int)
	 * @see                 Handler#onClientDisconnect(int, int, int)
	 * @see                 Handler#onClientKickFromChannel(int, int, int, String,
	 *                      String, String, int)
	 * @see                 Handler#onClientKickFromServer(int, int, int, String,
	 *                      String, String, int)
	 * @see                 Handler#onClientLeftView(int, int, int)
	 * @see                 Handler#onClientMovedByOther(int, int, int, String,
	 *                      String, int)
	 * @see                 Handler#onClientEnterView(int, int, int, Client)
	 * @see                 Handler#onClientEnterViewMoved(int, int, int, int,
	 *                      String, String, Client)
	 * @see                 Handler#onClientKickFromChannelOutOfView(int, int, int,
	 *                      int, String, String, String, Client)
	 * @see                 Handler#onClientConnect(int, int, Client)
	 */
	default void onClientKickFromChannelOutOfView(int schandlerid, int channelFromID, int channelToID, int invokerid,
			String invokername, String invokeruid, String reasonmsg, Client client) {
	}

	/**
	 * call when a client is kick from the server, require
	 * {@link EnumEvent#notifyclientleftview}
	 * 
	 * @param schandlerid     the server connection ID
	 * @param channelFromID   channel where the client were
	 * @param invokerClientID client ID of who kick the client (can be 0 if
	 *                        invokerName=Server)
	 * @param invokerName     client name of who kick the client
	 * @param invokerUID      client UID of who kick the client (can be empty if
	 *                        invokerName=Server)
	 * @param reasonmsg       the reason of the kick
	 * @param clientID        ID of the kicked client
	 * @see                   Handler#onClientMove(int, int, int)
	 * @see                   Handler#onClientMovedByOther(int, int, int, String,
	 *                        String, int)
	 * @see                   Handler#onClientDisconnect(int, int, int)
	 * @see                   Handler#onClientKickFromChannel(int, int, int, String,
	 *                        String, String, int)
	 * @see                   Handler#onClientLeftView(int, int, int)
	 * @see                   Handler#onClientEnterView(int, int, int, Client)
	 * @see                   Handler#onClientEnterViewMoved(int, int, int, int,
	 *                        String, String, Client)
	 * @see                   Handler#onClientKickFromChannelOutOfView(int, int,
	 *                        int, int, String, String, String, Client)
	 * @see                   Handler#onClientConnect(int, int, Client)
	 */
	default void onClientKickFromServer(int schandlerid, int channelFromID, int invokerClientID, String invokerName,
			String invokerUID, String reasonmsg, int clientID) {
	}

	/**
	 * call when a client left the view of our client, require
	 * {@link EnumEvent#notifyclientleftview}
	 * 
	 * @param schandlerid     the server connection ID
	 * @param channelTargetID the new channel ID
	 * @param clientID        the client ID
	 * @see                   Handler#onClientMove(int, int, int)
	 * @see                   Handler#onClientMovedByOther(int, int, int, String,
	 *                        String, int)
	 * @see                   Handler#onClientDisconnect(int, int, int)
	 * @see                   Handler#onClientKickFromChannel(int, int, int, String,
	 *                        String, String, int)
	 * @see                   Handler#onClientKickFromServer(int, int, int, String,
	 *                        String, String, int)
	 * @see                   Handler#onClientEnterView(int, int, int, Client)
	 * @see                   Handler#onClientEnterViewMoved(int, int, int, int,
	 *                        String, String, Client)
	 * @see                   Handler#onClientKickFromChannelOutOfView(int, int,
	 *                        int, int, String, String, String, Client)
	 * @see                   Handler#onClientConnect(int, int, Client)
	 */
	default void onClientLeftView(int schandlerid, int channelTargetID, int clientID) {
	}

	/**
	 * call when a client move to another channel, require
	 * {@link EnumEvent#notifyclientmoved}
	 * 
	 * @param schandlerid     the server connection ID
	 * @param channelTargetID the new channel
	 * @param clientID        the client ID
	 * @see                   Handler#onClientMovedByOther(int, int, int, String,
	 *                        String, int)
	 * @see                   Handler#onClientDisconnect(int, int, int)
	 * @see                   Handler#onClientKickFromChannel(int, int, int, String,
	 *                        String, String, int)
	 * @see                   Handler#onClientKickFromServer(int, int, int, String,
	 *                        String, String, int)
	 * @see                   Handler#onClientLeftView(int, int, int)
	 * @see                   Handler#onClientEnterView(int, int, int, Client)
	 * @see                   Handler#onClientEnterViewMoved(int, int, int, int,
	 *                        String, String, Client)
	 * @see                   Handler#onClientKickFromChannelOutOfView(int, int,
	 *                        int, int, String, String, String, Client)
	 * @see                   Handler#onClientConnect(int, int, Client)
	 */
	default void onClientMove(int schandlerid, int channelTargetID, int clientID) {
	}

	/**
	 * call when a client is moved by another client, require
	 * {@link EnumEvent#notifyclientmoved}
	 * 
	 * @param schandlerid     the server connection ID
	 * @param channelTargetID the new channel
	 * @param invokerClientID client ID of who move the client (can be 0 if
	 *                        invokerName=Server)
	 * @param invokerName     client name of who move the client
	 * @param invokerUID      client UID of who move the client (can be empty if
	 *                        invokerName=Server)
	 * @param clientID        ID of the moved client
	 * @see                   Handler#onClientMove(int, int, int)
	 * @see                   Handler#onClientDisconnect(int, int, int)
	 * @see                   Handler#onClientKickFromChannel(int, int, int, String,
	 *                        String, String, int)
	 * @see                   Handler#onClientKickFromServer(int, int, int, String,
	 *                        String, String, int)
	 * @see                   Handler#onClientLeftView(int, int, int)
	 * @see                   Handler#onClientEnterView(int, int, int, Client)
	 * @see                   Handler#onClientEnterViewMoved(int, int, int, int,
	 *                        String, String, Client)
	 * @see                   Handler#onClientKickFromChannelOutOfView(int, int,
	 *                        int, int, String, String, String, Client)
	 * @see                   Handler#onClientConnect(int, int, Client)
	 * 
	 */
	default void onClientMovedByOther(int schandlerid, int channelTargetID, int invokerClientID, String invokerName,
			String invokerUID, int clientID) {
	}

	/**
	 * call when a client data is update, require
	 * {@link EnumEvent#notifyclientupdated}
	 * 
	 * @param schandlerid the server connection ID
	 * @param client      a client object with only new information (see
	 *                    {@link Client#update(fr.harmonia.tsclientquery.objects.ParsedObject, boolean)})
	 */
	default void onClientUpdated(int schandlerid, Client client) {
	}

	/**
	 * call when the client has connect to a server
	 * 
	 * @param schandlerid the server connection handler id
	 */
	default void onConnected(int schandlerid) {
	}

	/**
	 * call when the client want to connect to a server
	 * 
	 * @param schandlerid the server connection handler id
	 */
	default void onConnecting(int schandlerid) {
	}

	/**
	 * call when the client know every channels and ask groups, permissions and
	 * server data
	 * 
	 * @param schandlerid the server connection handler id
	 */
	default void onConnectionEstablished(int schandlerid) {
	}

	/**
	 * call when the client start asking every channels
	 * 
	 * @param schandlerid the server connection handler id
	 */
	default void onConnectionEstablishing(int schandlerid) {
	}

	/**
	 * call when the client disconnect from a server
	 * 
	 * @param schandlerid the server connection handler id
	 * @param error       disconnect error code
	 */
	default void onDisconnected(int schandlerid, int error) {
	}

	/**
	 * call when the client receive a message, require
	 * {@link EnumEvent#notifytextmessage}
	 * 
	 * @param schandlerid the server connection ID
	 * @param targetmode  the target, {@link Handler#MESSAGE_TARGET_MODE_CHANNEL} or
	 *                    {@link Handler#MESSAGE_TARGET_MODE_SERVER}
	 * @param msg         the sent message
	 * @param invokerid   CLID of who sent the message
	 * @param invokername name of who sent the message
	 * @param invokeruid  UID of who sent the message
	 * @see               Handler#onPrivateMessage(int, String, int, int, String,
	 *                    String)
	 */
	default void onMessage(int schandlerid, int targetmode, String msg, int invokerid, String invokername,
			String invokeruid) {
	}

	/**
	 * call when the client receive a poke, require
	 * {@link EnumEvent#notifyclientpoke}
	 * 
	 * @param schandlerid the server connection ID
	 * @param invokerid   CLID of who sent the message
	 * @param msg         the sent message
	 * @param invokername name of who sent the message
	 * @param invokeruid  name of who sent the message
	 */
	default void onPoke(int schandlerid, int invokerid, String msg, String invokername, String invokeruid) {
	}

	/**
	 * call when the client receive a private message, require
	 * {@link EnumEvent#notifytextmessage}
	 * 
	 * @param schandlerid the server connection ID
	 * @param msg         the sent message
	 * @param target      target CLID of this message
	 * @param invokerid   CLID of who sent the message
	 * @param invokername name of who sent the message
	 * @param invokeruid  UID of who sent the message
	 * @see               Handler#onPrivateMessage(int, String, int, int, String,
	 *                    String)
	 */
	default void onPrivateMessage(int schandlerid, String msg, int target, int invokerid, String invokername,
			String invokeruid) {
	}
}
