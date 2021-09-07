package fr.harmonia.tsclientquery.query;

import fr.harmonia.tsclientquery.TSClientQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DelPermQuery<T extends DelPermQuery<T>> extends NoAnswerQuery {
	private List<String> permsid;

	public DelPermQuery(String name, String permsid) {
		super(name);
		this.permsid = new ArrayList<String>();
		addPerm(permsid);
	}

	@SuppressWarnings("unchecked")
	public T addPerm(String permsid) {
		this.permsid.add(permsid);
		return (T) this;
	}

	@Override
	public String createCommand() {
		return super.createCommand() + ' ' + permsid.stream()
				.map(k -> "permsid=" + TSClientQuery.encodeQueryStringParameter(k)).collect(Collectors.joining("|"));
	}

}
