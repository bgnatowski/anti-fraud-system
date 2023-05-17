package pl.bgnat.antifraudsystem.transaction.transaction_validation;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.transaction.TransactionRequest;

import java.util.List;

@Component
interface TransactionValidator {
	List<String> isValid(TransactionRequest request, List<String> info);
}
