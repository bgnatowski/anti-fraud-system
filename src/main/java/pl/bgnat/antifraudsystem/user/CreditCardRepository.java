package pl.bgnat.antifraudsystem.user;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
interface CreditCardRepository extends JpaRepository<CreditCard, Long> {
	Optional<CreditCard> findCreditCardByCardNumber(String cardNumber);

	void deleteAllByAccount_Iban(String account_iban);
	@Transactional
	@Modifying
	void deleteAllByAccountOwnerUsername(String account_owner_username);
}
