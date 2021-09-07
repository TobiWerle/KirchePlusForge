package fr.harmonia.tsclientquery.exception;

import fr.harmonia.tsclientquery.answer.RequireRegisterAnswer;

public class UnRegisterQueryException extends QueryException {
	private static final long serialVersionUID = 8169273869835289924L;

	public UnRegisterQueryException(RequireRegisterAnswer errorAnswer) {
		super(errorAnswer);
	}

}
