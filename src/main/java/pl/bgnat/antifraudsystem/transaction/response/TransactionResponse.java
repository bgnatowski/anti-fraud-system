package pl.bgnat.antifraudsystem.transaction.response;

import pl.bgnat.antifraudsystem.transaction.TransactionStatus;

public record TransactionResponse(TransactionStatus result, String info) {
}
