package pl.bgnat.antifraudsystem.dto;

import lombok.Builder;

@Builder
public record CreditCardDTO(
		Long id,
		UserDTO owner,
		AccountDTO accountDTO,
		String cardNumber
) {
}
