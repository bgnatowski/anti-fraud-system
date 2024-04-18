package pl.bgnat.antifraudsystem.domain.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.domain.request.TransactionRequest;
import pl.bgnat.antifraudsystem.domain.response.TransactionResponse;
import pl.bgnat.antifraudsystem.domain.transaction.validator.TransactionValidatorFacade;

@Service
@RequiredArgsConstructor
class TransactionService {
    private final TransactionValidatorFacade validatorChainFacade;
    private final TransactionRepository transactionRepository;

    TransactionResponse transferTransaction(TransactionRequest transactionRequest) {
        TransactionResponse transactionResponse = validatorChainFacade.valid(transactionRequest);

        Transaction transaction = createTransaction(transactionRequest);
        transaction.setStatus(transactionResponse.result());
        transactionRepository.save(transaction);

        return transactionResponse;
    }

    private Transaction createTransaction(TransactionRequest request) {
        return Transaction.builder()
                .amount(request.amount())
                .ipAddress(request.ip())
                .cardNumber(request.number())
                .region(TransactionRegion.valueOf(request.region()))
                .date(request.date())
                .build();
    }
}
