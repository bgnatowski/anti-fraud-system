package pl.bgnat.antifraudsystem.user;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jpa")
public class UserJPADataAccessService implements UserDao{
	private final UserRepository userRepository;

	public UserJPADataAccessService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public List<User> selectAllUsers() {
		return userRepository.findAll();
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
	public void insertUser(User user) {
		userRepository.save(user);
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
	public void updateUser(User update) {
		userRepository.save(update);
	}

	@Override
	public void deleteUserById(Long userId) {
		userRepository.deleteById(userId);
	}

	@Override
	public void deleteUserByUsername(String username) {
		userRepository.deleteUserByUsername(username);
	}
}
