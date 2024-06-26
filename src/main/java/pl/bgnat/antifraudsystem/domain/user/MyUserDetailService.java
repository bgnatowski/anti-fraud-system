package pl.bgnat.antifraudsystem.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class MyUserDetailService implements UserDetailsService {
    private static final String USER_WITH_USERNAME_S_NOT_FOUND = "User with username = %s not found";
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_WITH_USERNAME_S_NOT_FOUND, username)));
    }
}
