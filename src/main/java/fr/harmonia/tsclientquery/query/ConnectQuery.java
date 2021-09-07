package fr.harmonia.tsclientquery.query;

import fr.harmonia.tsclientquery.objects.ServerConnection;

import java.util.Objects;

/*
 * help connect Usage: connect address={address} [password={password}]
 * [nickname={nickname}] [channel={channel}] [channel_pw={channel_pw}]
 * [token={token}]
 * 
 * 
 * Connects to a server on the currently selected connection handler. Note, that
 * you should register for 'notifyconnectstatuschange' events to check wether or
 * not the connection was established successfully.
 * 
 * Example: connect address=127.0.0.1 nickname=ClientQuery error id=0 msg=ok
 * 
 * 
 * error id=0 msg=ok
 */
public class ConnectQuery extends NoAnswerQuery {

	public ConnectQuery(ServerConnection serverConnection) {
		super("connect");
		Objects.requireNonNull(serverConnection, "serverConnection can't be null");
		addArgument("address", serverConnection.getServerAddress());
		String channel = serverConnection.getChannelName();
		String channel_pw = serverConnection.getChannelPassword();
		String nickname = serverConnection.getNickname();
		String password = serverConnection.getServerPassword();
		String token = serverConnection.getToken();

		if (channel != null)
			addArgument("channel", channel);

		if (channel_pw != null)
			addArgument("channel_pw", channel_pw);

		if (nickname != null)
			addArgument("nickname", nickname);

		if (password != null)
			addArgument("password", password);

		if (token != null)
			addArgument("token", token);
	}

}
