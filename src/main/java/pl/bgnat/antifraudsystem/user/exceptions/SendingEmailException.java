package pl.bgnat.antifraudsystem.user.exceptions;

public class SendingEmailException extends RuntimeException {
	public SendingEmailException(String message) {
		super(message);
	}
}
