package pl.bgnat.antifraudsystem.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.user.dto.UserDTO;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
class UserDTOMapper implements Function<User, UserDTO> {
	private final AddressDTOMapper addressDTOMapper;
	private final PhoneNumberDTOMapper phoneNumberDTOMapper;
	private final TemporaryAuthorizationDTOMapper temporaryAuthorizationDTOMapper;


	@Override
	public UserDTO apply(User user) {
		return new UserDTO(
				user.getId(),
				user.getFirstName(),
				user.getLastName(),
				user.getUsername(),
				user.getEmail(),
				user.getDateOfBirth(),
				user.getRole(),
				user.isAccountNonLocked(),
				addressDTOMapper.apply(user.getAddress()),
				phoneNumberDTOMapper.apply(user.getPhone()),
				temporaryAuthorizationDTOMapper.apply(user.getTemporaryAuthorization()));
	}
}
