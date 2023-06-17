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
	@Column(name = "firstName", nullable = false, columnDefinition = "TEXT")
	private String firstName;
	@Column(name = "lastName", nullable = false, columnDefinition = "TEXT")
	private String lastName;
	@Column(name = "username", nullable = false, columnDefinition = "TEXT", unique = true)
	private String username;
	@Column(name = "password", nullable = false, columnDefinition = "TEXT")
	private String password;
	@Column(name = "email", nullable = false, columnDefinition = "TEXT", unique=true)
	private String email;

	@OneToOne(mappedBy = "owner",
			orphanRemoval = true,
			cascade = CascadeType.ALL)
	private Account account;

	@OneToOne(mappedBy = "user",
			orphanRemoval = true,
			cascade = CascadeType.ALL)
	private Address address;

	@OneToOne(mappedBy = "user",
			orphanRemoval = true,
			cascade = CascadeType.ALL)
	private PhoneNumber phone;

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

	public void setId(Long id) {
		this.id = id;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public PhoneNumber getPhone() {
		return phone;
	}

	public void setPhone(PhoneNumber phone) {
		this.phone = phone;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}
}
