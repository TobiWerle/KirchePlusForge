package fr.harmonia.tsclientquery.event;

import fr.harmonia.tsclientquery.TSClientQuery;
import fr.harmonia.tsclientquery.answer.WhoAmIAnswer;
import fr.harmonia.tsclientquery.exception.QueryException;
import fr.harmonia.tsclientquery.objects.ChannelClient;
import fr.harmonia.tsclientquery.objects.Client;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class BasicClientListHandler implements Handler {
	public class ClientListCollection implements Collection<Client> {
		private ServerConnectionHandlerData data;

		private ClientListCollection() {
		}

		@Override
		public boolean add(Client e) {
			return data.CLID_TO_CLIENT.values().add(e);
		}

		@Override
		public boolean addAll(Collection<? extends Client> c) {
			return data.CLID_TO_CLIENT.values().addAll(c);
		}

		@Override
		public void clear() {
			data.CLID_TO_CLIENT.values().clear();
		}

		@Override
		public boolean contains(Object o) {
			return data.CLID_TO_CLIENT.values().contains(o);
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			return data.CLID_TO_CLIENT.values().containsAll(c);
		}

		/**
		 * @return a static collection of clients in the same channel ID as our
		 *         connection
		 */
		public Collection<Client> getChannelClient() {
			return getChannelClient(data.cid);
		}

		/**
		 * @param  cid the channel id
		 * @return     static collection of clients in a channel
		 */
		public Collection<Client> getChannelClient(int cid) {
			return data.CLID_TO_CLIENT.values().stream().filter(c -> c.getChannelID() == cid)
					.collect(Collectors.toList());
		}

		@Override
		public boolean isEmpty() {
			return data.CLID_TO_CLIENT.values().isEmpty();
		}

		@Override
		public Iterator<Client> iterator() {
			return data.CLID_TO_CLIENT.values().iterator();
		}

		@Override
		public boolean remove(Object o) {
			return data.CLID_TO_CLIENT.values().remove(o);
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			return data.CLID_TO_CLIENT.values().removeAll(c);
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			return data.CLID_TO_CLIENT.values().retainAll(c);
		}

		@Override
		public int size() {
			return data.CLID_TO_CLIENT.values().size();
		}

		@Override
		public Object[] toArray() {
			return data.CLID_TO_CLIENT.values().toArray();
		}

		@Override
		public <T> T[] toArray(T[] a) {
			return data.CLID_TO_CLIENT.values().toArray(a);
		}

	}

	private class ServerConnectionHandlerData {
		int cid, clid;

		ConcurrentMap<Integer, Client> CLID_TO_CLIENT;

		public ServerConnectionHandlerData(ConcurrentMap<Integer, Client> clientMap) {
			CLID_TO_CLIENT = clientMap;
		}

		private void doForClient(int clid, Consumer<MutableClient> modifier) {
			MutableClient client = (MutableClient) CLID_TO_CLIENT.get(clid);
			if (client != null)
				modifier.accept(client);
		}
	}

	private TSClientQuery client;
	// best name ever
	private final ConcurrentMap<Integer, ServerConnectionHandlerData> SCHANDLERID_TO_SERVERDATA = new ConcurrentHashMap<>();
	private final ClientListCollection viewClients = new ClientListCollection();

	public BasicClientListHandler(TSClientQuery client) {
		this.client = client;
	}

	public ServerConnectionHandlerData createOrGetServerConnectionHandlerData(int schandlerid) {
		return SCHANDLERID_TO_SERVERDATA.computeIfAbsent(schandlerid,
				id -> new ServerConnectionHandlerData(new ConcurrentHashMap<Integer, Client>()));
	}

	public Client getClient(int schandlerid, int clid) {
		ServerConnectionHandlerData data = SCHANDLERID_TO_SERVERDATA.get(schandlerid);

		if (data == null)
			return null;

		return data.CLID_TO_CLIENT.get(clid);
	}

	/**
	 * get a view of the {@link Client} of a server connection
	 * 
	 * @param  schandlerid the server connection to view
	 * @return             a collection of client
	 */
	public Collection<Client> getClients(int schandlerid) {
		return createOrGetServerConnectionHandlerData(schandlerid).CLID_TO_CLIENT.values();
	}

	public Collection<Client> getCurrentChannelClients() {
		return getViewClients().getChannelClient();
	}

	/**
	 * get a view of the {@link Client} of the server connection, when the use
	 * change the connection, it reflex on this collection
	 * 
	 * @return a collection of client
	 */
	public ClientListCollection getViewClients() {
		return viewClients;
	}

	/**
	 * init the handler
	 */
	public void init() {
		client.clientNotifyRegister(EnumEvent.notifyclientleftview);
		client.clientNotifyRegister(EnumEvent.notifycliententerview);
		client.clientNotifyRegister(EnumEvent.notifyclientmoved);
		client.clientNotifyRegister(EnumEvent.notifycurrentserverconnectionchanged);
		client.clientNotifyRegister(EnumEvent.notifyconnectstatuschange);
		client.clientNotifyRegister(EnumEvent.notifyclientupdated);

		int schandlerid = client.currentServerConnectionHandlerID();

		queryClients(schandlerid);

		viewClients.data = createOrGetServerConnectionHandlerData(schandlerid);
	}

	@Override
	public void onChangeCurrentServerConnection(int schandlerid) {
		if (!SCHANDLERID_TO_SERVERDATA.containsKey(schandlerid))
			queryClients(schandlerid);
		viewClients.data = createOrGetServerConnectionHandlerData(schandlerid);
	}

	@Override
	public void onClientConnect(int schandlerid, int channelToID, Client client) {
		addClient(schandlerid, client);
	}

	@Override
	public void onClientDisconnect(int schandlerid, int channelFromID, int clientID) {
		deleteClient(schandlerid, clientID);
	}

	@Override
	public void onClientEnterView(int schandlerid, int channelFromID, int channelToID, Client client) {
		addClient(schandlerid, client);
	}

	@Override
	public void onClientEnterViewMoved(int schandlerid, int channelFromID, int channelToID, int invokerid,
			String invokername, String invokeruid, Client client) {
		addClient(schandlerid, client);
	}

	@Override
	public void onClientKickFromChannel(int schandlerid, int channelTargetID, int invokerClientID, String invokerName,
			String invokerUID, String reasonmsg, int clientID) {
		moveClient(schandlerid, channelTargetID, clientID);
	}

	@Override
	public void onClientKickFromChannelOutOfView(int schandlerid, int channelFromID, int channelToID, int invokerid,
			String invokername, String invokeruid, String reasonmsg, Client client) {
		addClient(schandlerid, client);
	}

	@Override
	public void onClientKickFromServer(int schandlerid, int channelFromID, int invokerClientID, String invokerName,
			String invokerUID, String reasonmsg, int clientID) {
		deleteClient(schandlerid, clientID);
	}

	@Override
	public void onClientLeftView(int schandlerid, int channelTargetID, int clientID) {
		deleteClient(schandlerid, clientID);
	}

	@Override
	public void onClientMove(int schandlerid, int channelTargetID, int clientID) {
		moveClient(schandlerid, channelTargetID, clientID);
	}

	@Override
	public void onClientMovedByOther(int schandlerid, int channelTargetID, int invokerClientID, String invokerName,
			String invokerUID, int clientID) {
		moveClient(schandlerid, channelTargetID, clientID);
	}

	@Override
	public void onClientUpdated(int schandlerid, Client client) {
		mergeClientOrCreate(schandlerid, client);
	}

	@Override
	public void onConnectionEstablished(int schandlerid) {
		queryClients(schandlerid);
	}

	@Override
	public void onDisconnected(int schandlerid, int error) {
		ServerConnectionHandlerData data = SCHANDLERID_TO_SERVERDATA.get(schandlerid);
		data.cid = 0;
		data.clid = 0;
		data.CLID_TO_CLIENT.clear();
	}

	private void addClient(int schandlerid, ChannelClient client) {
		mergeClientOrCreate(schandlerid, client);
	}

	private void deleteClient(int schandlerid, int clientID) {
		moveClient(schandlerid, 0, clientID);
	}

	private void mergeClientOrCreate(int schandlerid, ChannelClient client) {
		ConcurrentMap<Integer, Client> clidMap = SCHANDLERID_TO_SERVERDATA.computeIfAbsent(schandlerid,
				id -> new ServerConnectionHandlerData(new ConcurrentHashMap<Integer, Client>())).CLID_TO_CLIENT;
		int clid = client.getClientID();
		MutableClient old = (MutableClient) clidMap.get(clid);
		if (old == null) {
			clidMap.put(clid, new MutableClient(client));
		} else {
			old.update(client, false);
		}
	}

	private ServerConnectionHandlerData mergeClientOrCreate(int schandlerid, List<ChannelClient> clients) {
		ServerConnectionHandlerData data = SCHANDLERID_TO_SERVERDATA.computeIfAbsent(schandlerid,
				id -> new ServerConnectionHandlerData(new ConcurrentHashMap<Integer, Client>()));
		ConcurrentMap<Integer, Client> clidMap = data.CLID_TO_CLIENT;
		for (ChannelClient client : clients) {
			int clid = client.getClientID();
			MutableClient old = (MutableClient) clidMap.get(clid);
			if (old == null) {
				clidMap.put(clid, new MutableClient(client));
			} else {
				old.update(client, false);
			}
		}
		return data;
	}

	private void moveClient(int schandlerid, int channelToID, int clientID) {
		ServerConnectionHandlerData data = createOrGetServerConnectionHandlerData(schandlerid);
		data.doForClient(clientID, c -> c.changeChannel(channelToID));
		if (data.clid == clientID) {
			data.cid = channelToID;
			mergeClientOrCreate(schandlerid,
					client.channelClientList(channelToID, false, false, true, false, false, false));
		}
	}

	private void queryClients(int schandlerid) {
		try {
			client.use(schandlerid, client -> {
				ServerConnectionHandlerData data = mergeClientOrCreate(schandlerid, client.clientList());
				WhoAmIAnswer wai = client.whoAmI();
				data.cid = wai.getChannelID();
				data.clid = wai.getClientID();
			});
		} catch (QueryException e) {
		}
	}

}
