package pl.bgnat.antifraudsystem.domain.exceptions;


import pl.bgnat.antifraudsystem.exception.ResourceNotFoundException;

public class CreditCardNotFoundException extends ResourceNotFoundException {
	public static final String THERE_IS_NO_CREDIT_CARD_WITH_NUMBER_S = "There is no credit card with number = %s";
	public CreditCardNotFoundException(String number) {
		super(String.format(THERE_IS_NO_CREDIT_CARD_WITH_NUMBER_S, number));
	}
}
