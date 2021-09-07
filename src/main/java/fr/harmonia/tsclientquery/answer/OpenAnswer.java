package fr.harmonia.tsclientquery.answer;

import fr.harmonia.tsclientquery.objects.ParsedObject;

/**
 * a basic {@link Answer} with getter by key
 * 
 * @author ATE47
 *
 */
public class OpenAnswer extends Answer {

	public OpenAnswer(ParsedObject obj) {
		super(obj);
	}

	public OpenAnswer(String line) {
		super(line);
	}

	@Override
	public String get(int index, String key) {
		return super.get(index, key);
	}

	@Override
	public String get(String key) {
		return super.get(key);
	}

	@Override
	public boolean getBoolean(int index, String key) {
		return super.getBoolean(index, key);
	}

	@Override
	public boolean getBoolean(String key) {
		return super.getBoolean(key);
	}

	@Override
	public int getInteger(int index, String key) {
		return super.getInteger(index, key);
	}

	@Override
	public int getInteger(String key) {
		return super.getInteger(key);
	}

	@Override
	public void set(String key, Object value) {
		super.set(key, value);
	}

	@Override
	protected long getLong(int index, String key) {
		return super.getLong(index, key);
	}

	@Override
	protected long getLong(String key) {
		return super.getLong(key);
	}
}
