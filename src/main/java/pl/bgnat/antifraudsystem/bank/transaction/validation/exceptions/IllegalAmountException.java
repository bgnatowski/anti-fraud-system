package pl.bgnat.antifraudsystem.bank.transaction.validation.exceptions;

import pl.bgnat.antifraudsystem.exception.RequestValidationException;

public class IllegalAmountException extends RequestValidationException {
	public static final String WRONG_REQUEST_AMOUNT_HAVE_TO_BE_POSITIVE_NUMBER_PASSED_D = "Wrong request! Amount have to be positive number! Passed: %d";

	public IllegalAmountException(Long passedAmount) {
		super(String.format(WRONG_REQUEST_AMOUNT_HAVE_TO_BE_POSITIVE_NUMBER_PASSED_D, passedAmount));
	}
}
