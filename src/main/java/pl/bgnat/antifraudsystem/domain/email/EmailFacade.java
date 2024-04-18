package pl.bgnat.antifraudsystem.domain.email;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.domain.tempauth.TemporaryAuthorization;

@Component
@RequiredArgsConstructor
public class EmailFacade {
    private final EmailService emailService;

    public void sendConfirmationEmail(String email, String code) {
        emailService.sendConfirmationEmail(email, code);
    }

    public void confirmEmail(TemporaryAuthorization userTemporaryAuthorization, String code) {
        emailService.confirmEmail(userTemporaryAuthorization, code);
    }

    public void sendCreditCardPin(String email, String pin) {
        emailService.sendCreditCardPin(email, pin);
    }
}
