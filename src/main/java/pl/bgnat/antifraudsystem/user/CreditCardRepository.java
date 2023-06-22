package pl.bgnat.antifraudsystem.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface CreditCardRepository extends JpaRepository<CreditCard, Long> {
	Optional<CreditCard> findCreditCardByCardNumber(String cardNumber);
}
