package pl.bgnat.antifraudsystem.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {
	public static final String USER_WITH_USERNAME_S_NOT_FOUND = "User with username = %s not found";
	private final UserDao userDao;
	public MyUserDetailService(@Qualifier("jpa") UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userDao.selectUserByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(String.format(USER_WITH_USERNAME_S_NOT_FOUND, username)));
	}
}
