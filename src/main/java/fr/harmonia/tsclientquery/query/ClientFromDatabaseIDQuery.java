package fr.harmonia.tsclientquery.query;

import fr.harmonia.tsclientquery.answer.ClientFromAnswer;
import fr.harmonia.tsclientquery.event.EnumEvent;
import fr.harmonia.tsclientquery.objects.ParsedObject;

public class ClientFromDatabaseIDQuery extends EventAnswerQuery<ClientFromAnswer> {

	public ClientFromDatabaseIDQuery(int dbid) {
		super("clientgetnamefromdbid", EnumEvent.notifyclientnamefromdbid);
		addArgument("cldbid", dbid);
	}

	@Override
	public void buildAnswer(ParsedObject obj) {
		answer = new ClientFromAnswer(obj);
	}

}
