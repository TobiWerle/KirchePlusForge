package fr.harmonia.tsclientquery.exception;

import fr.harmonia.tsclientquery.answer.ErrorAnswer;

public class QueryException extends RuntimeException {
	/**
	 * error id=771 msg=channel\sname\sis\salready\sin\suse
	 */
	public static final int ERROR_ID_CHANNEL_NAME_IS_ALREADY_IN_USE = 771;
	/**
	 * error id=256 msg=command\snot\sfound
	 */
	public static final int ERROR_ID_COMMAND_NOT_FOUND = 256;
	/**
	 * error id=1796 msg=currently\snot\spossible
	 */
	public static final int ERROR_ID_CURRENTLY_NOT_POSSIBLE = 1796;
	/**
	 * error id=1281 msg=database\sempty\sresult\sset
	 */
	public static final int ERROR_ID_DATABASE_EMPTY_RESULT_SE = 1281;
	/**
	 * error id=2568 msg=insufficient\sclient\spermissions failed_permid=38
	 */
	public static final int ERROR_ID_INSUFFICIENT_CLIENT_PERMISSIONS = 2568;
	/**
	 * error id=3328 msg=invalid\sban\sid
	 */
	public static final int ERROR_ID_INVALID_BAN_ID = 3328;
	/**
	 * error id=781 msg=invalid\schannel\spassword
	 */
	public static final int ERROR_ID_INVALID_CHANNEL_PASSWORD = 781;
	/**
	 * error id=1538 msg=invalid\sparameter
	 */
	public static final int ERROR_ID_INVALID_PARAMETER = 1538;
	/**
	 * error id=2562 msg=invalid\spermission\sID
	 */
	public static final int ERROR_ID_INVALID_PERMISSION_ID = 2562;
	/**
	 * error id=1799 msg=invalid\sserver\sconnection\shandler\sID
	 */
	public static final int ERROR_ID_INVALID_SERVER_CONNECTION_SHANDLER = 1799;
	/**
	 * error id=1028 msg=invalid\sserver\spassword
	 */
	public static final int ERROR_ID_INVALID_SERVER_PASSWORD = 1028;
	/**
	 * error id=1542 msg=missing\srequired\sparameter
	 */
	public static final int ERROR_ID_MISSING_REQUIRED_PARAMETER = 1542;
	/**
	 * error id=1794 msg=not\sconnected
	 */
	public static final int ERROR_ID_NOT_CONNECTED = 1794;
	/**
	 * error id=0 msg=ok
	 */
	public static final int ERROR_ID_OK = 0;
	/**
	 * error id=1539 msg=parameter\snot\sfound
	 */
	public static final int ERROR_ID_PARAMETER_NOT_FOUND = 1539;
	/**
	 * error id=768 msg=invalid\schannelID
	 */
	public static final int ERROR_IDINVALID_CHANNEL_ID = 768;

	private static final long serialVersionUID = 1L;

	private ErrorAnswer errorAnswer;

	public QueryException(ErrorAnswer errorAnswer) {
		super(errorAnswer.getMsg());
		this.errorAnswer = errorAnswer;
	}

	public ErrorAnswer getErrorAnswer() {
		return errorAnswer;
	}

}
