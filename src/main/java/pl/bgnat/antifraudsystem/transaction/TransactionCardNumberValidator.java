package pl.bgnat.antifraudsystem.transaction;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.exception.stolenCard.CardNumberFormatException;
import pl.bgnat.antifraudsystem.transaction_security.stolenCards.StolenCardFacade;

import java.util.List;

@Component
public class TransactionCardNumberValidator extends Validator{
	private static final int CHECKSUM_DIGIT_POSITION = 15;
	private static final int CARD_NUMBER_LENGTH = 16;

	private final StolenCardFacade stolenCardFacade;

	public TransactionCardNumberValidator(StolenCardFacade facade) {
		this.stolenCardFacade = facade;
	}

	@Override
	public List<String> isValid(TransactionRequest request, List<String> info) {
		String cardNumber = request.number();
		if(!isValid(cardNumber))
			throw new CardNumberFormatException(cardNumber);
		if (stolenCardFacade.isBlacklisted(cardNumber))
			info.add("card-number");
		return nextValidation(request, info);
	}

	private boolean isValid(String number) {
		if (number.length() < CARD_NUMBER_LENGTH) return false;

		boolean isOddPosition = true;
		int sum = 0;
		int checkSum = Integer.parseInt(Character.toString((number.charAt(CHECKSUM_DIGIT_POSITION))));

		for(int i = 0; i < number.length()-1; i++){
			int digit = Integer.parseInt(Character.toString((number.charAt(i))));
			int multipliedOddPositionDigitBy2 = (isOddPosition ? 2 : 1) * digit;
			int substracted9FromOver9Number = multipliedOddPositionDigitBy2 - (multipliedOddPositionDigitBy2 > 9 ? 9 : 0);
			sum += substracted9FromOver9Number;
			isOddPosition = !isOddPosition;
		}
		return (sum+checkSum) % 10 == 0;
	}

}