package pl.bgnat.antifraudsystem.bank.transaction.suspiciousIP;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
interface SuspiciousIPRepository extends JpaRepository<SuspiciousIP, Long> {
	boolean existsByIp(String ipAddress);

	void deleteByIp(String ipAddress);
}
