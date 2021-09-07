package fr.harmonia.tsclientquery.exception;

import fr.harmonia.tsclientquery.answer.ErrorAnswer;

public class CurrentlyNotPossibleQueryException extends QueryException {
	private static final long serialVersionUID = 6380079712013517447L;

	public CurrentlyNotPossibleQueryException(ErrorAnswer errorAnswer) {
		super(errorAnswer);
	}

}
