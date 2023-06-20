
package pl.bgnat.antifraudsystem.user;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.user.dto.AccountDTO;

import java.util.function.Function;

@Component
class AccountDTOMapper implements Function<Account, AccountDTO> {
	private final UserDTOMapper userDTOMapper;

	AccountDTOMapper(UserDTOMapper userDTOMapper) {
		this.userDTOMapper = userDTOMapper;
	}

	@Override
	public AccountDTO apply(Account account) {
		return AccountDTO.builder()
				.id(account.getId())
				.owner(userDTOMapper.apply(account.getOwner()))
				.iban(account.getIban())
				.build();
	}
}
