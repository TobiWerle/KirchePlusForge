package fr.harmonia.tsclientquery.exception;

import fr.harmonia.tsclientquery.answer.ErrorAnswer;

public class CommandNotFoundException extends QueryException {

	private static final long serialVersionUID = -8244205565535382065L;

	public CommandNotFoundException(ErrorAnswer errorAnswer) {
		super(errorAnswer);
	}

}
