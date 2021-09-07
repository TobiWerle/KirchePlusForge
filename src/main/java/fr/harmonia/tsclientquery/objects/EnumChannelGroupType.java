package fr.harmonia.tsclientquery.objects;

public enum EnumChannelGroupType {
	REGULAR(0), TEMPLATE(1);

	private final int id;

	private EnumChannelGroupType(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
