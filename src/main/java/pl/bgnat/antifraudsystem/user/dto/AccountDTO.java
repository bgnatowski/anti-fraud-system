package pl.bgnat.antifraudsystem.user.dto;

import lombok.Builder;

@Builder
public record AccountDTO(Long id,
						 UserDTO owner,
						 String iban) {
}
