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
		String newIban = IBANGenerator.generateIBAN(user.getAddress().getCountryCode());
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
