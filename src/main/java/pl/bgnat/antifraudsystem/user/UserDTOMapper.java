package pl.bgnat.antifraudsystem.user;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.user.User;
import pl.bgnat.antifraudsystem.user.dto.UserDTO;

import java.util.function.Function;

@Component
class UserDTOMapper implements Function<User, UserDTO> {
	@Override
	public UserDTO apply(User user) {
		return new UserDTO(user.getId(), user.getName(), user.getUsername(), user.getRole());
	}
}
