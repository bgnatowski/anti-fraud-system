package pl.bgnat.antifraudsystem.domain.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.domain.enums.Country;
import pl.bgnat.antifraudsystem.dto.AccountDTO;

@Component
@RequiredArgsConstructor
public class AccountFacade {
    private final AccountService accountService;

    public Account createAccount(Country country) {
        return accountService.createAccount(country);
    }

    public AccountDTO mapToDto(Account newAccount) {
        return accountService.mapToDto(newAccount);
    }

    public void deleteAccountForUser(String username) {
        accountService.deleteAccountForUser(username);
    }
}
