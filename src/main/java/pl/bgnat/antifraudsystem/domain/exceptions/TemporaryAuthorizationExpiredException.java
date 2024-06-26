package pl.bgnat.antifraudsystem.domain.exceptions;

public class TemporaryAuthorizationExpiredException extends RuntimeException {
	public TemporaryAuthorizationExpiredException(String username) {
		super(String.format(
				"Verification code is expired. " +
						"If you want to confirm user email generate new code on: " +
						"http://localhost:1102/api/auth/user/%s/regenerate-activation-code", username
		));
	}
}
