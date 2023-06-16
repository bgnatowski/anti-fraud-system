package pl.bgnat.antifraudsystem.user;

import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.utils.IBANGenerator;

import java.time.LocalDateTime;

@Service
class AccountService {
	private AccountRepository accountRepository;
	public AccountService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	private Account createAccountForUser(User user){
		String alpha2Code = user.getAddress().getCountry().getCountryCode().getAlpha2Code();
		String newIban = IBANGenerator.generateIBAN(alpha2Code);
		Account newAccount = Account.builder()
				.owner(user)
				.balance(0d)
				.iban(newIban)
				.createDate(LocalDateTime.now())
				.isActive(false)
				.build();
		return accountRepository.save(newAccount);
	}
}
