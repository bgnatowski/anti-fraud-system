
package pl.bgnat.antifraudsystem.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.user.dto.CreditCardDTO;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
class CreditCardDTOMapper implements Function<CreditCard, CreditCardDTO> {
	private final UserDTOMapper userDTOMapper;
	private final AccountDTOMapper accountDTOMapper;

	@Override
	public CreditCardDTO apply(CreditCard creditCard) {
		if(creditCard==null) return null;
		return CreditCardDTO.builder()
				.id(creditCard.getId())
				.owner(userDTOMapper.apply(creditCard.getAccount().getOwner()))
				.accountDTO(accountDTOMapper.apply(creditCard.getAccount()))
				.cardNumber(creditCard.getCardNumber())
				.build();
	}
}
