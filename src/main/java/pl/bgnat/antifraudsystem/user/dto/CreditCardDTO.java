package pl.bgnat.antifraudsystem.user.dto;

import lombok.Builder;

@Builder
public record CreditCardDTO(
		Long id,
		UserDTO owner,
		AccountDTO accountDTO,
		String cardNumber
) {
}
