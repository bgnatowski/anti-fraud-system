
package pl.bgnat.antifraudsystem.user;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.user.dto.AccountDTO;
import pl.bgnat.antifraudsystem.user.dto.CreditCardDTO;

import java.util.function.Function;

@Component
class CreditCardDTOMapper implements Function<CreditCard, CreditCardDTO> {
	private final UserDTOMapper userDTOMapper;

	CreditCardDTOMapper(UserDTOMapper userDTOMapper) {
		this.userDTOMapper = userDTOMapper;
	}

	@Override
	public CreditCardDTO apply(CreditCard creditCard) {
		return CreditCardDTO.builder()
				.id(creditCard.getId())
				.owner(userDTOMapper.apply(creditCard.getOwner()))
				.cardNumber(creditCard.getCardNumber())
				.build();
	}
}
