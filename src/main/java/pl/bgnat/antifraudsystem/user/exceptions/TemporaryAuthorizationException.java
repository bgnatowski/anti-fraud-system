package pl.bgnat.antifraudsystem.user.exceptions;

public class TemporaryAuthorizationException extends RuntimeException {
	public TemporaryAuthorizationException() {
		super("Verification code is expired. If you want to confirm user email generate new code on ....");
	}
}
