package pl.bgnat.antifraudsystem.user;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.user.dto.UserDTO;

import java.util.function.Function;

@Component
class UserDTOMapper implements Function<User, UserDTO> {
	private final AddressDTOMapper addressDTOMapper;
	private final PhoneNumberDTOMapper phoneNumberDTOMapper;

	UserDTOMapper(AddressDTOMapper addressDTOMapper, PhoneNumberDTOMapper phoneNumberDTOMapper) {
		this.addressDTOMapper = addressDTOMapper;
		this.phoneNumberDTOMapper = phoneNumberDTOMapper;
	}

	@Override
	public UserDTO apply(User user) {
		return new UserDTO(
				user.getId(),
				user.getFirstName(),
				user.getLastName(),
				user.getUsername(),
				user.getEmail(),
				user.getRole(),
				user.isAccountNonLocked(),
				addressDTOMapper.apply(user.getAddress()),
				phoneNumberDTOMapper.apply(user.getPhone()));
	}
}
