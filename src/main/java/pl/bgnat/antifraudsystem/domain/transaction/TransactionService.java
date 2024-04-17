package pl.bgnat.antifraudsystem.domain.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.dto.request.TransactionRequest;
import pl.bgnat.antifraudsystem.dto.response.TransactionResponse;
import pl.bgnat.antifraudsystem.domain.transaction.validator.TransactionValidatorFacade;

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
