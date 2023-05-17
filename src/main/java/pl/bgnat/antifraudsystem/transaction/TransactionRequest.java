package pl.bgnat.antifraudsystem.transaction;

public record TransactionRequest(Long amount,
								 String ip,
								 String number

) {
}
