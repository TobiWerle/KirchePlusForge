package fr.harmonia.tsclientquery.objects;

import fr.harmonia.tsclientquery.TSClientQuery;
import fr.harmonia.tsclientquery.answer.Answer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.IntFunction;

public class ParsedObject {
	@SuppressWarnings("unchecked")
	private static Map<String, String>[] parseLines(String line, IntFunction<Map<String, String>> mapConstructor) {
		String[] raw = ((line.startsWith("error") ? line.substring("error ".length()) : line).split("\\|"));
		Map<String, String>[] data;

		if (raw.length == 0 || raw[0].isEmpty()) {
			data = new Map[] { mapConstructor.apply(0) };
		} else {
			data = new Map[raw.length];

			for (int i = 0; i < data.length; i++)
				data[i] = parseMap(raw[i], mapConstructor);
		}

		return data;
	}

	private static Map<String, String> parseMap(String raw, IntFunction<Map<String, String>> mapConstructor) {
		String[] arguments = raw.split(" ");

		Map<String, String> data = mapConstructor.apply(arguments.length != 0 ? arguments.length : 1);

		for (String arg : arguments) {
			String[] a = arg.split("[=]", 2);
			if (a.length == 2) {
				data.put(TSClientQuery.decodeQueryStringParameter(a[0]),
						TSClientQuery.decodeQueryStringParameter(a[1]));
			} else {
				data.put(TSClientQuery.decodeQueryStringParameter(a[0]), "");
			}
		}

		return data;
	}

	private Map<String, String>[] data;

	private int index = 0;

	private String line;

	public ParsedObject(ParsedObject p) {
		this.line = p.line;
		this.index = p.index;
		this.data = p.data;
	}

	public ParsedObject(String line) {
		this.line = line;
	}

	/**
	 * Convert this object to a list
	 * 
	 * @param  <R>       the list type
	 * @param  converter the parser
	 * @return           the new list
	 * 
	 */
	public <R> List<R> convertRowInto(Function<ParsedObject, R> converter) {
		List<R> l = new ArrayList<>();
		while (rowNotEmpty()) {
			R r = converter.apply(this);
			l.add(r);
			next();
		}
		return l;
	}

	/**
	 * parse the line if not already done (by asking a value) and return the data
	 * map
	 * 
	 * @return     the argument map
	 * @deprecated only for internal use
	 */
	@Deprecated
	public synchronized Map<String, String>[] getData() {
		if (data == null)
			return this.data = parseLines(line, ConcurrentHashMap::new);
		else
			return data;
	}

	/**
	 * @return the data line receive
	 */
	public String getLine() {
		return line;
	}

	/**
	 * go to the next row combine with {@link Answer#rowNotEmpty()} to create an
	 * iterator
	 */
	public void next() {
		if (!rowNotEmpty())
			new IllegalArgumentException("No next row");
		++index;
	}

	/**
	 * @return true while the row pointed isn't empty
	 */
	public boolean rowNotEmpty() {
		return index != getData().length && !data[index].isEmpty();
	}

	/**
	 * @return get the number of rows returned
	 */
	public int rowsCount() {
		return getData().length;
	}

	@Override
	public String toString() {
		return Arrays.toString(getData());
	}

	/**
	 * update those data with a new {@link ParsedObject}
	 * 
	 * @param object the new object
	 * @param share  if old data must be shared or merge in this object
	 */
	public void update(ParsedObject object, boolean share) {
		if (!share) {
			update(object.data);
		} else {
			synchronized (this) {
				this.line = object.line;
				data = object.data;
			}
		}
	}

	/**
	 * update those data with a new line
	 * 
	 * @param line  the new line
	 * @param reset if old data must be delete
	 */
	public void update(String line, boolean reset) {
		if (!reset) {
			update(parseLines(line, HashMap::new));
		} else {
			synchronized (this) {
				this.line = line;
				data = null;
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void update(Map<String, String>[] newData) {
		Map<String, String>[] oldData = getData();
		if (newData.length > oldData.length)
			synchronized (this) {
				data = new Map[newData.length];
				System.arraycopy(oldData, 0, data, 0, oldData.length);
				for (int i = oldData.length; i < data.length; i++)
					data[i] = newData[i];
			}
		for (int i = 0; i < oldData.length && i < newData.length; i++)
			newData[i].forEach(data[i]::put);

	}

	/**
	 * get a key value
	 * 
	 * @param  index the row index
	 * @param  key   the key
	 * @return       the value, or null if inexistent
	 */
	protected String get(int index, String key) {
		return getData()[index].get(key);
	}

	/**
	 * get a key value for the pointed row
	 * 
	 * @param  key the key
	 * @return     the value, or null if inexistent
	 */
	protected String get(String key) {
		return get(index, key);
	}

	/**
	 * get a key value as a boolean
	 * 
	 * @param  index the row index
	 * @param  key   the key
	 * @return       the value, or false if inexistent
	 */
	protected boolean getBoolean(int index, String key) {
		return getInteger(index, key) != 0;
	}

	/**
	 * get a key value as a boolean for the pointed row
	 * 
	 * @param  key the key
	 * @return     the value, or false if inexistent
	 */
	protected boolean getBoolean(String key) {
		return getBoolean(index, key);
	}

	/**
	 * get a key value as a integer
	 * 
	 * @param  index the row index
	 * @param  key   the key
	 * @return       the value, or 0 if inexistent
	 */
	protected int getInteger(int index, String key) {
		String value = getData()[index].get(key);
		return value == null || value.isEmpty() ? 0 : Integer.parseInt(value);
	}

	/**
	 * get a key value as a integer for pointed row
	 * 
	 * @param  key the key
	 * @return     the value, or 0 if inexistent
	 */
	protected int getInteger(String key) {
		return getInteger(index, key);
	}

	/**
	 * get a key value as a long
	 * 
	 * @param  index the row index
	 * @param  key   the key
	 * @return       the value, or 0 if inexistent
	 */
	protected long getLong(int index, String key) {
		String value = getData()[index].get(key);
		return value == null ? 0L : Long.parseLong(value);
	}

	/**
	 * get a key value as a long for pointed row
	 * 
	 * @param  key the key
	 * @return     the value, or 0 if inexistent
	 */
	protected long getLong(String key) {
		return getLong(index, key);
	}

	protected void set(String key, Object value) {
		getData()[index].put(key, String.valueOf(value));
	}

}
