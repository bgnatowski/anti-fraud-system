package pl.bgnat.antifraudsystem.dto.response;


import lombok.Builder;
import pl.bgnat.antifraudsystem.dto.TransactionStatus;

@Builder
public record TransactionResponse(TransactionStatus result, String info) {
}
