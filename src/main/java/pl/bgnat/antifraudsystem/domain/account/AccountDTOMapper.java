
package pl.bgnat.antifraudsystem.domain.account;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.bgnat.antifraudsystem.utils.Mapper;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
class AccountDTOMapper implements Mapper<Account, AccountDTO> {
    private static Mapper<Account, AccountDTO> instance;

    static Mapper<Account, AccountDTO> getMapper() {
        if (instance == null) {
            instance = new AccountDTOMapper();
        }
        return instance;
    }

    @Override
    public AccountDTO apply(Account account) {
        if (account == null) return null;

        AccountDTO.AccountDTOBuilder builder = AccountDTO.builder()
                .id(account.getId())
                .balance(account.getBalance())
                .isActive(account.isActive())
                .createDate(account.getCreateDate())
                .country(account.getCountry())
                .iban(account.getIban());

        if (account.getOwner() != null)
            builder.ownerId(account.getOwner().getId());

        return builder.build();
    }

    @Override
    public AccountDTO map(Account model) {
        return apply(model);
    }
}
