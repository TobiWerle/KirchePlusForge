package fr.harmonia.tsclientquery.query;

import fr.harmonia.tsclientquery.answer.MultipleBanAnswer;
import fr.harmonia.tsclientquery.objects.ClientBan;
import fr.harmonia.tsclientquery.objects.ParsedObject;

/*
 * Usage: banclient clid={clientID}|cldbid={clientDatabaseID}|uid={clientUID} \
 * [time={timeInSeconds}] [banreason={text}]
 * 
 * Bans the client specified with ID clid from the server. Please note that this
 * will create two separate ban rules for the targeted clients IP address and
 * his unique identifier.
 * 
 * Note that banning via cldbid parameter requires a 3.0.1 server version. Note
 * that banning via uid parameter requires a 3.0.2 server version.
 * 
 * Example: banclient clid=4 time=3600 banid=2 banid=3 error id=0 msg=ok
 * 
 * 
 * error id=0 msg=ok
 * 
 */

public class BanClientQuery extends Query<MultipleBanAnswer> {

	public BanClientQuery(ClientBan ban) {
		super("banclient");
		int clid = ban.getClientID();
		int cldbid = ban.getClientDatabaseID();
		String uid = ban.getUid();
		long timeInSeconds = ban.getTimeInSeconds();
		String banreason = ban.getBanReason();
		if (clid >= 0)
			addArgument("clid", clid);
		if (cldbid >= 0)
			addArgument("cldbid", cldbid);
		if (timeInSeconds >= 0)
			addArgument("time", timeInSeconds);

		if (uid != null)
			addArgument("uid", uid);
		if (banreason != null)
			addArgument("banreason", banreason);
		answer = new MultipleBanAnswer();
	}

	@Override
	public void buildAnswer(ParsedObject obj) {
		answer.addBan(obj);
	}
}
