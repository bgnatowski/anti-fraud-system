package pl.bgnat.antifraudsystem.transaction.stolenCards.exceptions;

import pl.bgnat.antifraudsystem.exception.ResourceNotFoundException;

public class StolenCardNotFound extends ResourceNotFoundException {
	public static final String THERE_IS_NO_STOLEN_CARD_WITH_NUMBER_S_IN_DATABASE = "There is no stolen card with number: %s in database.";
	public StolenCardNotFound(String number) {
		super(String.format(THERE_IS_NO_STOLEN_CARD_WITH_NUMBER_S_IN_DATABASE, number));
	}
}
