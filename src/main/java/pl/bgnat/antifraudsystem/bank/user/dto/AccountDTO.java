package pl.bgnat.antifraudsystem.bank.user.dto;

import lombok.Builder;
import pl.bgnat.antifraudsystem.bank.user.enums.Country;

import java.time.LocalDateTime;

@Builder
public record AccountDTO(Long id,
						 UserDTO owner,
						 String iban,
						 Double balance,
						 Country country,
						 boolean isActive,
						 LocalDateTime createDate
						 ) {
}
