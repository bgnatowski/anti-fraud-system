package pl.bgnat.antifraudsystem.bank.transaction.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.bank.transaction.dto.TransactionRequest;
import pl.bgnat.antifraudsystem.bank.transaction.dto.TransactionResponse;
import pl.bgnat.antifraudsystem.bank.transaction.validation.TransactionValidatorFacade;

@Service
@RequiredArgsConstructor
class TransactionService {
	private final TransactionValidatorFacade validatorChainFacade;
	private final TransactionRepository transactionRepository;
	private final TransactionMapper transactionMapper;

	TransactionResponse transferTransaction(TransactionRequest transactionRequest) {
		TransactionResponse transactionResponse = validatorChainFacade.valid(transactionRequest);

		Transaction transaction = transactionMapper.apply(transactionRequest);
		transaction.setStatus(transactionResponse.result());
		transactionRepository.save(transaction);

		return transactionResponse;
	}
}
