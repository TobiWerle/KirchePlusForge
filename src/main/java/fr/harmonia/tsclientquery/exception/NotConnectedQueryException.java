package fr.harmonia.tsclientquery.exception;

import fr.harmonia.tsclientquery.answer.ErrorAnswer;

public class NotConnectedQueryException extends QueryException {

	private static final long serialVersionUID = 6281503051890091052L;

	public NotConnectedQueryException(ErrorAnswer errorAnswer) {
		super(errorAnswer);
	}

}
