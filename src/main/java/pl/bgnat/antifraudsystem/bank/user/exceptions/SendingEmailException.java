package pl.bgnat.antifraudsystem.bank.user.exceptions;

public class SendingEmailException extends RuntimeException {
	public SendingEmailException(String message) {
		super(message);
	}
}
