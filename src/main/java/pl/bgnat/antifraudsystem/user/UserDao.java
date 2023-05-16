package pl.bgnat.antifraudsystem.user;

import java.util.List;
import java.util.Optional;

interface UserDao {
	List<User> selectAllUsers();
	Optional<User> selectUserById(Long userId);
	Optional<User> selectUserByUsername(String username);
	User insertUser(User user);
	boolean existsUserWithUsername(String username);
	boolean existsUserById(Long userId);
	void updateUser(User update);
	void deleteUserById(Long userId);
	void deleteUserByUsername(String username);
}
