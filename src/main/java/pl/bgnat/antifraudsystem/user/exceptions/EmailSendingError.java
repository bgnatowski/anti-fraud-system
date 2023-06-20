package pl.bgnat.antifraudsystem.user.exceptions;

public class EmailSendingError extends RuntimeException {
	public EmailSendingError(String message) {
		super(message);
	}
}
