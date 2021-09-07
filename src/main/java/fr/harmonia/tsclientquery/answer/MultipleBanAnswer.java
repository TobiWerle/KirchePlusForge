package fr.harmonia.tsclientquery.answer;

import fr.harmonia.tsclientquery.objects.ParsedObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MultipleBanAnswer extends Answer implements Iterable<BanAnswer> {
	private List<BanAnswer> list;

	public MultipleBanAnswer() {
		super("");
		list = new ArrayList<>();
	}

	public void addBan(ParsedObject obj) {
		list.add(new BanAnswer(obj));
	}

	public List<BanAnswer> getBanList() {
		return list;
	}

	@Override
	public Iterator<BanAnswer> iterator() {
		return list.iterator();
	}

}
