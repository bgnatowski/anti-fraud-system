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

	private CreditCard createCardForUser(User user){
		String cardNumber = CardNumberGenerator.generateCreditCardNumber();
		CreditCard newCreditCard = CreditCard.builder()
				.createdAt(LocalDateTime.now())
				.cardNumber(cardNumber)
				.account(user.getAccount())
				.isActive(false)
				.build();
		return creditCardRepository.save(newCreditCard);
	}
}
