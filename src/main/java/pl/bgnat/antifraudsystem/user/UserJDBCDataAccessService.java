package pl.bgnat.antifraudsystem.user;

import lombok.SneakyThrows;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service("jdbc")
// for training sql queries
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
					LIMIT 100
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

	@SneakyThrows
	@Override
	public User insertUser(@NonNull User user) {
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

		// Pobierz wstawionego użytkownika z bazy danych
		if(result == 1){
			var getInsertedUserSql = "SELECT * FROM _user WHERE username = ?";
			return jdbcTemplate.queryForObject(getInsertedUserSql,
					userRowMapper, user.getUsername());
		} else {
			throw new SQLException("Cannot insert user");
		}
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
					update.getRole().name(),
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
