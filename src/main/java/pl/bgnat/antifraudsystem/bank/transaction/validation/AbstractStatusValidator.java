package pl.bgnat.antifraudsystem.bank.transaction.validation;


import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.bank.transaction.domain.TransactionFacade;
import pl.bgnat.antifraudsystem.bank.transaction.dto.TransactionRequest;
import pl.bgnat.antifraudsystem.bank.transaction.dto.TransactionStatus;

import java.util.List;

import static pl.bgnat.antifraudsystem.bank.transaction.dto.TransactionStatus.ALLOWED;
import static pl.bgnat.antifraudsystem.bank.transaction.dto.TransactionStatus.PROHIBITED;

@Component
abstract class AbstractStatusValidator implements StatusValidator{
	private AbstractStatusValidator nextValidator;
	protected TransactionFacade transactionFacade;
	protected static TransactionStatus status = ALLOWED;
	protected AbstractStatusValidator(TransactionFacade transactionFacade) {
		this.transactionFacade = transactionFacade;
	}
	public static AbstractStatusValidator link(AbstractStatusValidator first, AbstractStatusValidator... chain) {
		AbstractStatusValidator head = first;
		for (AbstractStatusValidator nextInChain: chain) {
			head.nextValidator = nextInChain;
			head = nextInChain;
		}
		return first;
	}
	TransactionStatus nextValidation(TransactionRequest request, List<String> info) {
		if (nextValidator != null) {
			return nextValidator.valid(request, info);
		} else {
			return status;
		}
	}
	void setStatus(TransactionStatus newStatus) {
		if (status != PROHIBITED) {
			status = newStatus;
		}
	}

	@Override
	public void init() {
		status = ALLOWED;
	}
}
