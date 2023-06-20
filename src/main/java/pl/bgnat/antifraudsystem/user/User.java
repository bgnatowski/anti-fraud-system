package pl.bgnat.antifraudsystem.user;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.bgnat.antifraudsystem.user.enums.Role;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder

@Entity(name = "User")
@Table(name = "_user",
		uniqueConstraints = {
				@UniqueConstraint(
						name = "user_username_constraint",
						columnNames = "username"
				),
				@UniqueConstraint(
						name = "user_email_constraint",
						columnNames = "email"
				)
		})
class User implements UserDetails {
	@Id
	@SequenceGenerator(name = "user_id_sequence", sequenceName = "user_id_sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_sequence")
	@Column(name = "id")
	private Long id;
	@Column(name = "first_name", nullable = false, columnDefinition = "TEXT")
	private String firstName;
	@Column(name = "last_name", nullable = false, columnDefinition = "TEXT")
	private String lastName;
	@Column(name = "username", nullable = false, columnDefinition = "TEXT", unique = true)
	private String username;
	@Column(name = "password", nullable = false, columnDefinition = "TEXT")
	private String password;
	@Column(name = "email", nullable = false, columnDefinition = "TEXT", unique = true)
	private String email;

	@OneToOne(mappedBy = "user",
			cascade = CascadeType.ALL,
			fetch = FetchType.EAGER)
	private TemporaryAuthorization temporaryAuthorization;

	@OneToOne(mappedBy = "owner",
			orphanRemoval = true,
			cascade = CascadeType.ALL,
			fetch = FetchType.EAGER
	)
	private Account account;

	@OneToOne(mappedBy = "user",
			orphanRemoval = true,
			cascade = CascadeType.ALL,
			fetch = FetchType.EAGER)
	private Address address;

	@OneToOne(mappedBy = "user",
			orphanRemoval = true,
			cascade = CascadeType.ALL,
			fetch = FetchType.EAGER)
	private PhoneNumber phone;

	@OneToMany(mappedBy = "owner",
			cascade = CascadeType.ALL,
			fetch = FetchType.LAZY
	)
	private final Set<CreditCard> creditCards = new HashSet<>();

	@Column(name = "role", nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role;

	@Column(name = "account_non_locked")
	private boolean accountNonLocked;

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

	public String getFirstName() {
		return firstName;
	}


	public void lockAccount() {
		if (accountNonLocked) accountNonLocked = false;
	}

	public void unlockAccount() {
		if (!accountNonLocked) accountNonLocked = true;
	}
}
