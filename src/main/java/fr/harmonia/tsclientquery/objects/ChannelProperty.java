package fr.harmonia.tsclientquery.objects;

import fr.harmonia.tsclientquery.TSClientQuery;

public class ChannelProperty {
	private final EnumChannelProperty property;
	private final Object value;

	/**
	 * a channel property to send
	 * 
	 * @param property type
	 * @param value    value, {@link EnumChannelProperty#password} value shouldn't
	 *                 be prehash
	 */
	public ChannelProperty(EnumChannelProperty property, Object value) {
		this.property = property;
		this.value = value;
	}

	public EnumChannelProperty getProperty() {
		return property;
	}

	public Object getValue() {
		if (property == EnumChannelProperty.password)
			return TSClientQuery.hashPassword(String.valueOf(value));
		else
			return value;
	}

}
