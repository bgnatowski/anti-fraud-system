package pl.bgnat.antifraudsystem.transaction;

import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.exception.RequestValidationException;
import pl.bgnat.antifraudsystem.exception.stolenCard.CardNumberFormatException;
import pl.bgnat.antifraudsystem.exception.suspiciousIP.IpFormatException;
import pl.bgnat.antifraudsystem.transaction.request.TransactionRequest;
import pl.bgnat.antifraudsystem.transaction.response.TransactionResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static pl.bgnat.antifraudsystem.exception.RequestValidationException.WRONG_JSON_FORMAT;

@Service
class TransactionService {
	private final TransactionValidator transactionValidator;

	TransactionService(TransactionValidator transactionValidator) {
		this.transactionValidator = transactionValidator;
	}

	TransactionResponse validTransaction(TransactionRequest transactionRequest){
		if(isValidRequestJsonFormat(transactionRequest))
			throw new RequestValidationException(WRONG_JSON_FORMAT);

		Long amount = transactionRequest.amount();
		String ip = transactionRequest.ip();
		String cardNumber = transactionRequest.number();
		List<String> info = new ArrayList<>();

		if(transactionValidator.isValidCardNumber(cardNumber)) {
			throw new CardNumberFormatException(cardNumber);
		}
		if(transactionValidator.isValidIpAddress(ip)) {
			throw new IpFormatException(ip);
		}
		if(amount <= 0) {
			throw new RequestValidationException("Wrong request! Amount have to be positive number!");
		}


		if (amount <= 200)
			return new TransactionResponse(TransactionStatus.ALLOWED, "none");
		else if (amount <= 1500)
			return new TransactionResponse(TransactionStatus.MANUAL_PROCESSING, "none");
		else
			return new TransactionResponse(TransactionStatus.PROHIBITED, "none");
	}

	private boolean isValidRequestJsonFormat(TransactionRequest request) {
		return Stream.of(request.number(), request.amount(), request.ip()).noneMatch(Objects::isNull);
	}

}
