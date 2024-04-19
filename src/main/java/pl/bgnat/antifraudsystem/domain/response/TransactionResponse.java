package pl.bgnat.antifraudsystem.domain.response;


import lombok.Builder;
import pl.bgnat.antifraudsystem.domain.transaction.TransactionStatus;

@Builder
public record TransactionResponse(TransactionStatus result, String info) {
}
