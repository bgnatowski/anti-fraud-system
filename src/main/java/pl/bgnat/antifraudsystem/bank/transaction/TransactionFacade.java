package pl.bgnat.antifraudsystem.bank.transaction;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.bank.transaction.dto.TransactionRegion;
import pl.bgnat.antifraudsystem.bank.transaction.dto.TransactionRequest;
import pl.bgnat.antifraudsystem.bank.transaction.stolenCards.StolenCardFacade;
import pl.bgnat.antifraudsystem.bank.transaction.suspiciousIP.SuspiciousIPFacade;

import java.time.LocalDateTime;

import static pl.bgnat.antifraudsystem.utils.parser.DateTimeParser.getOneHourAgoDateFromString;


@Component
public class TransactionFacade {
	private final TransactionRepository transactionRepository;
	private final SuspiciousIPFacade suspiciousIPFacade;
	private final StolenCardFacade stolenCardFacade;

	public TransactionFacade(TransactionRepository transactionRepository,
							 SuspiciousIPFacade suspiciousIPFacade,
							 StolenCardFacade stolenCardFacade) {
		this.transactionRepository = transactionRepository;
		this.suspiciousIPFacade = suspiciousIPFacade;
		this.stolenCardFacade = stolenCardFacade;
	}

	public int checkRegionHistory(TransactionRequest request) {
		String dateString = request.date();
		LocalDateTime current = LocalDateTime.parse(dateString);
		LocalDateTime oneHourAgo = getOneHourAgoDateFromString(request.date());
		return transactionRepository
				.countTransactionsWithDistinctRegion(
						TransactionRegion.valueOf(request.region()), request.number(), oneHourAgo, current);
	}

	public int checkUniqueIpHistory(TransactionRequest request) {
		String dateString = request.date();
		LocalDateTime current = LocalDateTime.parse(dateString);
		LocalDateTime oneHourAgo = getOneHourAgoDateFromString(request.date());
		return transactionRepository.countTransactionWithDistinctIp(request.ip(), request.number(), oneHourAgo, current);
	}


	public void blacklistCardNumber(String number) {
		stolenCardFacade.blacklist(number);
	}

	public void blacklistIp(String ip){
		suspiciousIPFacade.blacklist(ip);
	}

	public boolean isValidIp(String ip) {
		return suspiciousIPFacade.isValid(ip);
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
