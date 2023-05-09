package pl.bgnat.antifraudsystem.user;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserRowMapper implements RowMapper {
	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		var id = rs.getLong("id");
		var name = rs.getString("name");
		var username = rs.getString("username");
		var password = rs.getString("password");
		var roleStr = rs.getString("role");
		var role = Role.valueOf(roleStr);
		var accountNonLocked = rs.getBoolean("account_non_locked");
		return new User(id, name, username, password, role, accountNonLocked);
	}
}
