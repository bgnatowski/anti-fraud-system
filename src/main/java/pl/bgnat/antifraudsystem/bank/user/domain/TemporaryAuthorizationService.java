package pl.bgnat.antifraudsystem.bank.user.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.bank.user.exceptions.TemporaryAuthorizationNotFoundException;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
class TemporaryAuthorizationService {
	private final TemporaryAuthorizationRepository temporaryAuthorizationRepository;
	private final Clock clock;
	TemporaryAuthorization getTemporaryAuthorization(String username) {
		TemporaryAuthorization temporaryAuthorization = findByUsername(username);

		if(isExpired(temporaryAuthorization))
			temporaryAuthorizationRepository.delete(temporaryAuthorization);

		return temporaryAuthorization;

	}

	private boolean isExpired(TemporaryAuthorization temporaryAuthorization) {
		return temporaryAuthorization.getExpirationDate().isBefore(LocalDateTime.now(clock));
	}

	private TemporaryAuthorization findByUsername(String username){
		return temporaryAuthorizationRepository.findTemporaryAuthorizationByUserUsername(username)
				.orElseThrow(() -> new TemporaryAuthorizationNotFoundException(username));
	}
}
