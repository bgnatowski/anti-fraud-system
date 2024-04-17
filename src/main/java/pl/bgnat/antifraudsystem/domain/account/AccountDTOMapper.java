
package pl.bgnat.antifraudsystem.domain.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.dto.AccountDTO;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
class AccountDTOMapper implements Function<Account, AccountDTO> {
	@Override
	public AccountDTO apply(Account account) {
		if(account==null) return null;
		return AccountDTO.builder()
				.id(account.getId())
				.balance(account.getBalance())
				.isActive(account.isActive())
				.createDate(account.getCreateDate())
				.iban(account.getIban())
				.build();
	}
}