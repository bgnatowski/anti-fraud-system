package pl.bgnat.antifraudsystem.bank.user.domain;

import pl.bgnat.antifraudsystem.utils.date.DateTimeUtils;
import pl.bgnat.antifraudsystem.utils.generator.MailConfirmationCodeGenerator;

class TemporaryAuthorizationCreator {
    static final int EXPIRATION_TIME_IN_MINUTES = 15;

    static TemporaryAuthorization createTemporaryAuthorization(User createdUser) {
        return TemporaryAuthorization.builder()
                .user(createdUser)
                .code(MailConfirmationCodeGenerator.generateConfirmationCode())
                .expirationDate(DateTimeUtils.currentLocalDateTime().plusMinutes(EXPIRATION_TIME_IN_MINUTES))
                .build();
    }
}
