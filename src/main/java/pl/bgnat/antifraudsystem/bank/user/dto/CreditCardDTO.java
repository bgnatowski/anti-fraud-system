package pl.bgnat.antifraudsystem.bank.user.dto;

import lombok.Builder;

@Builder
public record CreditCardDTO(
		Long id,
		UserDTO owner,
		AccountDTO accountDTO,
		String cardNumber
) {
}
