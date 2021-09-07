package fr.harmonia.tsclientquery.query;

import fr.harmonia.tsclientquery.answer.ClientListAnswer;
import fr.harmonia.tsclientquery.objects.ParsedObject;

/*
 * help channelclientlist Usage: channelclientlist cid=<cID> [-uid] [-away]
 * [-voice] [-groups] [-icon] [-country]
 * 
 * Displays a list of clients that are in the channel specified by the cid
 * parameter. Included information is the clientID, client database id,
 * nickname, channelID and client type. Please take note that you can only view
 * clients in channels that you are currently subscribed to.
 * 
 * Here is a list of the additional display paramters you will receive for each
 * of the possible modifier parameters.
 * 
 * -uid: client_unique_identifier
 * 
 * -away: client_away client_away_message
 * 
 * -voice: client_flag_talking client_input_muted client_output_muted
 * client_input_hardware client_output_hardware client_talk_power
 * client_is_talker client_is_priority_speaker client_is_recording
 * client_is_channel_commander client_is_muted
 * 
 * -groups: client_servergroups client_channel_group_id
 * 
 * -icon: client_icon_id
 * 
 * -country: client_country
 * 
 * Example: channelclientlist cid=184 clid=4 cid=184 client_database_id=35
 * client_nickname=MuhChy client_type=0 error id=0 msg=ok
 * 
 * error id=0 msg=ok
 */
public class ChannelClientListQuery extends Query<ClientListAnswer> {
	public ChannelClientListQuery(int cid) {
		this(cid, true, true, true, true, true, true);
	}

	public ChannelClientListQuery(int cid, boolean uid, boolean away, boolean voice, boolean groups, boolean icon,
			boolean country) {
		super("channelclientlist");
		addArgument("cid", cid);
		if (uid)
			addOption("-uid");
		if (away)
			addOption("-away");
		if (voice)
			addOption("-voice");
		if (groups)
			addOption("-groups");
		if (icon)
			addOption("-icon");
		if (country)
			addOption("-country");
		answer = new ClientListAnswer();
	}

	@Override
	public void buildAnswer(ParsedObject obj) {
		answer.addClients(obj);
	}

}
