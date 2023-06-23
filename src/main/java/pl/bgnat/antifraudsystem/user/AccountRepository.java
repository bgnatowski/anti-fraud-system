package pl.bgnat.antifraudsystem.user;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
interface AccountRepository extends JpaRepository<Account, Long> {
	boolean existsAccountByIban(String iban);
}
