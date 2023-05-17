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

	public String add(String number){
		StolenCardRequest request = new StolenCardRequest(number);
		StolenCard stolenCard = stolenCardService.addStolenCard(request);
		return stolenCard.toString();
	}

	public String delete(String number){
		StolenCardDeleteResponse stolenCardDeleteResponse = stolenCardService.deleteStolenCardByNumber(number);
		return stolenCardDeleteResponse.status();
	}

}
