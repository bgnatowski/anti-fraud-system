package pl.bgnat.antifraudsystem.domain.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.domain.cards.stolencard.StolenCardFacade;
import pl.bgnat.antifraudsystem.domain.request.TransactionRequest;
import pl.bgnat.antifraudsystem.domain.susip.SuspiciousIPFacade;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


@Component
@RequiredArgsConstructor
public class TransactionFacade {
    private final TransactionRepository transactionRepository;
    private final SuspiciousIPFacade suspiciousIPFacade;
    private final StolenCardFacade stolenCardFacade;

    public int checkRegionHistory(TransactionRequest request) {
        LocalDateTime oneHourAgo = request.date().minus(1, ChronoUnit.HOURS);
        return transactionRepository
                .countTransactionsWithDistinctRegion(
                        TransactionRegion.valueOf(request.region()), request.number(), oneHourAgo, request.date());
    }

    public int checkUniqueIpHistory(TransactionRequest request) {
        LocalDateTime oneHourAgo = request.date().minus(1, ChronoUnit.HOURS);
        return transactionRepository.countTransactionWithDistinctIp(request.ip(), request.number(), oneHourAgo, request.date());
    }

    public boolean isBlacklistedIp(String ip) {
        return suspiciousIPFacade.isBlacklisted(ip);
    }

    public boolean isBlacklistedCardNumber(String cardNumber) {
        return stolenCardFacade.isBlacklisted(cardNumber);
    }

    public boolean isValidCarNumber(String cardNumber) {
        return stolenCardFacade.isValid(cardNumber);
    }
}
