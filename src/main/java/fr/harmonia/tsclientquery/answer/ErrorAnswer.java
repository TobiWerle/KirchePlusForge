package fr.harmonia.tsclientquery.answer;

/*
 * error id=0 msg=ok
 */
public class ErrorAnswer extends OpenAnswer {

	public ErrorAnswer(String line) {
		super(line);
	}

	public int getId() {
		return getInteger("id");
	}

	public String getMsg() {
		return get("msg");
	}

}
