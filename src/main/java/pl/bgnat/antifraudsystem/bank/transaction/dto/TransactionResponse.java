package pl.bgnat.antifraudsystem.bank.transaction.dto;


import lombok.Builder;

@Builder
public record TransactionResponse(TransactionStatus result, String info) {
}
