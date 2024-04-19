package pl.bgnat.antifraudsystem.domain.tempauth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TemporaryAuthorizationFacade {
    private final TemporaryAuthorizationService temporaryAuthorizationService;

    public TemporaryAuthorization getTemporaryAuthorization(String username) {
        return temporaryAuthorizationService.getTemporaryAuthorization(username);
    }

    public TemporaryAuthorization createTemporaryAuthorization() {
        return TemporaryAuthorizationCreator.createTemporaryAuthorization();
    }
}
