package fr.harmonia.tsclientquery.query;

public class DisconnectQuery extends NoAnswerQuery {

	public DisconnectQuery() {
		super("disconnect");
	}

	public DisconnectQuery(String message) {
		this();
		addArgument("msg", message);
	}

}
