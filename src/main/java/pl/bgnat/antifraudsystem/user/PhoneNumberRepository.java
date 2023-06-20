package pl.bgnat.antifraudsystem.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface PhoneNumberRepository extends JpaRepository<PhoneNumber, Long> {
}
