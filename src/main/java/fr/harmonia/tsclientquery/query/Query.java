package fr.harmonia.tsclientquery.query;

import fr.harmonia.tsclientquery.TSClientQuery;
import fr.harmonia.tsclientquery.answer.Answer;
import fr.harmonia.tsclientquery.answer.ErrorAnswer;
import fr.harmonia.tsclientquery.objects.ParsedObject;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class Query<T extends Answer> {
	private Map<String, String> data = new HashMap<>();
	private String name;

	private String option = "";
	protected T answer;

	protected ErrorAnswer error;

	public Query(String name) {
		this.name = name;
	}

	public void addError(ErrorAnswer error) {
		this.error = error;
	}

	public abstract void buildAnswer(ParsedObject obj);

	public String createCommand() {
		return name + " "
				+ data.entrySet().stream().map(e -> e.getKey() + '=' + e.getValue()).collect(Collectors.joining(" "))
				+ option;
	}

	public T getAnswer() {
		return answer;
	}

	public ErrorAnswer getError() {
		return error;
	}

	protected void addArgument(String key, Object value) {
		String k = TSClientQuery.encodeQueryStringParameter(key);
		String v = value instanceof Boolean ? (((Boolean) value).booleanValue() ? "1" : "0")
				: value instanceof Number ? String.valueOf(value)
						: TSClientQuery.encodeQueryStringParameter(String.valueOf(value));
		data.put(k, v);
	}

	protected void addOption(String opt) {
		option += ' ' + opt;
	}

}
