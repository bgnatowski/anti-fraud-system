package pl.bgnat.antifraudsystem.domain.stolencards;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.dto.response.StolenCardDeleteResponse;
import pl.bgnat.antifraudsystem.dto.request.StolenCardRequest;

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

	public String blacklist(String number){
		StolenCardRequest request = new StolenCardRequest(number);
		StolenCard stolenCard = stolenCardService.addStolenCard(request);
		return stolenCard.toString();
	}

	public String delete(String number){
		StolenCardDeleteResponse stolenCardDeleteResponse = stolenCardService.deleteStolenCardByNumber(number);
		return stolenCardDeleteResponse.status();
	}

}
