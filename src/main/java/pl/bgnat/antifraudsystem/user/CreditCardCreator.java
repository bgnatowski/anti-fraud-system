package pl.bgnat.antifraudsystem.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.user.enums.Country;
import pl.bgnat.antifraudsystem.utils.generator.CreditCardDataGenerator;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
class CreditCardCreator {
	private final Clock clock;
	public CreditCard createNewCreditCard(Country country) {
		String cardNumber = CreditCardDataGenerator.generateCreditCardNumber();
		String pin = CreditCardDataGenerator.generatePin();
		String cvv = CreditCardDataGenerator.generateCvv();
		LocalDateTime creationDate = LocalDateTime.now(clock);
		LocalDate expiration = LocalDate.of(
				creationDate.getYear()+3,
				creationDate.getMonth(),
				creationDate.getMonth().length(creationDate.toLocalDate().isLeapYear()));

		return CreditCard.builder()
				.createdAt(creationDate)
				.cardNumber(cardNumber)
				.pin(pin)
				.cvv(cvv)
				.expirationDate(expiration)
				.country(country)
				.validationAttempt(0)
				.isActive(false)
				.isBlocked(false)
				.build();
	}
}
