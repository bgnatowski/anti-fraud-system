package pl.bgnat.antifraudsystem.transaction;

import org.springframework.stereotype.Service;

@Service
public class TransactionService {
	public TransactionResponse validTransaction(Long amount){
		if (amount <= 200)
			return new TransactionResponse(TransactionStatus.ALLOWED);
		else if (amount <= 1500)
			return new TransactionResponse(TransactionStatus.MANUAL_PROCESSING);
		else
			return new TransactionResponse(TransactionStatus.PROHIBITED);
	}

}
