package pl.bgnat.antifraudsystem.domain.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.domain.user.User;

@Component
@RequiredArgsConstructor
public class AccountFacade {
    private final AccountService accountService;

    public Account createAccount(User user) {
        return accountService.createAccount(user);
    }

    public AccountDTO mapToDto(Account newAccount) {
        return accountService.mapToDto(newAccount);
    }

    public void deleteAccountForUser(String username) {
        accountService.deleteAccountForUser(username);
    }
}
