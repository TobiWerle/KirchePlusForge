package fr.harmonia.tsclientquery.event;

import fr.harmonia.tsclientquery.objects.Client;
import fr.harmonia.tsclientquery.objects.ParsedObject;

class MutableClient extends Client {

	public MutableClient(ParsedObject client) {
		super(client);
	}

	public void changeChannel(int newChannelID) {
		set("cid", newChannelID);
	}
}
