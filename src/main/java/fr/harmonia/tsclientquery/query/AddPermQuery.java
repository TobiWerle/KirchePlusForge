package fr.harmonia.tsclientquery.query;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AddPermQuery<T extends AddPermQuery<T>> extends NoAnswerQuery {
	private interface Perm {
		String getIdArgument();

		int getValue();
	}

	private class PermByID implements Perm {
		int permid;
		int permvalue;

		public PermByID(int permid, int permvalue) {
			this.permid = permid;
			this.permvalue = permvalue;
		}

		@Override
		public String getIdArgument() {
			return "permid=" + permid;
		}

		@Override
		public int getValue() {
			return permvalue;
		}
	}

	private class PermByName implements Perm {
		String permsid;
		int permvalue;

		public PermByName(String permsid, int permvalue) {
			this.permsid = permsid;
			this.permvalue = permvalue;
		}

		@Override
		public String getIdArgument() {
			return "permsid=" + permsid;
		}

		@Override
		public int getValue() {
			return permvalue;
		}
	}

	private List<Perm> perms = new ArrayList<>();

	public AddPermQuery(String name, int permid, int permvalue) {
		super(name);
		addPerm(permid, permvalue);
	}

	public AddPermQuery(String name, String permsid, int permvalue) {
		super(name);
		addPerm(permsid, permvalue);
	}

	@SuppressWarnings("unchecked")
	public T addPerm(int permid, int permvalue) {
		perms.add(new PermByID(permid, permvalue));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T addPerm(String permsid, int permvalue) {
		perms.add(new PermByName(permsid, permvalue));
		return (T) this;
	}

	@Override
	public String createCommand() {
		return super.createCommand() + ' ' + perms.stream().map(k -> k.getIdArgument() + " permvalue=" + k.getValue())
				.collect(Collectors.joining("|"));
	}

}
