package fr.harmonia.tsclientquery.query;

import fr.harmonia.tsclientquery.objects.ChannelProperty;
import fr.harmonia.tsclientquery.objects.EnumChannelProperty;

import java.util.Objects;

/*
 * help channelcreate Usage: channelcreate channel_name={channelName}
 * [channel_properties...]
 * 
 * Creates a new channel using the given properties and displays its ID.
 * 
 * N.B. The channel_password property needs a hashed password as a value. The
 * hash is a sha1 hash of the password, encoded in base64. You can use the
 * "hashpassword" command to get the correct value.
 * 
 * Example: channelcreate channel_name=My\sChannel channel_topic=My\sTopic
 * cid=16 error id=0 msg=ok
 * 
 * error id=0 msg=ok
 * 
 */
public class ChannelCreateQuery extends NoAnswerQuery {
	public ChannelCreateQuery(String name, ChannelProperty... properties) {
		super("channelcreate");
		addArgument(EnumChannelProperty.name.getPropertyName(false), name);
		for (ChannelProperty prop : properties)
			addArgument(
					Objects.requireNonNull(prop, "ChannelProperty can't be null").getProperty().getPropertyName(false),
					prop.getValue());
	}

}
