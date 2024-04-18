package pl.bgnat.antifraudsystem.domain.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.domain.enums.Country;
import pl.bgnat.antifraudsystem.domain.user.User;
import pl.bgnat.antifraudsystem.exception.RequestValidationException;
import pl.bgnat.antifraudsystem.utils.date.DateTimeUtils;

import java.util.Random;

@Service
@RequiredArgsConstructor
class AccountService {
    private final AccountRepository accountRepository;

    Account createAccount(User user) {
        if (user.getAddress() == null)
            throw new RequestValidationException(
                    "Cannot create account without address details. Create address first here: http://localhost:1102/api/auth/user/%s/address/create"
                            .formatted(user.getUsername()));

        Country country = user.getAddress().getCountry();
        String alpha2Code = country.getIbanCountryCode().getAlpha2Code();

        String newIban;
        do {
            newIban = generateIBAN(alpha2Code);
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

    AccountDTO mapToDto(Account account) {
        return AccountDTO.MAPPER.map(account);
    }

    private String generateIBAN(String countryCode) {
        StringBuilder ibanBuilder = new StringBuilder();
        // Add the country code and checksum digits
        ibanBuilder.append(countryCode);
        ibanBuilder.append("00");

        // Generate the rest of the digits randomly
        Random random = new Random();
        for (int i = 0; i < 14; i++) {
            int digit = random.nextInt(10);
            ibanBuilder.append(digit);
        }
        return ibanBuilder.toString();
    }
}
