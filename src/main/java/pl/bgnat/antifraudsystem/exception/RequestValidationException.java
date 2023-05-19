package pl.bgnat.antifraudsystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class RequestValidationException extends RuntimeException {
	public static final String WRONG_JSON_FORMAT = "Wrong json format";
	public static final String INVALID_REQUEST = "Invalid request form";
	public RequestValidationException(String message) {
		super(message);
	}
}
