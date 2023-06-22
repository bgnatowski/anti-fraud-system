package pl.bgnat.antifraudsystem.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.user.dto.TemporaryAuthorizationDTO;
import pl.bgnat.antifraudsystem.user.exceptions.TemporaryAuthorizationNotFoundException;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TemporaryAuthorizationService {
	private final TemporaryAuthorizationRepository temporaryAuthorizationRepository;
	private final Clock clock;
	public TemporaryAuthorization getTemporaryAuthorization(String username) {
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
