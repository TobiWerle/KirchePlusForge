package fr.harmonia.tsclientquery.objects.builder;

import fr.harmonia.tsclientquery.objects.ServerConnection;

import java.util.Objects;

public class ServerConnectionBuilder {

	private String address;
	private String channel;
	private String channel_pw;
	private String nickname;
	private String password;
	private String token;

	public ServerConnectionBuilder(String serverAddress) {
		this.address = Objects.requireNonNull(serverAddress, "serverAddress must be not null");
	}

	public ServerConnection build() {
		return new ServerConnection(address, password, nickname, channel, channel_pw, token);
	}

	public ServerConnectionBuilder withChannelName(String channelName) {
		this.channel = channelName;
		return this;
	}

	public ServerConnectionBuilder withChannelPassword(String channelPassword) {
		this.channel_pw = channelPassword;
		return this;
	}

	public ServerConnectionBuilder withNickname(String nickname) {
		this.nickname = nickname;
		return this;
	}

	public ServerConnectionBuilder withServerPassword(String serverPassword) {
		this.password = serverPassword;
		return this;
	}

	public ServerConnectionBuilder withToken(String token) {
		this.token = token;
		return this;
	}
}
