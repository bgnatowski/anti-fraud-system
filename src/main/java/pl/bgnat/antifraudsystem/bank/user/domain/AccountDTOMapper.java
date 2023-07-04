
package pl.bgnat.antifraudsystem.bank.user.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.bank.user.dto.AccountDTO;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
class AccountDTOMapper implements Function<Account, AccountDTO> {
	private final UserDTOMapper userDTOMapper;

	@Override
	public AccountDTO apply(Account account) {
		if(account==null) return null;
		return AccountDTO.builder()
				.id(account.getId())
				.owner(userDTOMapper.apply(account.getOwner()))
				.balance(account.getBalance())
				.isActive(account.isActive())
				.createDate(account.getCreateDate())
				.iban(account.getIban())
				.build();
	}
}
