package pl.bgnat.antifraudsystem.user.exceptions;

public class TemporaryAuthorizationExpiredException extends RuntimeException {
	public TemporaryAuthorizationExpiredException(String username) {
		super(String.format(
				"Verification code is expired. " +
						"If you want to confirm user email generate new code on: " +
						"http://localhost:4200/user/%s/auth/generatecode", username
		));
	}
}
