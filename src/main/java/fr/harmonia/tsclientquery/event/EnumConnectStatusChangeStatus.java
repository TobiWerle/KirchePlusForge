package fr.harmonia.tsclientquery.event;

public enum EnumConnectStatusChangeStatus {
	/**
	 * connected to server, ask default channel
	 */
	connected,
	/**
	 * try to connect to server
	 */
	connecting,
	/**
	 * ask every channel / server groups, client needed permissions, server data,
	 * subscribe
	 */
	connection_established,
	/**
	 * ask every channel, end with channellistfinished
	 */
	connection_establishing,
	/**
	 * when the client disconnect
	 */
	disconnected
}
