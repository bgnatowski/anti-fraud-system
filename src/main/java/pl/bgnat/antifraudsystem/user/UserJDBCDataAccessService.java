package pl.bgnat.antifraudsystem.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class UserJDBCDataAccessService implements UserDao {
	private final JdbcTemplate jdbcTemplate;
	private final UserRowMapper userRowMapper;

	public UserJDBCDataAccessService(JdbcTemplate jdbcTemplate, UserRowMapper userRowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.userRowMapper = userRowMapper;
	}

	@Override
	public List<User> selectAllUsers() {
		var sql = """
					SELECT *
					FROM _user
				""";
		return jdbcTemplate.query(sql, userRowMapper);
	}

	@Override
	public Optional<User> selectUserById(Long userId) {
		var sql = """
				SELECT *
				FROM _user 
				WHERE id = ?
				""";
		return jdbcTemplate.query(sql, userRowMapper, userId)
				.stream()
				.findFirst();
	}

	@Override
	public Optional<User> selectUserByUsername(String username) {
		var sql = """
				SELECT *
				FROM _user
				WHERE username = ?
				""";
		return jdbcTemplate.query(sql, userRowMapper, username)
				.stream()
				.findFirst();
	}

	@Override
	public void insertUser(User user) {
		var sql = """
				INSERT INTO _user(name, username, password, role, account_non_locked)
				VALUES(?,?,?,?,?)
				""";
		int result = jdbcTemplate.update(sql,
				user.getName(),
				user.getUsername(),
				user.getPassword(),
				user.getRole().name(),
				user.isAccountNonLocked());
		System.out.println("insertUser result " + result);
	}

	@Override
	public boolean existsUserWithUsername(String username) {
		var sql = """
				SELECT count(id)
				FROM _user
				WHERE username = ?
				""";
		Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username);
		return count != null && count > 0;
	}

	@Override
	public boolean existsUserById(Long userId) {
		var sql = """
				SELECT count(id)
				FROM _user
				WHERE id = ?
				""";
		Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
		return count != null && count > 0;
	}

	@Override
	public void deleteUserById(Long userId) {
		var sql = """
				DELETE
				FROM _user
				WHERE id = ?
				""";
		int result = jdbcTemplate.update(sql, userId);
		System.out.println("deleteUserById result = " + result);
	}

	@Override
	public void deleteUserByUsername(String username) {
		var sql = """
				DELETE
				FROM _user
				WHERE username = ?
				""";
		int result = jdbcTemplate.update(sql, username);
		System.out.println("deleteUserById result = " + result);
	}

	@Override
	public void updateUser(User update) {
		if (update.getName() != null) {
			String sql = "UPDATE _user SET name = ? WHERE id = ?";
			int result = jdbcTemplate.update(
					sql,
					update.getName(),
					update.getId()
			);
			System.out.println("update user name result = " + result);
		}
		if (update.getUsername() != null) {
			String sql = "UPDATE _user SET username = ? WHERE id = ?";
			int result = jdbcTemplate.update(
					sql,
					update.getUsername(),
					update.getId()
			);
			System.out.println("update user username result = " + result);
		}
		if (update.getRole() != null) {
			String sql = "UPDATE _user SET role = ? WHERE id = ?";
			int result = jdbcTemplate.update(
					sql,
					update.getRole(),
					update.getId()
			);
			System.out.println("update user role result = " + result);
		}

		String sql = "UPDATE _user SET account_non_locked = ? WHERE id = ?";
		int result = jdbcTemplate.update(
				sql,
				update.isAccountNonLocked(),
				update.getId()
		);
		System.out.println("update user account_non_locked result = " + result);

	}
}
