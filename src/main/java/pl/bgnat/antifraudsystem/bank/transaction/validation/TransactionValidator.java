package pl.bgnat.antifraudsystem.bank.transaction.validation;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.bank.transaction.dto.TransactionRequest;

import java.util.List;

@Component
interface TransactionValidator extends Validator<List<String>> {
	List<String> valid(TransactionRequest request, List<String> info);
}
