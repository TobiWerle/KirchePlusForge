package fr.harmonia.tsclientquery.query;

import fr.harmonia.tsclientquery.answer.ClientFromAnswer;
import fr.harmonia.tsclientquery.event.EnumEvent;
import fr.harmonia.tsclientquery.objects.ParsedObject;

public class ClientFromIDQuery extends EventAnswerQuery<ClientFromAnswer> {

	public ClientFromIDQuery(int id) {
		super("clientgetuidfromclid", EnumEvent.notifyclientuidfromclid);
		addArgument("clid", id);
	}

	@Override
	public void buildAnswer(ParsedObject obj) {
		answer = new ClientFromAnswer(obj);
	}

}
