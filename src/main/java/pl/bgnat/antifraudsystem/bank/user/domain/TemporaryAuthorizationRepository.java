package pl.bgnat.antifraudsystem.bank.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface TemporaryAuthorizationRepository extends JpaRepository<TemporaryAuthorization, Long> {
	Optional<TemporaryAuthorization> findTemporaryAuthorizationByUserUsername(String user_username);
}
