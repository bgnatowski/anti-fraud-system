package pl.bgnat.antifraudsystem.domain.account;

import lombok.Builder;
import pl.bgnat.antifraudsystem.domain.enums.Country;
import pl.bgnat.antifraudsystem.domain.user.User;
import pl.bgnat.antifraudsystem.domain.user.UserDTO;
import pl.bgnat.antifraudsystem.utils.Mapper;

import java.time.LocalDateTime;

@Builder
public record AccountDTO(Long id,
						 Long ownerId,
						 String iban,
						 Double balance,
						 Country country,
						 boolean isActive,
						 LocalDateTime createDate
						 ) {
	public static Mapper<Account, AccountDTO> MAPPER = AccountDTOMapper.getMapper();
}
