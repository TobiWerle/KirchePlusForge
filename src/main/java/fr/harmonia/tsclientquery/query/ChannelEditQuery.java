package fr.harmonia.tsclientquery.query;

import fr.harmonia.tsclientquery.objects.ChannelProperty;

import java.util.Objects;

public class ChannelEditQuery extends NoAnswerQuery {

	public ChannelEditQuery(int cid, ChannelProperty... properties) {
		super("channeledit");
		addArgument("cid", cid);
		for (ChannelProperty prop : properties)
			addArgument(
					Objects.requireNonNull(prop, "ChannelProperty can't be null").getProperty().getPropertyName(false),
					prop.getValue());
	}
}
