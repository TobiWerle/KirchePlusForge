package fr.harmonia.tsclientquery;

import fr.harmonia.tsclientquery.answer.*;
import fr.harmonia.tsclientquery.event.EnumEvent;
import fr.harmonia.tsclientquery.event.Handler;
import fr.harmonia.tsclientquery.event.executor.AsynchronousEventExecutor;
import fr.harmonia.tsclientquery.event.executor.RunnablesExecutor;
import fr.harmonia.tsclientquery.event.executor.SynchronousEventExecutor;
import fr.harmonia.tsclientquery.exception.*;
import fr.harmonia.tsclientquery.objects.*;
import fr.harmonia.tsclientquery.objects.builder.BanBuilder;
import fr.harmonia.tsclientquery.objects.builder.ClientBanBuilder;
import fr.harmonia.tsclientquery.objects.builder.ServerConnectionBuilder;
import fr.harmonia.tsclientquery.query.*;
import fr.harmonia.tsclientquery.query.ClientKickQuery.Reason;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static fr.harmonia.tsclientquery.exception.QueryException.*;

/**
 * a client to link with a Teamspeak client with <a
 * href=https://forum.teamspeak.com/threads/66509-Official-ClientQuery-Plugin>ClientQuery</a>
 * 
 * @author ATE47
 */
public class TSClientQuery {
	/**
	 * the max length of a kick message
	 */
	public static final int MAX_KICK_MESSAGE_LENGTH = 40;
	/**
	 * the max length of a message
	 */
	public static final int MAX_MESSAGE_LENGTH = 8192;
	/**
	 * the max length of a poke
	 */
	public static final int MAX_POKE_LENGTH = 100;

	/**
	 * decode with base64
	 * 
	 * @param  s what to decode
	 * @return   the decoded string
	 */
	public static String decodeBase64(String s) {
		return new String(Base64.getDecoder().decode(s));
	}

	/**
	 * decode a string query
	 * 
	 * @param  str string to decode
	 * @return     the decoded string
	 */
	public static String decodeQueryStringParameter(String str) {
		return str.replace("\\a", String.valueOf((char) 7)).replace("\\v", String.valueOf((char) 11))
				.replace("\\r", "\r").replace("\\p", "|").replace("\\f", "\f").replace("\\b", "\b").replace("\\n", "\n")
				.replace("\\r", "\r").replace("\\s", " ").replace("\\r", "\r").replace("\\/", "/")
				.replace("\\\\", "\\");
	}

	/**
	 * encode with base64
	 * 
	 * @param  s what to encode
	 * @return   the encoded string
	 */
	public static String encodeBase64(String s) {
		return new String(Base64.getEncoder().encode(s.getBytes()));
	}

	/**
	 * encode a string query
	 * 
	 * @param  str string to encode
	 * @return     the encoded string
	 */
	public static String encodeQueryStringParameter(String str) {
		return str.replace("\\", "\\\\").replace("\n", "\\n").replace("\r", "\\r").replace(" ", "\\s")
				.replace("/", "\\/").replace("|", "\\p").replace("\b", "\\b").replace("\f", "\\f").replace("\t", "\\t")
				.replace(String.valueOf((char) 7), "\\a").replace(String.valueOf((char) 11), "\\v");
	}

