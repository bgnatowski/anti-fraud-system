package pl.bgnat.antifraudsystem.domain.cards.creditcard;

import pl.bgnat.antifraudsystem.domain.enums.Country;
import pl.bgnat.antifraudsystem.utils.date.DateTimeUtils;
import pl.bgnat.antifraudsystem.domain.cards.CardDataGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;

class CreditCardCreator {
    public static CreditCard createNewCreditCardForCountry(Country country) {
        String cardNumber = CardDataGenerator.generateCreditCardNumber();
        String pin = CardDataGenerator.generatePin();
        String cvv = CardDataGenerator.generateCvv();
        LocalDateTime creationDate = DateTimeUtils.currentLocalDateTime();
        LocalDate expiration = LocalDate.of(
                creationDate.getYear() + 3,
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
