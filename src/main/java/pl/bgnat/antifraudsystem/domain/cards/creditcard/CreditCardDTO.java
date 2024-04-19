package pl.bgnat.antifraudsystem.domain.cards.creditcard;

import lombok.Builder;
import pl.bgnat.antifraudsystem.utils.Mapper;

@Builder
public record CreditCardDTO(
        Long id,
        Long ownerId,
        Long accountId,
        String cardNumber,
        Boolean active
) {
    public static Mapper<CreditCard, CreditCardDTO> MAPPER = CreditCardDTOMapper.getMapper();
}
