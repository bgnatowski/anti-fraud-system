package pl.bgnat.antifraudsystem.transaction;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TransactionValidator {
	List<String> isValid(TransactionRequest request, List<String> info);
}
