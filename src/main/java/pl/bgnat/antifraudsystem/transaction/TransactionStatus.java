package pl.bgnat.antifraudsystem.transaction;

import org.springframework.stereotype.Component;

@Component
public enum TransactionStatus {
	ALLOWED, PROHIBITED, MANUAL_PROCESSING
}
