package fr.harmonia.tsclientquery.query;

import fr.harmonia.tsclientquery.answer.BanAnswer;
import fr.harmonia.tsclientquery.objects.Ban;
import fr.harmonia.tsclientquery.objects.ParsedObject;

/*
 * 
 * help banadd Usage: banadd [ip={regexp}] [name={regexp}] [uid={clientUID}]
 * [time={timeInSeconds}] [banreason={text}]
 * 
 * Adds a new ban rule on the selected virtual server. All parameters are
 * optional but at least one of the following must be set: ip, name, or uid.
 * 
 * Example: banadd ip=1.2.3.4 banreason=just\s4\sfun banid=1 error id=0 msg=ok
 * 
 * error id=0 msg=ok
 * 
 */
public class BanAddQuery extends Query<BanAnswer> {

	public BanAddQuery(Ban ban) {
		super("banadd");
		String ip = ban.getIp();
		String regex = ban.getNameRegex();
		String uid = ban.getUid();
		long timeInSeconds = ban.getTimeInSeconds();
		String banreason = ban.getBanReason();
		if (ip != null)
			addArgument("ip", ip);
		if (regex != null)
			addArgument("name", regex);
		if (uid != null)
			addArgument("uid", uid);

		if (timeInSeconds >= 0)
			addArgument("time", timeInSeconds);

		if (banreason != null)
			addArgument("banreason", banreason);
	}

	@Override
	public void buildAnswer(ParsedObject obj) {
		answer = new BanAnswer(obj);
	}

}
