package pl.bgnat.antifraudsystem.domain.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.dto.AccountDTO;
import pl.bgnat.antifraudsystem.domain.enums.Country;
import pl.bgnat.antifraudsystem.utils.date.DateTimeUtils;
import pl.bgnat.antifraudsystem.utils.generator.IBANGenerator;

@Service
@RequiredArgsConstructor
class AccountService {
	private final AccountRepository accountRepository;
	private final AccountDTOMapper accountDTOMapper;

	Account createAccount(Country country) {
		String alpha2Code = country.getIbanCountryCode().getAlpha2Code();

		String newIban;
		do {
			newIban = IBANGenerator.generateIBAN(alpha2Code);
		} while (accountRepository.existsAccountByIban(newIban));

		Account newAccount = Account.builder()
				.balance(0d)
				.iban(newIban)
				.createDate(DateTimeUtils.currentLocalDateTime())
				.country(country)
				.isActive(true)
				.build();

		return accountRepository.save(newAccount);
	}

	void deleteAccountForUser(String username) {
		accountRepository.deleteAccountByOwnerUsername(username);
	}

	AccountDTO mapToDto(Account account){
		return accountDTOMapper.apply(account);
	}
}
