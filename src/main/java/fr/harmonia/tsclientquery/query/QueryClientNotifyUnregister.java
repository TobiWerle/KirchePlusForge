package fr.harmonia.tsclientquery.query;

import fr.harmonia.tsclientquery.event.EnumEvent;

public class QueryClientNotifyUnregister extends NoAnswerQuery {

	public QueryClientNotifyUnregister(int schandlerid, EnumEvent event) {
		super("clientnotifyunregister");
		addArgument("schandlerid", schandlerid);
		addArgument("event", event);
	}

}
