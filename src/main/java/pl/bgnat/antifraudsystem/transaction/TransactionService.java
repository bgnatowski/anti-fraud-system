package pl.bgnat.antifraudsystem.transaction;

import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.exception.RequestValidationException;
import pl.bgnat.antifraudsystem.exception.stolenCard.CardNumberFormatException;
import pl.bgnat.antifraudsystem.exception.suspiciousIP.IpFormatException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static pl.bgnat.antifraudsystem.exception.RequestValidationException.WRONG_JSON_FORMAT;

@Service
class TransactionService {
	private final TransactionValidatorChain validatorChain;
	TransactionService(TransactionValidatorChain validatorChain) {
		this.validatorChain = validatorChain;
	}
	TransactionResponse validTransaction(TransactionRequest transactionRequest){
		TransactionValidator transactionValidationFilterChain = validatorChain.getTransactionValidationFilterChain();
		List<String> info = transactionValidationFilterChain.isValid(transactionRequest, new ArrayList<>());

		String result = info.isEmpty() ?
				"none" : getResultedInfoAsString(info);
		Long amount = transactionRequest.amount();

		if (amount <= 200)
			return new TransactionResponse(TransactionStatus.ALLOWED, result);
		else if (amount <= 1500)
			return new TransactionResponse(TransactionStatus.MANUAL_PROCESSING, result);
		else
			return new TransactionResponse(TransactionStatus.PROHIBITED, result);
	}

	private static String getResultedInfoAsString(List<String> info) {
		return String.join(", ", info.stream().sorted().toList());
	}

}
