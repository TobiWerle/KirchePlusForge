package fr.harmonia.tsclientquery.answer;

import fr.harmonia.tsclientquery.objects.ChannelClient;
import fr.harmonia.tsclientquery.objects.ParsedObject;

import java.util.ArrayList;
import java.util.List;

/*
 * 
 * channelclientlist cid=1 -uid -away -voice -groups -icon -country clid=1 cid=1
 * client_database_id=2 client_nickname=ATE47 client_type=0 client_away=0
 * client_away_message client_flag_talking=0 client_input_muted=0
 * client_output_muted=1 client_input_hardware=0 client_output_hardware=1
 * client_talk_power=75 client_is_talker=0 client_is_priority_speaker=0
 * client_is_recording=0 client_is_channel_commander=0 client_is_muted=0
 * client_unique_identifier=5H1C8xKIFE3TQqp7i7P3IE7Jtgk= client_servergroups=6,7
 * client_channel_group_id=6 client_icon_id=0 client_country|clid=2 cid=1
 * client_database_id=4 client_nickname=Spktacular client_type=0 client_away=0
 * client_away_message client_flag_talking=0 client_input_muted=0
 * client_output_muted=1 client_input_hardware=1 client_output_hardware=1
 * client_talk_power=0 client_is_talker=0 client_is_priority_speaker=0
 * client_is_recording=0 client_is_channel_commander=0 client_is_muted=0
 * client_unique_identifier=Wk+H5VFRlAfGpj1nFwMjrS+Iv7s= client_servergroups=8
 * client_channel_group_id=8 client_icon_id=0 client_country
 */
public class ClientListAnswer extends Answer {
	private List<ChannelClient> list = new ArrayList<>();

	public ClientListAnswer() {
		super("");
	}

	public void addClients(ParsedObject obj) {
		list.addAll(obj.convertRowInto(ChannelClient::new));
	}

	public List<ChannelClient> getChannelClientList() {
		return list;
	}

}
