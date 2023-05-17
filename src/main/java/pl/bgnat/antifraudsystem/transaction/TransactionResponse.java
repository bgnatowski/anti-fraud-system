package pl.bgnat.antifraudsystem.transaction;

import pl.bgnat.antifraudsystem.transaction.TransactionStatus;
record TransactionResponse(TransactionStatus result, String info) {
}
