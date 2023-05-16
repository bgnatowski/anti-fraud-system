package pl.bgnat.antifraudsystem.transaction;

import org.springframework.stereotype.Component;

@Component
public class TransactionFacade {
	private final TransactionValidator transactionValidator;
	private final TransactionStatus transactionStatus;
	public TransactionFacade(TransactionValidator transactionValidator,
							 TransactionStatus transactionStatus) {
		this.transactionValidator = transactionValidator;
		this.transactionStatus = transactionStatus;
	}

	public TransactionValidator getValidator(){
		return transactionValidator;
	}
}
