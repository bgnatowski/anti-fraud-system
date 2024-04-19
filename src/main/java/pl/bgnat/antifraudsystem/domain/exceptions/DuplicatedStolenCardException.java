package pl.bgnat.antifraudsystem.domain.exceptions;

import pl.bgnat.antifraudsystem.exception.DuplicateResourceException;

public class DuplicatedStolenCardException extends DuplicateResourceException {
	public static final String STOLEN_CARD_WITH_NUMBER_S_IS_ALREADY_SUSPICIOUS = "Stolen card with number: %s is already suspicious";

	public DuplicatedStolenCardException(String number) {
		super(String.format(STOLEN_CARD_WITH_NUMBER_S_IS_ALREADY_SUSPICIOUS, number));
	}
}
