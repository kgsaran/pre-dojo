package br.com.amil.a5.model;

public class SendMessage extends NickName {
	private String message;
	private String messageTo;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessageTo() {
		return messageTo;
	}

	public void setMessageTo(String messageTo) {
		this.messageTo = messageTo;
	}
}
