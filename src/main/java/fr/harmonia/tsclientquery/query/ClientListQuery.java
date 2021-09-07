package fr.harmonia.tsclientquery.query;

import fr.harmonia.tsclientquery.answer.ClientListAnswer;
import fr.harmonia.tsclientquery.objects.ParsedObject;

/*
 * clientlist clid=1 cid=1 client_database_id=2 client_nickname=ATE47
 * client_type=0 error id=0 msg=ok
 */
public class ClientListQuery extends Query<ClientListAnswer> {

	public ClientListQuery() {
		this(true, true, true, true, true, true);
	}

	public ClientListQuery(boolean uid, boolean away, boolean voice, boolean groups, boolean icon, boolean country) {
		super("clientlist");
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
