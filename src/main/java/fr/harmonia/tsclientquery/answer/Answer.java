package fr.harmonia.tsclientquery.answer;

import fr.harmonia.tsclientquery.objects.ParsedObject;
import fr.harmonia.tsclientquery.query.Query;

/**
 * a {@link Query} answer, parse returned line into array of maps, to get access
 * by key / index, use an {@link OpenAnswer}
 * 
 * @author ATE47
 *
 */
public class Answer extends ParsedObject {

	public Answer(ParsedObject obj) {
		super(obj);
	}

	public Answer(String line) {
		super(line);
	}

}
