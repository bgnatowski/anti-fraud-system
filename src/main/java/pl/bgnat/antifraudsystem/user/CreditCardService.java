package pl.bgnat.antifraudsystem.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.exception.RequestValidationException;
import pl.bgnat.antifraudsystem.user.dto.CreditCardDTO;
import pl.bgnat.antifraudsystem.user.enums.Country;
import pl.bgnat.antifraudsystem.user.exceptions.CreditCardNotFoundException;
import pl.bgnat.antifraudsystem.utils.generator.CreditCardDataGenerator;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
class CreditCardService {
	private final CreditCardRepository creditCardRepository;
	private final CreditCardDTOMapper creditCardDTOMapper;
	private final Clock clock;
	CreditCard createCreditCard(Country country){
		String cardNumber = CreditCardDataGenerator.generateCreditCardNumber();
		String pin = CreditCardDataGenerator.generatePin();
		String cvv = CreditCardDataGenerator.generateCvv();
		LocalDateTime creationDate = LocalDateTime.now(clock);

		LocalDate expiration = LocalDate.of(
				creationDate.getYear()+3,
				creationDate.getMonth(),
				creationDate.getMonth().length(creationDate.toLocalDate().isLeapYear()));

		CreditCard newCreditCard = CreditCard.builder()
				.createdAt(creationDate)
				.cardNumber(cardNumber)
				.pin(pin)
				.cvv(cvv)
				.expirationDate(expiration)
				.country(country)
				.isActive(false)
				.isBlocked(false)
				.build();
		return creditCardRepository.save(newCreditCard);
	}
	void activeCreditCard(String cardNumber, String pin){
		CreditCard cardToActive = creditCardRepository.findCreditCardByCardNumber(cardNumber).orElseThrow(() -> new CreditCardNotFoundException(cardNumber));
		if(cardToActive.isActive())
			throw new RequestValidationException("Card is already active!");
		if(cardToActive.getPin() != pin)
			throw new RequestValidationException("[Security] Wrong pin!");


		String customPin = "1234"; // get new pin from user

		cardToActive.setActive(true);
		cardToActive.setPin(customPin);

		creditCardRepository.save(cardToActive);
	}

	void deleteCreditCardsFromAccountByUsername(String username){
		creditCardRepository.deleteAllByAccountOwnerUsername(username);
	}

	void changePin(String cardNumber,String newPin){
		CreditCard cardToChangePin = creditCardRepository.findCreditCardByCardNumber(cardNumber).orElseThrow(() -> new CreditCardNotFoundException(cardNumber));
		if(!cardToChangePin.isActive())
			throw new RequestValidationException("Card is inactive. Contact with bank");
		if(cardToChangePin.isBlocked())
			throw new RequestValidationException("Card is blocked. Contact with bank");
		if(cardToChangePin.getPin() == newPin)
			throw new RequestValidationException("Pins are same");

		cardToChangePin.setPin(newPin);
	}

	CreditCardDTO mapToDto(CreditCard creditCard){
		return creditCardDTOMapper.apply(creditCard);
	}


}
