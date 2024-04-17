package pl.bgnat.antifraudsystem.domain.account;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
interface AccountRepository extends JpaRepository<Account, Long> {
	boolean existsAccountByIban(String iban);
	@Transactional
	@Modifying
	void deleteAccountByOwnerUsername(String owner_username);
}
