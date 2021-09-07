package fr.harmonia.tsclientquery.query;

/*
 * help sendtextmessage Usage: sendtextmessage targetmode={1-3}
 * [target=clientID] msg={text}
 * 
 * Sends a text message a specified target. The type of the target is determined
 * by targetmode. Available targetmodes are: 1: Send private text message to a
 * client. You must specify the target parameter 2: Send message to the channel
 * you are currently in. Target is ignored. 3: Send message to the entire
 * server. Target is ignored.
 * 
 * Example: sendtextmessage targetmode=2 msg=Hello\sChannel error id=0 msg=ok
 * 
 * 
 * error id=0 msg=ok
 */

public class SendTextMessageQuery extends NoAnswerQuery {

	public enum TargetMode {
		CHANNEL(2), SERVER(3);

		public final int id;

		private TargetMode(int id) {
			this.id = id;
		}
	}

	public SendTextMessageQuery(int clientID, String msg) {
		super("sendtextmessage");
		addArgument("targetmode", 1);
		addArgument("target", clientID);
		addArgument("msg", msg);
	}

	public SendTextMessageQuery(TargetMode target, String msg) {
		super("sendtextmessage");
		addArgument("targetmode", target.id);
		addArgument("msg", msg);
	}

}
