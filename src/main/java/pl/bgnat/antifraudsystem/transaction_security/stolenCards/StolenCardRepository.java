package pl.bgnat.antifraudsystem.transaction_security.stolenCards;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StolenCardRepository extends JpaRepository<StolenCard, Long> {
	void deleteByNumber(String number);
	boolean existsByNumber(String number);
}
