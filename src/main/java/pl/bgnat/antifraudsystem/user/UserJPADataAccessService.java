package pl.bgnat.antifraudsystem.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("jpa")
class UserJPADataAccessService implements UserDao{
	private final UserRepository userRepository;

	UserJPADataAccessService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public List<User> selectAllUsers() {
		Page<User> page = userRepository.findAll(Pageable.ofSize(100));
		return page.getContent();
	}

	@Override
	public Optional<User> selectUserById(Long userId) {
		return userRepository.findById(userId);
	}

	@Override
	public Optional<User> selectUserByUsername(String username) {
		return userRepository.findUserByUsername(username);
	}

	@Override
	public User insertUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public boolean existsUserWithUsername(String username) {
		return userRepository.existsUserByUsername(username);
	}

	@Override
	public boolean existsUserById(Long userId) {
		return userRepository.existsById(userId);
	}

	@Override
	public void deleteUserById(Long userId) {
		userRepository.deleteById(userId);
	}

	@Override
	public void deleteUserByUsername(String username) {
		userRepository.deleteUserByUsername(username);
	}

	@Override
	public void updateUser(User update) {
		userRepository.save(update);
	}
}
