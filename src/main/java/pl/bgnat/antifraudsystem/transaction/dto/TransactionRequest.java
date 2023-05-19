package pl.bgnat.antifraudsystem.transaction.dto;

public record TransactionRequest(Long amount,
								 String ip,
								 String number

) {
}
