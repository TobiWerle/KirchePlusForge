package fr.harmonia.tsclientquery.answer;

import fr.harmonia.tsclientquery.objects.DataBaseBan;
import fr.harmonia.tsclientquery.objects.ParsedObject;

import java.util.ArrayList;
import java.util.List;

public class BanListAnswer extends Answer {
	private List<DataBaseBan> bans;

	public BanListAnswer() {
		super("");
		bans = new ArrayList<>();
	}

	public void addToList(ParsedObject obj) {
		OpenAnswer oa = new OpenAnswer(obj);
		while (oa.rowNotEmpty()) {
			bans.add(new DataBaseBan(oa.getInteger("banid"), oa.get("ip"), oa.get("name"), oa.get("uid"),
					oa.get("lastnickname"), oa.getLong("created"), oa.getLong("duration"), oa.get("invokername"),
					oa.getInteger("invokercldbid"), oa.get("invokeruid"), oa.get("reason"),
					oa.getInteger("enforcements")));
			oa.next();
		}
	}

	public List<DataBaseBan> getBanList() {
		return bans;
	}

}
