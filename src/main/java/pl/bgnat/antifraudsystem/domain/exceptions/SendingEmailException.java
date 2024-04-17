package pl.bgnat.antifraudsystem.domain.exceptions;

public class SendingEmailException extends RuntimeException {
	public SendingEmailException(String message) {
		super(message);
	}
}
