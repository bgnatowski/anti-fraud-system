package pl.bgnat.antifraudsystem.domain.tempauth;

import pl.bgnat.antifraudsystem.domain.user.User;
import pl.bgnat.antifraudsystem.utils.date.DateTimeUtils;
import pl.bgnat.antifraudsystem.utils.generator.MailConfirmationCodeGenerator;

public class TemporaryAuthorizationCreator {
    static final int EXPIRATION_TIME_IN_MINUTES = 15;

    public static TemporaryAuthorization createTemporaryAuthorization(User createdUser) {
        return TemporaryAuthorization.builder()
                .user(createdUser)
                .code(MailConfirmationCodeGenerator.generateConfirmationCode())
                .expirationDate(DateTimeUtils.currentLocalDateTime().plusMinutes(EXPIRATION_TIME_IN_MINUTES))
                .build();
    }
}
