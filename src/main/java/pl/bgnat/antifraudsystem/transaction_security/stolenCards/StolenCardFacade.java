package pl.bgnat.antifraudsystem.transaction_security.stolenCards;

import org.springframework.stereotype.Component;


@Component
public class StolenCardFacade {
	private final StolenCardService stolenCardService;
	public StolenCardFacade(StolenCardService stolenCardService) {
		this.stolenCardService = stolenCardService;
	}
	public boolean isValid(String cardNumber) {
		return stolenCardService.isValidCardNumber(cardNumber);
	}
	public boolean isBlacklisted(String number){
		return this.stolenCardService.isBlacklisted(number);
	}

}