	/**
	 * hash a string baseEncode64(sha1(String))
	 * 
	 * @param  s the string to hash
	 * @return   the hashed string
	 */
	public static String hashPassword(String s) {
		try {
			MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
			byte[] bytes = s.getBytes();
			int len = bytes.length;

			sha1.update(bytes, 0, len);

			return new String(Base64.getEncoder().encode(sha1.digest()));
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}

	private final InetAddress address;
	private final String apikey;
	private final int port;

	private QueryReader reader;
	private Socket socket;
	private boolean started = false;
	private QueryWritter writter;
	AtomicReference<Query<?>> currentQuery = new AtomicReference<Query<?>>();
	RunnablesExecutor executor = new AsynchronousEventExecutor();

	long floodRate = 250L;
	final List<Handler> HANDLERS = new ArrayList<>();
	final BlockingQueue<Query<?>> queue = new LinkedBlockingQueue<Query<?>>();
	AtomicInteger selectedSchandlerid = new AtomicInteger();
	AtomicInteger usedSchandlerid = new AtomicInteger();

	/**
	 * create a clientquery to default localhost:25639
	 * 
	 * @param apikey client APIKEY
	 * @see          TSClientQuery#TSClientQuery(String, InetAddress, int)
	 */
	public TSClientQuery(String apikey) {
		this(apikey, InetAddress.getLoopbackAddress(), 25639);
	}

	/**
	 * create a clientquery to a non-default address
	 * 
	 * @param apikey  client APIKEY
	 * @param address clientquery server address
	 * @param port    clientquery server port
	 * @see           TSClientQuery#TSClientQuery(String)
	 */
	public TSClientQuery(String apikey, InetAddress address, int port) {
		this.apikey = apikey;
		this.address = address;
		this.port = port;
	}

	/**
	 * send a {@link BanAddQuery} and get the ban ID
	 * 
	 * @param  ban                                         the ban to send
	 * @return                                             the new ban id
	 * @throws InsufficientClientPermissionsQueryException if the client haven't the
	 *                                                     permission to do this
	 *                                                     command
	 * @see                                                BanBuilder
	 */
	public int banAdd(Ban ban) throws InsufficientClientPermissionsQueryException {
		return sendQuery(new BanAddQuery(ban)).getBanId();
	}

	/**
	 * send a {@link BanClientQuery} and get the ban IDs
	 * 
	 * @param  ban            the ban to send
	 * @return                the ban IDs of the new bans
	 * @throws QueryException if an error occur
	 * @see                   ClientBanBuilder
	 */
	public int[] banClient(ClientBan ban) throws InsufficientClientPermissionsQueryException {
		MultipleBanAnswer asw = sendQuery(new BanClientQuery(ban));
		return asw.getBanList().stream().mapToInt(BanAnswer::getBanId).toArray();
	}

	/**
	 * get the list of ban in the database, the {@link EnumEvent#notifybanlist} must
	 * be register
	 * 
	 * @return                                             the list of ban
	 * @throws InsufficientClientPermissionsQueryException if the client haven't the
	 *                                                     permission to do this
	 *                                                     command
	 * @throws UnRegisterQueryException                    if the
	 *                                                     {@link EnumEvent#notifybanlist}
	 *                                                     event isn't register
	 */
	public List<DataBaseBan> banList() throws InsufficientClientPermissionsQueryException, UnRegisterQueryException {
		return sendQuery(new BanListQuery()).getBanList();
	}

	/**
	 * send a {@link ChannelAddPermQuery} to a channel (usable to add multiple
	 * permission in one query)
	 * 
	 * @param  query                                       the query to send
	 * @return                                             if the permission(s)
	 *                                                     is(were) added
	 * @throws InsufficientClientPermissionsQueryException if the client haven't the
	 *                                                     permission to do this
	 *                                                     command
	 */
	public boolean channelAddPerm(ChannelAddPermQuery query) throws InsufficientClientPermissionsQueryException {
		return sendQuery(query).getId() == QueryException.ERROR_ID_OK;
	}

	/**
	 * add a permission to a channel, to add multiple permissions in one query use
	 * {@link TSClientQuery#channelAddPerm(ChannelAddPermQuery)}
	 * 
	 * @param  channelId                                   the channel id
	 * @param  permid                                      the perm id
	 * @param  permvalue                                   the perm value
	 * @return                                             if the permission is
	 *                                                     added
	 * @throws InsufficientClientPermissionsQueryException if the client haven't the
	 *                                                     permission to do this
	 *                                                     command
	 */
	public boolean channelAddPerm(int channelId, int permid, int permvalue)
			throws InsufficientClientPermissionsQueryException {
		return channelAddPerm(new ChannelAddPermQuery(channelId, permid, permvalue));
	}

	/**
	 * add a permission to a channel, to add multiple permissions in one query use
	 * {@link TSClientQuery#channelAddPerm(ChannelAddPermQuery)}
	 * 
	 * @param  channelId                                   the channel id
	 * @param  permName                                    the perm name
	 * @param  permvalue                                   the perm value
	 * @return                                             if the permission is
	 *                                                     added
	 * @throws InsufficientClientPermissionsQueryException if the client haven't the
	 *                                                     permission to do this
	 *                                                     command
	 */
	public boolean channelAddPerm(int channelId, String permName, int permvalue)
			throws InsufficientClientPermissionsQueryException {
		return channelAddPerm(new ChannelAddPermQuery(channelId, permName, permvalue));
	}

	/**
	 * send a {@link ChannelClientAddPermQuery} to a channel (usable to add multiple
	 * permission in one query)
	 * 
	 * @param  query                                       the query to change
	 * @return                                             if the permission is
	 *                                                     added
	 * @throws InsufficientClientPermissionsQueryException if the client haven't the
	 *                                                     permission to do this
	 *                                                     command
	 */
	public boolean channelClientAddPerm(ChannelClientAddPermQuery query)
			throws InsufficientClientPermissionsQueryException {
		return sendQuery(query).getId() == QueryException.ERROR_ID_OK;
	}

	/**
	 * add a permission to a channel,to add multiple permissions in one query use
	 * {@link TSClientQuery#channelClientAddPerm(ChannelClientAddPermQuery)}
	 * 
	 * @param  channelId                                   the channel id
	 * @param  cldbid                                      the client database id
	 * @param  permid                                      the perm id
	 * @param  permvalue                                   the perm value
	 * @return                                             if the permission is
	 *                                                     added
	 * @throws InsufficientClientPermissionsQueryException if the client haven't the
	 *                                                     permission to do this
	 *                                                     command
	 */
	public boolean channelClientAddPerm(int channelId, int cldbid, int permid, int permvalue)
			throws InsufficientClientPermissionsQueryException {
		return channelClientAddPerm(new ChannelClientAddPermQuery(channelId, cldbid, permid, permvalue));
	}

	/**
	 * add a permission to a channel,to add multiple permissions in one query use
	 * {@link TSClientQuery#channelClientAddPerm(ChannelClientAddPermQuery)}
	 * 
	 * @param  channelId                                   the channel id
	 * @param  cldbid                                      the client database id
	 * @param  permName                                    the perm name
	 * @param  permvalue                                   the perm value
	 * @return                                             if the permission is
	 *                                                     added
	 * @throws InsufficientClientPermissionsQueryException if the client haven't the
	 *                                                     permission to do this
	 *                                                     command
	 */
	public boolean channelClientAddPerm(int channelId, int cldbid, String permName, int permvalue)
			throws InsufficientClientPermissionsQueryException {
		return channelClientAddPerm(new ChannelClientAddPermQuery(channelId, cldbid, permName, permvalue));
	}

	/**
	 * get the client list of a channel (or empty if unsubscribable)
	 * 
	 * @param  cid the channel ID
	 * @return     the list of {@link ChannelClient}
	 */
	public List<ChannelClient> channelClientList(int cid) {
		return sendQuery(new ChannelClientListQuery(cid)).getChannelClientList();
	}

	/**
	 * get the client list of a channel (or empty if unsubscribable)
	 * 
	 * @param  cid     the channel ID
	 * @param  uid     get client UID?
	 * @param  away    get client away?
	 * @param  voice   get client voice state?
	 * @param  groups  get client groups?
	 * @param  icon    get client icon?
	 * @param  country get client country?
	 * @return         the list of {@link ChannelClient}
	 */
	public List<ChannelClient> channelClientList(int cid, boolean uid, boolean away, boolean voice, boolean groups,
			boolean icon, boolean country) {
		return sendQuery(new ChannelClientListQuery(cid, uid, away, voice, groups, icon, country))
				.getChannelClientList();
	}

	/**
	 * get the list of permission for a client for a channel in the database, the
	 * {@link EnumEvent#notifychannelclientpermlist} event must be registered
	 * 
	 * @param  cid                                         the channel id
	 * @param  cldbid                                      the client databaseid
	 * @return                                             the list of permission
	 * @throws InsufficientClientPermissionsQueryException if the client haven't the
	 *                                                     permission to do this
	 *                                                     command
	 * @throws UnRegisterQueryException                    if the
	 *                                                     {@link EnumEvent#notifychannelclientpermlist}
	 *                                                     event isn't registered
	 * 
	 */
	public List<Permission> channelClientPermList(int cid, int cldbid)
			throws InsufficientClientPermissionsQueryException, UnRegisterQueryException {
		return sendQuery(new ChannelClientPermListQuery(cid, cldbid)).getPermissions();
	}

	/**
	 * 
	 * @return                                             the current channel
	 *                                                     connection info
	 * 
	 * @throws InsufficientClientPermissionsQueryException if the client haven't the
	 *                                                     permission to do this
	 *                                                     command
	 */
	public ChannelConnectInfoAnswer channelConnectInfo() throws InsufficientClientPermissionsQueryException {
		return sendQuery(new ChannelConnectInfoQuery());
	}

	/**
	 * @param  cid                                         channel ID
	 * @return                                             the channel connection
	 *                                                     info
	 * @throws InsufficientClientPermissionsQueryException if the client haven't the
	 *                                                     permission to do this
	 *                                                     command
	 */
	public ChannelConnectInfoAnswer channelConnectInfo(int cid) throws InsufficientClientPermissionsQueryException {
		return sendQuery(new ChannelConnectInfoQuery(cid));
	}

	/**
	 * create a channel group
	 * 
	 * @param  name                                        the group name
	 * @return                                             if the group is added
	 * @throws InsufficientClientPermissionsQueryException if the client haven't the
	 *                                                     permission to do this
	 *                                                     command
	 */
	public boolean channelGroupAdd(String name) throws InsufficientClientPermissionsQueryException {
		return sendQuery(new ChannelGroupAddQuery(name)).getId() == QueryException.ERROR_ID_OK;
	}

	/**
	 * create a channel group
	 * 
	 * @param  name                                        the group name
	 * @param  type                                        the group type
	 * @return                                             if the group is added
	 * @throws InsufficientClientPermissionsQueryException if the client haven't the
	 *                                                     permission to do this
	 *                                                     command
	 */
	public boolean channelGroupAdd(String name, EnumChannelGroupType type)
			throws InsufficientClientPermissionsQueryException {
		return sendQuery(new ChannelGroupAddQuery(name, type)).getId() == QueryException.ERROR_ID_OK;
	}

	/**
	 * send a {@link ChannelGroupAddPermQuery} to a channel (usable to add multiple
	 * permission in one query)
	 * 
	 * @param  query                                       the query to send
	 * @return                                             if the permission(s)
	 *                                                     is(were) added
	 * @throws InsufficientClientPermissionsQueryException if the client haven't the
	 *                                                     permission to do this
	 *                                                     command
	 */
	public boolean channelGroupAddPerm(ChannelGroupAddPermQuery query)
			throws InsufficientClientPermissionsQueryException {
		return sendQuery(query).getId() == QueryException.ERROR_ID_OK;
	}

	/**
	 * add a permission to a channel, to add multiple permissions in one query use
	 * {@link TSClientQuery#channelGroupAddPerm(ChannelGroupAddPermQuery)}
	 * 
	 * @param  channelGroupId                              the channel id
	 * @param  permid                                      the perm id
	 * @param  permvalue                                   the perm value
	 * @return                                             if the permission is
	 *                                                     added
	 * @throws InsufficientClientPermissionsQueryException if the client haven't the
	 *                                                     permission to do this
	 *                                                     command
	 */
	public boolean channelGroupAddPerm(int channelGroupId, int permid, int permvalue)
			throws InsufficientClientPermissionsQueryException {
		return channelGroupAddPerm(new ChannelGroupAddPermQuery(channelGroupId, permid, permvalue));
	}

	/**
	 * add a permission to a channel, to add multiple permissions in one query use
	 * {@link TSClientQuery#channelGroupAddPerm(ChannelGroupAddPermQuery)}
	 * 
	 * @param  channelGroupId                              the channel id
	 * @param  permName                                    the perm name
	 * @param  permvalue                                   the perm value
	 * @return                                             if the permission is
	 *                                                     added
	 * @throws InsufficientClientPermissionsQueryException if the client haven't the
	 *                                                     permission to do this
	 *                                                     command
	 */
	public boolean channelGroupAddPerm(int channelGroupId, String permName, int permvalue)
			throws InsufficientClientPermissionsQueryException {
		return channelGroupAddPerm(new ChannelGroupAddPermQuery(channelGroupId, permName, permvalue));
	}

	/**
	 * request client data with his database id, require
	 * {@link EnumEvent#notifyclientnamefromdbid}
	 * 
	 * @param  dbid the client database id
	 * @return      client data
	 */
	public ClientFromAnswer clientInfoFromDatabaseID(int dbid) {
		return sendQuery(new ClientFromDatabaseIDQuery(dbid));
	}

	/**
	 * request client data with his unique id, require
	 * {@link EnumEvent#notifyclientnamefromuid}
	 * 
	 * @param  uid the client unique id
	 * @return     client data
	 */
	public ClientFromAnswer clientInfoFromDatabaseUID(String uid) {
		return sendQuery(new ClientFromUIDQuery(uid));
	}

	/**
	 * request client data with his id, require
	 * {@link EnumEvent#notifyclientuidfromclid}
	 * 
	 * @param  id the client id
	 * @return    client data
	 */
	public ClientFromAnswer clientInfoFromID(int id) {
		return sendQuery(new ClientFromIDQuery(id));
	}

	/**
	 * get the client list of a server (without unsubscribable channels)
	 * 
	 * @return the list of {@link ChannelClient}
	 */
	public List<ChannelClient> clientList() {
		return sendQuery(new ClientListQuery()).getChannelClientList();
	}

	/**
	 * get the client list of a server (without unsubscribable channels)
	 * 
	 * @param  uid     get client UID?
	 * @param  away    get client away?
	 * @param  voice   get client voice state?
	 * @param  groups  get client groups?
	 * @param  icon    get client icon?
	 * @param  country get client country?
	 * @return         the list of {@link ChannelClient}
	 */
	public List<ChannelClient> clientList(boolean uid, boolean away, boolean voice, boolean groups, boolean icon,
			boolean country) {
		return sendQuery(new ClientListQuery(uid, away, voice, groups, icon, country)).getChannelClientList();
	}

	/**
	 * move clients to a channel
	 * 
	 * @param  cid                                         the channel id
	 * @param  clids                                       the client IDs to move
	 * @throws InsufficientClientPermissionsQueryException if the client haven't the
	 *                                                     permission to do this
	 *                                                     command
	 * @see                                                TSClientQuery#clientMove(int,
	 *                                                     String, int...)
	 */
	public void clientMove(int cid, int... clids) throws InsufficientClientPermissionsQueryException {
		sendQuery(new ClientMoveQuery(cid, clids));
	}

	/**
	 * move clients to a channel
	 * 
	 * @param  cid                                         the channel id
	 * @param  password                                    the password of this
	 *                                                     channel
	 * @param  clids                                       the client IDs to move
	 * @throws InsufficientClientPermissionsQueryException if the client haven't the
	 *                                                     permission to do this
	 *                                                     command
	 * @see                                                TSClientQuery#clientMove(int,
	 *                                                     int...)
	 */
	public void clientMove(int cid, String password, int... clids) throws InsufficientClientPermissionsQueryException {
		sendQuery(new ClientMoveQuery(cid, password, clids));
	}

	/**
	 * register an event to a all schandlerid
	 * 
	 * @param event the event to register
	 */
	public void clientNotifyRegister(EnumEvent event) {
		clientNotifyRegister(0, event);
	}

	/**
	 * register an event to a certain schandlerid
	 * 
	 * @param schandlerid the schandlerid (0 for any)
	 * @param event       the event to register
	 */
	public void clientNotifyRegister(int schandlerid, EnumEvent event) {
		sendQuery(new QueryClientNotifyRegister(schandlerid, event));
	}

	/**
	 * register all events to a all schandlerid
	 */
	public void clientNotifyRegisterAll() {
		clientNotifyRegisterAll(0);
	}

	/**
	 * register all events to a certain schandlerid
	 * 
	 * @param schandlerid the schandlerid (0 for any)
	 */
	public void clientNotifyRegisterAll(int schandlerid) {
		clientNotifyRegister(schandlerid, EnumEvent.any);
	}

	/**
	 * unregister all events to a all schandlerid
	 */
	public void clientNotifyUnregisterAll() {
		clientNotifyUnregisterAll(0);
	}

	/**
	 * unregister an event to a all schandlerid
	 * 
	 * @param event the event to unregister
	 */
	public void clientNotifyUnregisterAll(EnumEvent event) {
		clientNotifyRegister(0, event);
	}

	/**
	 * unregister all events to a certain schandlerid
	 * 
	 * @param schandlerid the schandlerid (0 for any)
	 */
	public void clientNotifyUnregisterAll(int schandlerid) {
		clientNotifyUnregisterAll(schandlerid, EnumEvent.any);
	}

	/**
	 * unregister an event to a certain schandlerid
	 * 
	 * @param schandlerid the schandlerid (0 for any)
	 * @param event       the event to unregister
	 */
	public void clientNotifyUnregisterAll(int schandlerid, EnumEvent event) {
		sendQuery(new QueryClientNotifyUnregister(schandlerid, event));
	}

	/**
	 * try to connect to a server
	 * 
	 * @param  server                             the server to connect, see
	 *                                            {@link ServerConnectionBuilder} to
	 *                                            build
	 * @throws CurrentlyNotPossibleQueryException if the selected server connection
	 *                                            handler is already use for a
	 *                                            server
	 */
	public void connect(ServerConnection server) throws CurrentlyNotPossibleQueryException {
		sendQuery(new ConnectQuery(server));
	}

	/**
	 * create a channel
	 * 
	 * @param  name                                        the channel name
	 * @param  properties                                  the channel properties
	 * @throws InsufficientClientPermissionsQueryException if the client haven't the
	 *                                                     permission to do this
	 *                                                     command
	 */
	public void createChannel(String name, ChannelProperty... properties)
			throws InsufficientClientPermissionsQueryException {
		sendQuery(new ChannelCreateQuery(name, properties));
	}

	/**
	 * @return the current server connection handler id
	 */
	public int currentServerConnectionHandlerID() {
		sendQuery(new CurrentServerConnectionHandlerIdQuery());
		return selectedSchandlerid.get();
	}

	/**
	 * delete all bans from this server
	 * 
	 * @throws InsufficientClientPermissionsQueryException if the client haven't the
	 *                                                     permission to do this
	 *                                                     command
	 */
	public void deleteAllBans() throws InsufficientClientPermissionsQueryException {
		sendQuery(new BanDelAllQuery());
	}

	/**
	 * delete a ban
	 * 
	 * @param  banid                                       the ban ID
	 * @return                                             if the ban existed and
	 *                                                     has been deleted
	 * @throws InsufficientClientPermissionsQueryException if the client haven't the
	 *                                                     permission to do this
	 *                                                     command
	 */
	public boolean deleteBan(int banid) throws InsufficientClientPermissionsQueryException {
		return sendQuery(new BanDel(banid)).getId() == QueryException.ERROR_ID_OK;
	}

	/**
	 * delete a channel from the server
	 * 
	 * @param  cid                                         the channel id
	 * @param  force                                       delete even if there are
	 *                                                     clients within
	 * @throws InsufficientClientPermissionsQueryException if the client haven't the
	 *                                                     permission to do this
	 *                                                     command
	 */
	public void deleteChannel(int cid, boolean force) throws InsufficientClientPermissionsQueryException {
		sendQuery(new ChannelDeleteQuery(cid, force));
	}

	/**
	 * disconnect from the current server connection
	 */
	public void disconnect() {
		sendQuery(new DisconnectQuery());
	}

	/**
	 * disconnect from the current server connection
	 * 
	 * @param message the message to show
	 */
	public void disconnect(String message) {
		sendQuery(new DisconnectQuery(message));
	}

	/**
	 * edit a channel
	 * 
	 * @param  cid                                         the channel ID
	 * @param  properties                                  the channel properties
	 * @throws InsufficientClientPermissionsQueryException if the client haven't the
	 *                                                     permission to do this
	 *                                                     command
	 */
	public void editChannel(int cid, ChannelProperty... properties) throws InsufficientClientPermissionsQueryException {
		sendQuery(new ChannelEditQuery(cid, properties));
	}

	/**
	 * get the help file for ClientQuery
	 * 
	 * @return the lines of the file
	 */
	public List<String> help() {
		return sendQuery(new HelpQuery()).getLines();
	}

	/**
	 * get the help file for a ClientQuery command
	 * 
	 * @param  command                        the command to search
	 * @return                                the lines of the file
	 * @throws InvalidParameterQueryException is the command is unknown
	 */
	public List<String> help(String command) throws InvalidParameterQueryException {
		return sendQuery(new HelpQuery(command)).getLines();
	}

	/**
	 * @return if the client is started
	 */
	public boolean isStarted() {
		return started;
	}

	/**
	 * kick clients from the channel
	 * 
	 * @param  clids                                       cliend IDs of the client
	 *                                                     to kick
	 * @throws InsufficientClientPermissionsQueryException if the client haven't the
	 *                                                     permission to do this
	 *                                                     command
	 */
	public void kickFromChannel(int... clids) throws InsufficientClientPermissionsQueryException {
		sendQuery(new ClientKickQuery(Reason.CHANNEL, clids));
	}

	/**
	 * kick clients from the channel with a reason
	 * 
	 * @param  reason                                      the reason of this
	 * @param  clids                                       cliend IDs of the client
	 *                                                     to kick
	 * @throws InsufficientClientPermissionsQueryException if the client haven't the
	 *                                                     permission to do this
	 *                                                     command
	 * @throws MessageTooLongException                     if the size of the reason
	 *                                                     is too long
	 * @see                                                TSClientQuery#MAX_KICK_MESSAGE_LENGTH
	 */
	public void kickFromChannel(String reason, int... clids)
			throws InsufficientClientPermissionsQueryException, MessageTooLongException {
		if (reason.length() > MAX_KICK_MESSAGE_LENGTH)
			throw new MessageTooLongException();
		sendQuery(new ClientKickQuery(Reason.CHANNEL, reason, clids));
	}

	/**
	 * kick clients from the server
	 * 
	 * @param  clids                                       cliend IDs of the client
	 *                                                     to kick
	 * @throws InsufficientClientPermissionsQueryException if the client haven't the
	 *                                                     permission to do this
	 *                                                     command
	 */
	public void kickFromServer(int... clids) throws InsufficientClientPermissionsQueryException {
		sendQuery(new ClientKickQuery(Reason.SERVER, clids));
	}

	/**
	 * kick clients from the server with a reason
	 * 
	 * @param  reason                                      the reason of this
	 * @param  clids                                       cliend IDs of the client
	 *                                                     to kick
	 * @throws InsufficientClientPermissionsQueryException if the client haven't the
	 *                                                     permission to do this
	 *                                                     command
	 * @throws MessageTooLongException                     if the size of the reason
	 *                                                     is too long
	 * @see                                                TSClientQuery#MAX_KICK_MESSAGE_LENGTH
	 */
	public void kickFromServer(String reason, int... clids)
			throws InsufficientClientPermissionsQueryException, MessageTooLongException {
		if (reason.length() > MAX_KICK_MESSAGE_LENGTH)
			throw new MessageTooLongException();
		sendQuery(new ClientKickQuery(Reason.SERVER, reason, clids));
	}

	/**
	 * register an handler
	 * 
	 * @param handler the handler
	 */
	public synchronized void registerHandler(Handler handler) {
		HANDLERS.add(handler);
	}

	/**
	 * 
	 * send a poke to another client
	 * 
	 * @param  clientid                                    the client id to sent the
	 *                                                     poke
	 * @param  message                                     the poke text message
	 * @throws MessageTooLongException                     if the size of the
	 *                                                     message is too long
	 * @throws InsufficientClientPermissionsQueryException if the client hasn't the
	 *                                                     permission to do this
	 * @see                                                TSClientQuery#MAX_POKE_LENGTH
	 */
	public void sendPoke(int clientid, String message)
			throws InsufficientClientPermissionsQueryException, MessageTooLongException {
		if (message.length() > MAX_POKE_LENGTH)
			throw new MessageTooLongException();
		sendQuery(new ClientPokeQuery(message, clientid));
	}

	/**
	 * send a query
	 * 
	 * @param  <T>                                         query answer type
	 * @param  query                                       the query to send
	 * @return                                             the answer to this query
	 * @throws CommandNotFoundException                    if the query command
	 *                                                     isn't a valid one
	 * @throws NotConnectedQueryException                  if not connected to a
	 *                                                     server connection
	 * @throws InsufficientClientPermissionsQueryException if the client haven't the
	 *                                                     permission to do this
	 *                                                     command
	 * @throws UnRegisterQueryException                    if this query require to
	 *                                                     register a notify event
	 * @throws InvalidParameterQueryException              if the parameter aren't
	 *                                                     valid
	 * @throws CurrentlyNotPossibleQueryException          if the action is not
	 *                                                     possible at that time
	 */
	public synchronized <T extends Answer> T sendQuery(Query<T> query)
			throws CommandNotFoundException, InsufficientClientPermissionsQueryException, UnRegisterQueryException,
			InvalidParameterQueryException, NotConnectedQueryException, CurrentlyNotPossibleQueryException {
		checkStartedClient();
		Objects.requireNonNull(query, "query can't be null");
		synchronized (query) {
			if (query instanceof EventAnswerQuery)
				((EventAnswerQuery<?>) query).setSCHandlerid(this.usedSchandlerid.get());
			queue.add(query);
			try {
				query.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
				return (T) null;
			}
		}
		ErrorAnswer err = query.getError();
		if (err instanceof RequireRegisterAnswer)
			throw new UnRegisterQueryException((RequireRegisterAnswer) err);

		switch (err.getId()) {
		case ERROR_ID_COMMAND_NOT_FOUND:
			throw new CommandNotFoundException(err);
		case ERROR_ID_CURRENTLY_NOT_POSSIBLE:
			throw new CurrentlyNotPossibleQueryException(err);
		case ERROR_ID_NOT_CONNECTED:
			throw new NotConnectedQueryException(err);
		case ERROR_ID_INVALID_PARAMETER:
			throw new InvalidParameterQueryException(err);
		case ERROR_ID_INSUFFICIENT_CLIENT_PERMISSIONS:
			throw new InsufficientClientPermissionsQueryException(err);
		case ERROR_ID_OK:
		default:
			return query.getAnswer();
		}
	}

	/**
	 * send a message to the current channel chat
	 * 
	 * @param  message                                     the text message
	 * @throws MessageTooLongException                     if the size of the
	 *                                                     message is too long
	 * @throws InsufficientClientPermissionsQueryException if the client hasn't the
	 *                                                     permission to do this
	 * @see                                                TSClientQuery#MAX_MESSAGE_LENGTH
	 */
	public void sendTextMessageToChannel(String message)
			throws MessageTooLongException, InsufficientClientPermissionsQueryException {
		if (message.length() > MAX_MESSAGE_LENGTH)
			throw new MessageTooLongException();
		sendQuery(new SendTextMessageQuery(SendTextMessageQuery.TargetMode.CHANNEL, message));
	}

	/**
	 * send a message to another client
	 * 
	 * @param  clientid                                    the client id to sent the
	 *                                                     message
	 * @param  message                                     the text message
	 * @throws MessageTooLongException                     if the size of the
	 *                                                     message is too long
	 * @throws InsufficientClientPermissionsQueryException if the client hasn't the
	 *                                                     permission to do this
	 * @see                                                TSClientQuery#MAX_MESSAGE_LENGTH
	 */
	public void sendTextMessageToClient(int clientid, String message)
			throws MessageTooLongException, InsufficientClientPermissionsQueryException {
		if (message.length() > MAX_MESSAGE_LENGTH)
			throw new MessageTooLongException();
		sendQuery(new SendTextMessageQuery(clientid, message));
	}

	/**
	 * send a message to the current server chat
	 * 
	 * @param  message                                     the text message
	 * @throws MessageTooLongException                     if the size of the
	 *                                                     message is too long
	 * @throws InsufficientClientPermissionsQueryException if the client hasn't the
	 *                                                     permission to do this
	 * @see                                                TSClientQuery#MAX_MESSAGE_LENGTH
	 */
	public void sendTextMessageToServer(String message)
			throws InsufficientClientPermissionsQueryException, MessageTooLongException {
		if (message.length() > MAX_MESSAGE_LENGTH)
			throw new MessageTooLongException();
		sendQuery(new SendTextMessageQuery(SendTextMessageQuery.TargetMode.SERVER, message));
	}

	/**
	 * set the {@link RunnablesExecutor} to execute notify event, default
	 * implementation is {@link AsynchronousEventExecutor}
	 * 
	 * @param  runnablesExecutor    the runnables executor
	 * @throws NullPointerException if runnablesExecutor is null
	 * @see                         AsynchronousEventExecutor
	 * @see                         SynchronousEventExecutor
	 */
	public synchronized void setEventExecutor(RunnablesExecutor runnablesExecutor) {
		Objects.requireNonNull(runnablesExecutor);
		executor.stop();
		this.executor = runnablesExecutor;
		if (isStarted())
			runnablesExecutor.start();
	}

	/**
	 * Set the flood rate between every queries, default value is 250
	 * 
	 * @param floodRate the flood rate in millis
	 */
	public void setFloodRate(long floodRate) {
		if (floodRate < 0)
			throw new IllegalArgumentException("Negative flood rate!");
		synchronized (this) {
			this.floodRate = floodRate;
		}
	}

	/**
	 * start the client
	 * 
	 * @throws IOException           if the client can't create a valid socket
	 * @throws WrongAuthKeyException if the apikey is wrong
	 */
	public synchronized void start() throws IOException, WrongAuthKeyException {
		if (started)
			throw new AlreadyStartedException();

		socket = new Socket(address, port);

		currentQuery.set(null);
		queue.clear();

		executor.start();

		reader = new QueryReader(this, socket.getInputStream());
		writter = new QueryWritter(this, socket.getOutputStream());

		reader.start();
		writter.start();

		started = true;

		if (!auth()) {
			stop();
			throw new WrongAuthKeyException();
		}
	}

	/**
	 * stop the client
	 * 
	 * @throws UnStartedClientException if the client isn't started
	 * @throws IOException              if the client can't close the connection
	 */
	public synchronized void stop() throws IOException {
		checkStartedClient();
		reader.interrupt();
		writter.interrupt();
		socket.close();
		executor.stop();

		try {
			writter.join();
			reader.join();
		} catch (InterruptedException e) {
		} finally {
			started = false;

			reader = null;
			writter = null;
			socket = null;

			currentQuery.set(null);
			queue.clear();
		}
	}

	/**
	 * add a server group token to a server, the {@link EnumEvent#notifytokenadd}
	 * event must be registered
	 * 
	 * @param  serverGroupID                               the server group id
	 * @return                                             the created token
	 * @throws InsufficientClientPermissionsQueryException if the client hasn't the
	 *                                                     permission to do this
	 * @throws QueryException                              if an error occurred (bad
	 *                                                     serverGroupID)
	 * @throws UnRegisterQueryException                    if the
	 *                                                     {@link EnumEvent#notifytokenadd}
	 *                                                     event isn't registered
	 */
	public String tokenAdd(int serverGroupID)
			throws InsufficientClientPermissionsQueryException, UnRegisterQueryException, QueryException {
		TokenAddQuery q = new TokenAddQuery(serverGroupID);
		sendQuery(q);
		if (q.getError().getId() != QueryException.ERROR_ID_OK)
			throw new QueryException(q.getError());
		else
			return q.getAnswer().getToken();
	}

	/**
	 * add a channel group token to a server, the {@link EnumEvent#notifytokenadd}
	 * event must be registered
	 * 
	 * @param  channelGroupID                              the channel group id
	 * @param  channelID                                   the channel id
	 * @return                                             the created token
	 * @throws InsufficientClientPermissionsQueryException if the client hasn't the
	 *                                                     permission to do this
	 * @throws QueryException                              if an error occurred (bad
	 *                                                     channelGroupID or
	 *                                                     channelID) if the
	 *                                                     {@link EnumEvent#notifytokenadd}
	 *                                                     event isn't registered
	 */
	public String tokenAdd(int channelGroupID, int channelID)
			throws InsufficientClientPermissionsQueryException, UnRegisterQueryException, QueryException {
		TokenAddQuery q = new TokenAddQuery(channelGroupID, channelID);
		sendQuery(q);
		if (q.getError().getId() != QueryException.ERROR_ID_OK)
			throw new QueryException(q.getError());
		else
			return q.getAnswer().getToken();
	}

	/**
	 * delete a token from a server
	 * 
	 * @param  token                                       the token to delete
	 * @throws InsufficientClientPermissionsQueryException if the client hasn't the
	 *                                                     permission to do this
	 * @throws QueryException                              if an error occurred (bad
	 *                                                     token)
	 */
	public void tokenDelete(String token) throws InsufficientClientPermissionsQueryException, QueryException {
		ErrorAnswer a = sendQuery(new TokenDeleteQuery(token));
		if (a.getId() != QueryException.ERROR_ID_OK)
			throw new QueryException(a);
	}

	/**
	 * list of all tokens of the server, the {@link EnumEvent#notifytokenlist} event
	 * must be registered
	 * 
	 * @return                                             the token list
	 * @throws InsufficientClientPermissionsQueryException if the client hasn't the
	 *                                                     permission to do this
	 * @throws UnRegisterQueryException                    if the
	 *                                                     {@link EnumEvent#notifytokenlist}
	 *                                                     event isn't registered
	 */
	public List<Token> tokenList() throws InsufficientClientPermissionsQueryException, UnRegisterQueryException {
		return sendQuery(new TokenListQuery()).getTokenList();
	}

	/**
	 * use and delete a token from a server
	 * 
	 * @param  token                                       the token to delete
	 * @throws InsufficientClientPermissionsQueryException if the client hasn't the
	 *                                                     permission to do this
	 * @throws QueryException                              if an error occurred (bad
	 *                                                     token)
	 */
	public void tokenUse(String token) throws InsufficientClientPermissionsQueryException, QueryException {
		ErrorAnswer a = sendQuery(new TokenDeleteQuery(token));
		if (a.getId() != QueryException.ERROR_ID_OK)
			throw new QueryException(a);
	}

	/**
	 * unregister all registered handlers
	 */
	public synchronized void unregisterAllHandlers() {
		HANDLERS.clear();
	}

	/**
	 * unregister an handler
	 * 
	 * @param handler the handler
	 */
	public synchronized void unregisterHandler(Handler handler) {
		HANDLERS.remove(handler);
	}

	/**
	 * Selects the currently active server connection handler.
	 */
	public void use() {
		sendQuery(new UseQuery());
	}

	/**
	 * Selects the server connection handler scHandlerID
	 * 
	 * @param scHandlerID the server connection handler
	 */
	public void use(int scHandlerID) {
		sendQuery(new UseQuery(scHandlerID));
	}

	/**
	 * use a certain command to a schandlerID
	 * 
	 * @param schandlerID the server connection handler id
	 * @param and         the action to perform
	 */
	public synchronized void use(int schandlerID, Consumer<TSClientQuery> and) {
		use(schandlerID);
		and.accept(this);
	}

	/**
	 * check if we know a channel password
	 * 
	 * @param  channelID the channel ID
	 * @param  password  the password to try
	 * @return           true if the password is correct, false otherwise
	 */
	public boolean verifyChannelPassword(int channelID, String password) {
		return sendQuery(new VerifyChannelPasswordQuery(channelID, password)).getId() == QueryException.ERROR_ID_OK;
	}

	/**
	 * check if we know the server password
	 * 
	 * @param  password the password to try
	 * @return          true if the password is correct, false otherwise
	 */
	public boolean verifyServerPassword(String password) {
		return sendQuery(new VerifyServerPasswordQuery(password)).getId() == QueryException.ERROR_ID_OK;
	}

	/**
	 * request our ChannelID and our ClientID on this server connection
	 * 
	 * @return a {@link WhoAmIAnswer} with those information
	 */
	public WhoAmIAnswer whoAmI() {
		return sendQuery(new WhoAmIQuery());
	}

	private boolean auth() {
		try {
			sendQuery(new AuthQuery(apikey));
		} catch (InvalidParameterQueryException e) {
			return false;
		}
		return true;
	}

	private void checkStartedClient() throws UnStartedClientException {
		if (!isStarted())
			throw new UnStartedClientException();
	}
}
