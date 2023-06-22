package pl.bgnat.antifraudsystem.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.user.dto.AccountDTO;
import pl.bgnat.antifraudsystem.user.dto.TemporaryAuthorizationDTO;
import pl.bgnat.antifraudsystem.user.dto.UserDTO;
import pl.bgnat.antifraudsystem.user.enums.Country;
import pl.bgnat.antifraudsystem.user.enums.IBANCountryCode;
import pl.bgnat.antifraudsystem.utils.generator.IBANGenerator;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
class AccountService {
	private final AccountRepository accountRepository;
	private final AccountDTOMapper accountDTOMapper;
	private final Clock clock;

	Account createAccount(Country country) {
		String alpha2Code = country.getIbanCountryCode().getAlpha2Code();

		String newIban;
		do {
			newIban = IBANGenerator.generateIBAN(alpha2Code);
		} while (accountRepository.existsAccountByIban(newIban));

		Account newAccount = Account.builder()
				.balance(0d)
				.iban(newIban)
				.createDate(LocalDateTime.now(clock))
				.country(country)
				.isActive(true)
				.build();

		return accountRepository.save(newAccount);
	}

	AccountDTO mapToDto(Account account){
		return accountDTOMapper.apply(account);
	}




}
