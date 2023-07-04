package pl.bgnat.antifraudsystem.bank.transaction.dto;

import lombok.Builder;

@Builder
public record TransactionRequest(Long amount,
								 String ip,
								 String number,
								 String region,
								 String date
) {
}
