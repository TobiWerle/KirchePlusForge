package fr.harmonia.tsclientquery.objects;

import java.util.Objects;

public class ServerConnection {

	private String address;
	private String channel;
	private String channel_pw;
	private String nickname;
	private String password;
	private String token;

	public ServerConnection(String serverAddress, String serverPassword, String nickname, String channelName,
			String channelPassword, String token) {
		this.address = Objects.requireNonNull(serverAddress, "serverAddress must be not null");
		this.password = serverPassword;
		this.nickname = nickname;
		this.channel = channelName;
		this.channel_pw = channelPassword;
		this.token = token;
	}

	public String getChannelName() {
		return channel;
	}

	public String getChannelPassword() {
		return channel_pw;
	}

	public String getNickname() {
		return nickname;
	}

	public String getServerAddress() {
		return address;
	}

	public String getServerPassword() {
		return password;
	}

	public String getToken() {
		return token;
	}
}
