package fr.harmonia.tsclientquery.query;

import fr.harmonia.tsclientquery.answer.BanListAnswer;
import fr.harmonia.tsclientquery.event.EnumEvent;
import fr.harmonia.tsclientquery.objects.ParsedObject;

/*
 * Usage: banlist
 * 
 * Displays a list of active bans on the selected virtual server.
 * 
 * Example: banlist banid=7 ip=1.2.3.4 created=1259444002242 invokername=Sven
 * invokercldbid=56 invokeruid=oHhi9WzXLNEFQOwAu4JYKGU+C+c= reason
 * enforcements=0
 * 
 * 
 * error id=0 msg=ok
 * 
 * notifybanlist schandlerid=2
 * 
 * banid=2 ip name=Le\struc uid mytsid lastnickname created=1565268333
 * duration=0
 * 
 * invokername=ATE47 invokercldbid=2 invokeruid=5H1C8xKIFE3TQqp7i7P3IE7Jtgk=
 * reason=Michel enforcements=0
 * 
 * | ...
 * 
 */

public class BanListQuery extends EventAnswerQuery<BanListAnswer> {

	public BanListQuery() {
		super("banlist", EnumEvent.notifybanlist);
		answer = new BanListAnswer();
	}

	@Override
	public void buildAnswer(ParsedObject obj) {
		answer.addToList(obj);
	}

}
