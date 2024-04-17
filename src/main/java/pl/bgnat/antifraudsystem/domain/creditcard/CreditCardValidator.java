package pl.bgnat.antifraudsystem.domain.creditcard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.exception.RequestValidationException;

@Component
@RequiredArgsConstructor
class CreditCardValidator {
	void accessValidation(CreditCard creditCard, String pin) {
		if(creditCard.isBlocked())
			throw new RequestValidationException("Card is blocked. Contact with bank");
		if(creditCard.getPin() != pin){
			creditCard.increaseValidationAttempt();
			int validationAttempt = creditCard.getValidationAttempt();
			throw new RequestValidationException(String.format("[Security] Wrong pin! You have %d more attempt.", (3-validationAttempt)));
		}
	}

	void activationValidation(CreditCard card, String activationPin) {
		if(card.isActive())
			throw new RequestValidationException("Card is already active!");
		if(card.getPin() != activationPin){
			card.increaseValidationAttempt();
			throw new RequestValidationException("[Security] Wrong pin!");
		}
	}

	void changingPinValidation(CreditCard card, String newPin) {
		if(!card.isActive())
			throw new RequestValidationException("Card is inactive. Contact with bank");
		if(card.getPin() == newPin)
			throw new RequestValidationException("Pins are same");
	}
}
