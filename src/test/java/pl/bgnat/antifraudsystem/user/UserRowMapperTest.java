package pl.bgnat.antifraudsystem.user;

import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserRowMapperTest {
	@Test
	void mapRow() throws SQLException{
		UserRowMapper userRowMapper = new UserRowMapper();

		ResultSet resultSet = mock(ResultSet.class);
		when(resultSet.getLong("id")).thenReturn(1L);
		when(resultSet.getString("name")).thenReturn("user");
		when(resultSet.getString("username")).thenReturn("username");
		when(resultSet.getString("password")).thenReturn("password");
		when(resultSet.getString("role")).thenReturn("ADMINISTRATOR");
		when(resultSet.getBoolean("account_non_locked")).thenReturn(true);

		// When
		User actual = userRowMapper.mapRow(resultSet, 1);


		// Then
		User expected = User.builder()
				.id(1L)
				.name("user")
				.username("username")
				.password("password")
				.role(Role.ADMINISTRATOR)
				.accountNonLocked(true)
				.build();

		assertThat(actual).isEqualTo(expected);
	}
}
