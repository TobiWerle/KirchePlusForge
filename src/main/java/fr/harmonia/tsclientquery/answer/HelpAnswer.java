package fr.harmonia.tsclientquery.answer;

import java.util.List;

public class HelpAnswer extends Answer {
	private List<String> lines;

	public HelpAnswer(String line) {
		super(line);
		lines.add(line);
	}

	public void addLine(String line) {
		lines.add(line);
	}

	public List<String> getLines() {
		return lines;
	}
}
