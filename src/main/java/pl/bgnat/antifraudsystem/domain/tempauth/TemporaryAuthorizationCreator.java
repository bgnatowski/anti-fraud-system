package pl.bgnat.antifraudsystem.domain.tempauth;

import pl.bgnat.antifraudsystem.utils.date.DateTimeUtils;

import java.util.Random;

class TemporaryAuthorizationCreator {
    static final int EXPIRATION_TIME_IN_MINUTES = 1;

    static TemporaryAuthorization createTemporaryAuthorization() {
        return TemporaryAuthorization.builder()
                .code(generateConfirmationCode())
                .expirationDate(DateTimeUtils.currentLocalDateTime().plusMinutes(EXPIRATION_TIME_IN_MINUTES))
                .build();
    }

    static String generateConfirmationCode() {
        Random random = new Random();
        int code = random.nextInt(90000) + 10000;
        return String.valueOf(code);
    }
}
