package pl.bgnat.antifraudsystem.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.user.dto.UserDTO;
import pl.bgnat.antifraudsystem.user.exceptions.IllegalCountryException;
import pl.bgnat.antifraudsystem.utils.generator.IBANGenerator;
import pl.bgnat.antifraudsystem.user.enums.IBANCountryCode;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
class AccountService {
	private final AccountRepository accountRepository;
	private final Clock clock;
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
					.createDate(LocalDateTime.now(clock))
					.isActive(false)
					.build();

			return accountRepository.save(newAccount);
		}catch (NullPointerException | IllegalArgumentException e){
			throw new IllegalCountryException(user.address().country());
		}
	}
}
