package pl.bgnat.antifraudsystem.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MyUserDetailServiceTest {
	@Mock
	private UserJPADataAccessService userDao;

	private MyUserDetailService myUserDetailsService;

	@BeforeEach
	public void setUp() {
		myUserDetailsService = new MyUserDetailService(userDao);
	}

	@Test
	public void GIVEN_username_THEN_return_user_details() {
		// Given
		final String username = "existingUserName";
		final User user = User.builder().username(username).build();
		given(userDao.selectUserByUsername(username)).willReturn(Optional.ofNullable(user));
		// When
		final UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
		//Assert
		assertThat(userDetails).isNotNull();
		assertThat(userDetails.getUsername()).isEqualTo(username);
	}
}
