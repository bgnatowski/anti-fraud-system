package pl.bgnat.antifraudsystem.transaction.validation;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.transaction.dto.TransactionRequest;

import java.util.List;

@Component
interface TransactionValidator {
	List<String> isValid(TransactionRequest request, List<String> info);
}
