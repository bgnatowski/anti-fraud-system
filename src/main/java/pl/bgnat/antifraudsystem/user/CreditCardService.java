package pl.bgnat.antifraudsystem.user;

import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.utils.CardNumberGenerator;

import java.time.LocalDateTime;

@Service
class CreditCardService {
	private CreditCardRepository creditCardRepository;

	CreditCardService(CreditCardRepository creditCardRepository) {
		this.creditCardRepository = creditCardRepository;
	}

	CreditCard createCreditCard(){
		String cardNumber = CardNumberGenerator.generateCreditCardNumber();
		CreditCard newCreditCard = CreditCard.builder()
				.createdAt(LocalDateTime.now())
				.cardNumber(cardNumber)
				.isActive(false)
				.build();
		return creditCardRepository.save(newCreditCard);
	}
}
