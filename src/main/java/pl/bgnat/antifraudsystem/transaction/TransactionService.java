package pl.bgnat.antifraudsystem.transaction;

import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.exception.RequestValidationException;
import pl.bgnat.antifraudsystem.exception.stolenCard.CardNumberFormatException;
import pl.bgnat.antifraudsystem.exception.suspiciousIP.IpFormatException;
import pl.bgnat.antifraudsystem.transaction_security.stolenCards.StolenCardFacade;
import pl.bgnat.antifraudsystem.transaction_security.suspiciousIP.SuspiciousIPFacade;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static pl.bgnat.antifraudsystem.exception.RequestValidationException.WRONG_JSON_FORMAT;

@Service
class TransactionService {
	public static final String WRONG_REQUEST_AMOUNT_HAVE_TO_BE_POSITIVE_NUMBER = "Wrong request! Amount have to be positive number!";
	private final TransactionValidator transactionValidator;
	TransactionService(TransactionValidator transactionValidator, StolenCardFacade stolenCardFacade, SuspiciousIPFacade suspiciousIPFacade) {
		this.transactionValidator = transactionValidator;
	}

	//TODO refactor maybe chain of responsibility for validation
	TransactionResponse validTransaction(TransactionRequest transactionRequest){
		if(!isValidRequestJsonFormat(transactionRequest))
			throw new RequestValidationException(WRONG_JSON_FORMAT);

		Long amount = transactionRequest.amount();
		String ip = transactionRequest.ip();
		String cardNumber = transactionRequest.number();
		List<String> info = new ArrayList<>();

		if(!transactionValidator.isValidCardNumber(cardNumber))
			throw new CardNumberFormatException(cardNumber);
		if(!transactionValidator.isValidIpAddress(ip))
			throw new IpFormatException(ip);
		if(amount <= 0)
			throw new RequestValidationException(WRONG_REQUEST_AMOUNT_HAVE_TO_BE_POSITIVE_NUMBER);

		if(transactionValidator.isBlacklistedCard(cardNumber))
			info.add("card-number");
		if(transactionValidator.isBlacklistedIP(ip))
			info.add("ip");

		if (amount <= 200)
			return new TransactionResponse(TransactionStatus.ALLOWED, "none");
		else if (amount <= 1500){
			info.add("amount");
			String result = String.join(", ", info.stream().sorted().toList());
			return new TransactionResponse(TransactionStatus.MANUAL_PROCESSING, result);
		}
		else{
			info.add("amount");
			String result = String.join(", ", info.stream().sorted().toList());
			return new TransactionResponse(TransactionStatus.PROHIBITED, result);
		}
	}

	private boolean isValidRequestJsonFormat(TransactionRequest request) {
		return Stream.of(request.number(), request.amount(), request.ip()).noneMatch(Objects::isNull);
	}

}
