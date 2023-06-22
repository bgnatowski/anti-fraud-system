package pl.bgnat.antifraudsystem.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.user.dto.TemporaryAuthorizationDTO;
import pl.bgnat.antifraudsystem.user.exceptions.TemporaryAuthorizationNotFoundException;

@Service
@RequiredArgsConstructor
public class TemporaryAuthorizationService {
	private final TemporaryAuthorizationRepository temporaryAuthorizationRepository;
	private final TemporaryAuthorizationDTOMapper temporaryAuthorizationDTOMapper;
	public TemporaryAuthorization getTemporaryAuthorizationByUsername(String username) {
		TemporaryAuthorization temporaryAuthorization = temporaryAuthorizationRepository.findTemporaryAuthorizationByUserUsername(username)
				.orElseThrow(()-> new TemporaryAuthorizationNotFoundException(username));
//		temporaryAuthorizationRepository.delete(temporaryAuthorization);
		return temporaryAuthorization;
	}

	public TemporaryAuthorizationDTO mapToDto(TemporaryAuthorization temporaryAuthorization){
		return temporaryAuthorizationDTOMapper.apply(temporaryAuthorization);
	}
}
