package pl.bgnat.antifraudsystem.transaction_security.suspiciousIP;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface SuspiciousIPRepository extends JpaRepository<SuspiciousIP, Long> {
	boolean existsByIpAddress(String ipAddress);

	void deleteByIpAddress(String ipAddress);
}
