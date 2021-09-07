package fr.harmonia.tsclientquery.query;

import fr.harmonia.tsclientquery.answer.HelpAnswer;
import fr.harmonia.tsclientquery.objects.ParsedObject;

public class HelpQuery extends Query<HelpAnswer> {

	private String commandName;

	public HelpQuery() {
		this("");
	}

	public HelpQuery(String commandName) {
		super("help");
		this.commandName = commandName;
	}

	@Override
	public void buildAnswer(ParsedObject obj) {
		if (answer == null)
			answer = new HelpAnswer(obj.getLine());
		else
			answer.addLine(obj.getLine());
	}

	@Override
	public String createCommand() {
		return super.createCommand() + ' ' + commandName;
	}
}
