
package pl.bgnat.antifraudsystem.bank.user.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.bank.user.dto.CreditCardDTO;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
class CreditCardDTOMapper implements Function<CreditCard, CreditCardDTO> {
	@Override
	public CreditCardDTO apply(CreditCard creditCard) {
		if(creditCard==null) return null;
		return CreditCardDTO.builder()
				.id(creditCard.getId())
				.cardNumber(creditCard.getCardNumber())
				.build();
	}
}
