package pl.bgnat.antifraudsystem.transaction.dto;

import lombok.Builder;

@Builder
public record TransactionRequest(Long amount,
								 String ip,
								 String number,
								 String region,
								 String date
) {
}
