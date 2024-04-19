package pl.bgnat.antifraudsystem.domain.cards.creditcard;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
interface CreditCardRepository extends JpaRepository<CreditCard, Long> {
	Optional<CreditCard> findCreditCardByCardNumber(String cardNumber);
	@Transactional
	@Modifying
	void deleteAllByAccountOwnerUsername(String account_owner_username);
}
