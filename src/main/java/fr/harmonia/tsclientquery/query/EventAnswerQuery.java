package fr.harmonia.tsclientquery.query;

import fr.harmonia.tsclientquery.answer.Answer;
import fr.harmonia.tsclientquery.event.EnumEvent;

public abstract class EventAnswerQuery<T extends Answer> extends Query<T> {
	private EnumEvent listenedEvent;
	private int schandlerid;

	public EventAnswerQuery(String name, EnumEvent listenedEvent) {
		super(name);
		this.listenedEvent = listenedEvent;
	}

	public EnumEvent getListenedEvent() {
		return listenedEvent;
	}

	public int getSCHandlerid() {
		return schandlerid;
	}

	public void setSCHandlerid(int schandlerid) {
		this.schandlerid = schandlerid;
	}

}
