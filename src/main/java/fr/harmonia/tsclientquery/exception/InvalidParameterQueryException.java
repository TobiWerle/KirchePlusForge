package fr.harmonia.tsclientquery.exception;

import fr.harmonia.tsclientquery.answer.ErrorAnswer;

public class InvalidParameterQueryException extends QueryException {

	private static final long serialVersionUID = 4207676137772701094L;

	public InvalidParameterQueryException(ErrorAnswer errorAnswer) {
		super(errorAnswer);
	}

}
