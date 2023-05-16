package pl.bgnat.antifraudsystem.transaction;

import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.exception.RequestValidationException;
import pl.bgnat.antifraudsystem.transaction.request.TransactionRequest;
import pl.bgnat.antifraudsystem.transaction.response.TransactionResponse;

import java.util.Objects;
import java.util.stream.Stream;

import static pl.bgnat.antifraudsystem.exception.RequestValidationException.*;

@Service
public class TransactionService {
	private final TransactionValidator transactionValidator;

	public TransactionService(TransactionValidator transactionValidator) {
		this.transactionValidator = transactionValidator;
	}

	public TransactionResponse validTransaction(TransactionRequest transactionRequest){
		if(isValidRequestJsonFormat(transactionRequest))
			throw new RequestValidationException(WRONG_JSON_FORMAT);

		Long amount = transactionRequest.amount();
		if(amount <= 0)
			throw new RequestValidationException("Wrong request!");
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
