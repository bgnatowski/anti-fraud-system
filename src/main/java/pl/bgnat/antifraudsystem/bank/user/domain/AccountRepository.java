package pl.bgnat.antifraudsystem.bank.user.domain;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
interface AccountRepository extends JpaRepository<Account, Long> {
	boolean existsAccountByIban(String iban);
}
