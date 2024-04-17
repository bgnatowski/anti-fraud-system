package pl.bgnat.antifraudsystem.bank.transaction.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.bgnat.antifraudsystem.bank.transaction.dto.TransactionRegion;

import java.time.LocalDateTime;

@Repository
interface TransactionRepository extends JpaRepository<Transaction, Long> {
	@Query("SELECT COUNT(DISTINCT t.region) " +
			"FROM Transaction t " +
			"WHERE t.region <> ?1 AND t.cardNumber = ?2 " +
			"AND t.date BETWEEN ?3 AND ?4")
	int countTransactionsWithDistinctRegion(TransactionRegion excludedRegion, String cardNumber, LocalDateTime startDate, LocalDateTime endDate);

	@Query("SELECT COUNT(DISTINCT t.ipAddress) " +
			"FROM Transaction t " +
			"WHERE t.ipAddress <> ?1 AND t.cardNumber = ?2 " +
			"AND t.date BETWEEN ?3 AND ?4")
	int countTransactionWithDistinctIp(String excludedIp, String cardNumber, LocalDateTime startDate, LocalDateTime endDate);
}
