package pl.bgnat.antifraudsystem.transaction_security.stolenCards;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StolenCardFacade {
	private final StolenCardService stolenCardService;
	public StolenCardFacade(StolenCardService stolenCardService) {
		this.stolenCardService = stolenCardService;
	}
	public List<StolenCard> getAllStolenCards(){
		return this.stolenCardService.getAllStolenCards();
	}
}
