package pl.bgnat.antifraudsystem.bank.transaction.dto;


public record TransactionResponse(TransactionStatus result, String info) {
}
