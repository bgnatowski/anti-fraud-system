package pl.bgnat.antifraudsystem.user;

import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.user.dto.UserDTO;
import pl.bgnat.antifraudsystem.user.exceptions.IllegalCountryException;
import pl.bgnat.antifraudsystem.utils.generator.IBANGenerator;
import pl.bgnat.antifraudsystem.user.enums.IBANCountryCode;

import java.time.LocalDateTime;

@Service
class AccountService {
	private AccountRepository accountRepository;
	public AccountService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}
	Account createAccount(UserDTO user){
		try{
			String alpha2Code = IBANCountryCode.of(user.address().country()).getAlpha2Code();

			String newIban;
			do {
				newIban = IBANGenerator.generateIBAN(alpha2Code);
			}while (accountRepository.existsAccountByIban(newIban));

			Account newAccount = Account.builder()
					.balance(0d)
					.iban(newIban)
					.createDate(LocalDateTime.now())
					.isActive(false)
					.build();
			return accountRepository.save(newAccount);
		}catch (NullPointerException | IllegalArgumentException e){
			throw new IllegalCountryException(user.address().country());
		}
	}
}
