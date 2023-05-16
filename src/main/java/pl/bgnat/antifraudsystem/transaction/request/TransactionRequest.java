package pl.bgnat.antifraudsystem.transaction.request;

public record TransactionRequest(Long amount,
								 String ip,
								 String number

) {
}
