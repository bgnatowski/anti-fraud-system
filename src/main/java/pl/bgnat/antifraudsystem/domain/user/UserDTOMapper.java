package pl.bgnat.antifraudsystem.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.dto.UserDTO;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
class UserDTOMapper implements Function<User, UserDTO> {
	@Override
	public UserDTO apply(User user) {
		return UserDTO.builder()
				.id(user.getId())
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.username(user.getUsername())
				.email(user.getEmail())
				.dateOfBirth(user.getDateOfBirth())
				.role(user.getRole())
				.isActive(user.isAccountNonLocked())
				.hasAccount(user.isHasAccount())
				.hasAnyCreditCard(user.isHasAnyCreditCard())
				.numberOfCreditCards(user.getNumberOfCreditCards())
				.build();
	}
}
