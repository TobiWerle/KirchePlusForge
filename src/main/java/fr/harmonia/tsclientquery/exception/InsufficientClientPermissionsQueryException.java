package fr.harmonia.tsclientquery.exception;

import fr.harmonia.tsclientquery.answer.ErrorAnswer;

public class InsufficientClientPermissionsQueryException extends QueryException {

	private static final long serialVersionUID = -90002167226156843L;

	public InsufficientClientPermissionsQueryException(ErrorAnswer errorAnswer) {
		super(errorAnswer);
	}

	public int getFailedPermid() {
		return getErrorAnswer().getInteger("failed_permid");
	}

}
