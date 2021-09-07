package fr.harmonia.tsclientquery.query;

import fr.harmonia.tsclientquery.answer.TokenAddAnswer;
import fr.harmonia.tsclientquery.event.EnumEvent;
import fr.harmonia.tsclientquery.objects.ParsedObject;

/*
 * tokenadd tokentype=0 tokenid1=6 tokenid2=0 notifytokenadd schandlerid=1
 * token=71tGEQoq7+MpaLDUIYbhgyCrjA4AMdAxSz4dYyDi error id=0 msg=ok
 * 
 */
public class TokenAddQuery extends EventAnswerQuery<TokenAddAnswer> {
	public TokenAddQuery(int sgid) {
		this(false, sgid, 0);
	}

	public TokenAddQuery(int cgid, int cid) {
		this(true, cgid, cid);
	}

	private TokenAddQuery(boolean tokentype, int tokenid1, int tokenid2) {
		super("tokenadd", EnumEvent.notifytokenadd);
		addArgument("tokentype", tokentype);
		addArgument("tokenid1", tokenid1);
		addArgument("tokenid2", tokenid2);

	}

	@Override
	public void buildAnswer(ParsedObject obj) {
		answer = new TokenAddAnswer(obj);
	}

}
