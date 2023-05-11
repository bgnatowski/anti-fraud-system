package pl.bgnat.antifraudsystem.transaction;

import org.springframework.lang.NonNull;

public record TransactionRequest(@NonNull Long amount) {
}
