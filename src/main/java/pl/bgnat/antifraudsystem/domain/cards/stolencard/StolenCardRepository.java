package pl.bgnat.antifraudsystem.domain.cards.stolencard;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
interface StolenCardRepository extends JpaRepository<StolenCard, Long> {
    void deleteByNumber(String number);

    boolean existsByNumber(String number);
}
