package pl.bgnat.antifraudsystem.domain.exceptions;

import pl.bgnat.antifraudsystem.exception.RequestValidationException;

public class CreditCardWithoutAccountException extends RequestValidationException {
	public CreditCardWithoutAccountException() {
		super(String.format("Credit card can't be assigned to user without account assigned"));
	}
}
