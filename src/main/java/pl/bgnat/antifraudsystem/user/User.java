package pl.bgnat.antifraudsystem.user;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity(name = "User")
@Table(name = "_user",
		uniqueConstraints = {
				@UniqueConstraint(
						name = "user_username_constraint",
						columnNames = "username"
				)
		})
public class User implements UserDetails {
	@Id
	@SequenceGenerator(name = "user_id_sequence", sequenceName = "user_id_sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_sequence")
	private Long id;
	@Column(name = "name", nullable = false, columnDefinition = "TEXT")
	private String name;
	@Column(name = "username", nullable = false, columnDefinition = "TEXT", unique = true)
	private String username;
	@Column(name = "password", nullable = false, columnDefinition = "TEXT")
	private String password;

	@Column(name = "role", nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role;

	@Column(name = "account_non_locked")
	private boolean accountNonLocked;

	public User(String name, String username, String password) {
		this.name = name;
		this.username = username;
		this.password = password;
	}

	public User(Long id, String name, String username, String password, Role role) {
		this.id = id;
		this.name = name;
		this.username = username;
		this.password = password;
		this.role = role;
	}

	public User(String name, String username, String password, Role role) {
		this.name = name;
		this.username = username;
		this.password = password;
		this.role = role;
	}

	public User(String name, String username, String password, Role role, boolean accountNonLocked) {
		this.name = name;
		this.username = username;
		this.password = password;
		this.role = role;
		this.accountNonLocked = accountNonLocked;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(role.name()));
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Role getRole() {
		return role;
	}


	public void setRole(Role role) {
		this.role = role;
	}

	public void lockAccount(){
		if(accountNonLocked) accountNonLocked = false;
	}

	public void unlockAccount(){
		if(!accountNonLocked) accountNonLocked = true;
	}
}
