package pl.bgnat.antifraudsystem.user.dto;

import lombok.Builder;
import pl.bgnat.antifraudsystem.user.enums.Country;

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
